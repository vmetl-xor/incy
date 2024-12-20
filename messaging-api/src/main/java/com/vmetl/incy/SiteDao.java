package com.vmetl.incy;

import java.util.Map;
import java.util.Optional;

public interface SiteDao {
    void addSite(String name);

    Optional<Integer> getSiteIdByName(String siteName);

    void updateSiteStatistics(int siteId, Map<String, Integer> statistics);
}
