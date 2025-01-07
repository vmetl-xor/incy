package com.vmetl.incy.cache;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Primary
public class RedisVisitedRefsCache implements RefsCache {
    public static final String REFS_VISITED = "refs:visited";
    private final RedisTemplate<String, String> redisTemplate;
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(RedisVisitedRefsCache.class);
    private final Set<String> cache = ConcurrentHashMap.newKeySet();

    @Autowired
    public RedisVisitedRefsCache(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        SetOperations<String, String> setOps = redisTemplate.opsForSet();
        Set<String> members = setOps.members(REFS_VISITED);
        if (members != null) {
            cache.addAll(members);
            LOG.info("Populated cache for {} members", members.size());
        }
    }

    @Override
    public boolean exists(String ref) {
        boolean isInLocalCache = cache.contains(ref);
        if (isInLocalCache) return true;

        Boolean isMember = redisTemplate.opsForSet().isMember(REFS_VISITED, ref);
        boolean isInRedisCache = (isMember != null) && isMember;

        if (isInRedisCache)
            cache.add(ref);

        return isInRedisCache;
    }

    @Override
    public boolean add(String ref) {
        Long added = redisTemplate.opsForSet().add(REFS_VISITED, ref);
        if ((added == null || added == 0) && !cache.contains(ref)) {
            LOG.error("Could not add to the cache: {}", ref);
        }
        cache.add(ref);
        return (added != null && added > 0);
    }
}
