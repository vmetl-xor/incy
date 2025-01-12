package com.vmetl.incy.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
//@Primary
public class VisitedRefsLocalCache implements VisitedRefsCache {

    private static final Logger LOG = LoggerFactory.getLogger(VisitedRefsLocalCache.class);

    Set<String> cache = ConcurrentHashMap.newKeySet();

    @Override
    public boolean exists(String key) {
        boolean contains = cache.contains(key);
        LOG.debug("cache {}: {}", contains ? "hit" : "miss", key);

        return contains;
    }

    @Override
    public boolean add(String ref) {
        LOG.debug("cache add: {}, cache size: {}", ref, cache.size());

        return cache.add(ref);
    }
}
