package com.vmetl.api.service;

import com.vmetl.incy.SiteDao;
import com.vmetl.incy.SiteStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class SitesService {

    private final SiteDao dbService;

    @Autowired
    public SitesService(SiteDao dbService) {
        this.dbService = dbService;
    }


    public SiteStats getSiteStats(String siteName) {

        String siteNameWithProtocol = siteName;
        if (!siteName.startsWith("http")) {
            siteNameWithProtocol = "http://" + siteName;
        }

        return dbService.getSiteStats(siteNameWithProtocol);
    }

    public Flux<SiteStats> getSitesStats(String namePattern) {
        return dbService.getSiteStatsByNameStream(namePattern);
    }

    public Flux<SiteStats> getSiteStatsStream() {
        return dbService.getSiteStatsStream();
    }

}
