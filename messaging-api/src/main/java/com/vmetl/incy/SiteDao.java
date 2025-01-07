package com.vmetl.incy;

import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.Optional;

public interface SiteDao {
    void addSite(String name);

    Optional<Integer> getSiteIdByName(String siteName);

    void updateSiteStatistics(int siteId, Map<String, Integer> statistics);

    Flux<SiteStats> getSiteStatsStream();

    Flux<SiteStats> getSiteStatsByNameStream(String name);

    SiteStats getSiteStats(String siteName);
}
