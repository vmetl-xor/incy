package com.vmetl.incy.cache;

public interface VisitedRefsCache {
    boolean exists(String ref);
    boolean add(String ref);
}
