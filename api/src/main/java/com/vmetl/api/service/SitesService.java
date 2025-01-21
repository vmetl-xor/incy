package com.vmetl.api.service;

import com.vmetl.incy.dao.SiteDao;
import com.vmetl.incy.SiteStats;
import com.vmetl.incy.dao.reactive.SiteReactiveDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class SitesService {

    private final SiteDao dbService;
    private final SiteReactiveDao dbReactiveService;

    @Autowired
    public SitesService(SiteDao dbService, SiteReactiveDao dbReactiveService) {
        this.dbService = dbService;
        this.dbReactiveService = dbReactiveService;
    }


    public SiteStats getSiteStats(String siteName) {

        String siteNameWithProtocol = siteName;
        if (!siteName.startsWith("http")) {
            siteNameWithProtocol = "http://" + siteName;
        }

        return dbService.getSiteStats(siteNameWithProtocol);
    }

    public Flux<SiteStats> getSitesStats(String namePattern) {
        return dbReactiveService.getSiteStatsByNameStream(namePattern);
    }

    public Flux<SiteStats> getSiteStatsStream() {
        return dbReactiveService.getSiteStatsStream();
    }

}
