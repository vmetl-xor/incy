package com.vmetl.api.rest;


import com.vmetl.api.service.JobService;
import com.vmetl.incy.CacheAwareDbService;
import com.vmetl.incy.SiteDao;
import com.vmetl.incy.SiteStats;
import com.vmetl.incy.messaging.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;
    private final SiteDao dbService;

    @Autowired
    public JobController(JobService jobService, SiteDao dbService) {
        this.jobService = jobService;
        this.dbService = dbService;
    }


    @GetMapping(value = "/{id}")
    public Message findById(@PathVariable("id") Long id) {
        return jobService.sendMessage(Job.of("https://en.wikipedia.org/wiki/Charles_Dickens", 0));
    }

    @PostMapping(value = "/add")
    public Message createJob(@RequestBody Job job) {
        return jobService.sendMessage(job);
    }

    @GetMapping(value = "/sites/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<SiteStats> getAllSitesStats() {
        return dbService.getSiteStatsStream();
    }

    @GetMapping(value = "/stop")
    public String stopAll() {
        jobService.stopAllProcessors();

        return "All stopped";
    }
}
