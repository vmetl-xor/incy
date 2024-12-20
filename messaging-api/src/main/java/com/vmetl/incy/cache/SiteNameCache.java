package com.vmetl.incy.cache;

import java.util.Optional;

public interface SiteNameCache {
    Optional<Integer> getIdByName(String siteName);

    boolean addSiteName(String siteName, Integer siteId);
}
