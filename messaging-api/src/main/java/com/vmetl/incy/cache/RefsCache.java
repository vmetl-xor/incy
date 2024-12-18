package com.vmetl.incy.cache;

public interface RefsCache {
    boolean exists(String ref);
    boolean add(String ref);
}
