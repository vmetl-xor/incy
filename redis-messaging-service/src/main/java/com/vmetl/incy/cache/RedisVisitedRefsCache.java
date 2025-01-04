package com.vmetl.incy.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Primary
public class RedisVisitedRefsCache implements RefsCache {
    public static final String REFS_VISITED = "refs:visited";
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisVisitedRefsCache(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        redisTemplate.opsForSet().add(REFS_VISITED, "-");
    }

    @Override
    public boolean exists(String ref) {
        Boolean isMember = redisTemplate.opsForSet().isMember(REFS_VISITED, ref);
        return isMember != null && isMember;
    }

    @Override
    public boolean add(String ref) {
        Long added = redisTemplate.opsForSet().add(REFS_VISITED);
        return (added != null && added > 0);
    }
}
