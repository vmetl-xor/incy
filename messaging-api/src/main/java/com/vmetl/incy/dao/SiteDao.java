package com.vmetl.incy.dao;

import com.vmetl.incy.SiteStats;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.Optional;

public interface SiteDao {
    void addSite(String name);

    Optional<Integer> getSiteIdByName(String siteName);

    void updateSiteStatistics(int siteId, Map<String, Integer> statistics);

    SiteStats getSiteStats(String siteName);
}
