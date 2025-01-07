package com.vmetl.api.service;

import com.vmetl.api.rest.Job;
import com.vmetl.incy.SiteDao;
import com.vmetl.incy.SiteStats;
import com.vmetl.incy.messaging.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class SitesService {

    private final JobService jobService;
    private final SiteDao dbService;

    @Autowired
    public SitesService(JobService jobService, SiteDao dbService) {
        this.jobService = jobService;
        this.dbService = dbService;
    }


    public Flux<SiteStats> getSiteStats(String siteName) {
        return dbService.getSiteStatsByNameStream(siteName);
    }

    public Message sendMessage(Job job) {
        return jobService.sendMessage(job);
    }

    public Flux<SiteStats> getSiteStatsStream() {
        return dbService.getSiteStatsStream();
    }

    public void stopAllProcessors() {
        jobService.stopAllProcessors();
    }
}
