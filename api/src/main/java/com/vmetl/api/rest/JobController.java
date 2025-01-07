package com.vmetl.api.rest;


import com.vmetl.api.service.SitesService;
import com.vmetl.incy.SiteStats;
import com.vmetl.incy.messaging.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final SitesService sitesService;

    @Autowired
    public JobController(SitesService sitesService) {
        this.sitesService = sitesService;
    }


    @GetMapping(value = "/{id}")
    public Message findById(@PathVariable("id") Long id) {
        return sitesService.sendMessage(Job.of("https://en.wikipedia.org/wiki/Charles_Dickens", 0));
    }

    @PostMapping(value = "/add")
    public Message createJob(@RequestBody Job job) {
        return sitesService.sendMessage(job);
    }

    @GetMapping(value = "/sites/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<SiteStats> getAllSitesStats() {
        return sitesService.getSiteStatsStream();
    }

    public Flux<SiteStats> getSiteStats() {
        return sitesService.getSiteStats("");
    }

    @GetMapping(value = "/stop")
    public String stopAll() {
        sitesService.stopAllProcessors();

        return "All stopped";
    }
}
