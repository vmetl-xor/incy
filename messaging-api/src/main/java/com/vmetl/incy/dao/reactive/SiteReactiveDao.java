package com.vmetl.incy.dao.reactive;

import com.vmetl.incy.SiteStats;
import reactor.core.publisher.Flux;

public interface SiteReactiveDao {
    Flux<SiteStats> getSiteStatsStream();

    Flux<SiteStats> getSiteStatsByNameStream(String name);
}
