package com.vmetl.crawler.cache;

public interface RefsCache {
    boolean exists(String ref);
    boolean add(String ref);
}
