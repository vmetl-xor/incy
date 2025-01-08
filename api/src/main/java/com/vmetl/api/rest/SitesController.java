package com.vmetl.api.rest;

import com.vmetl.api.service.SitesService;
import com.vmetl.incy.SiteStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/sites")
public class SitesController {
    private final SitesService sitesService;

    @Autowired
    public SitesController(SitesService sitesService) {
        this.sitesService = sitesService;
    }

    @GetMapping(value = "/{name}")
    public SiteStats findByName(@PathVariable("name") String name) {
        return sitesService.getSiteStats(name);
    }

    @GetMapping(value = "/stream/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<SiteStats> getAllSitesStats() {
        return sitesService.getSiteStatsStream();
    }

    @GetMapping(value = "/stream/{name_pattern}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<SiteStats> getSiteStats(@PathVariable("name_pattern") String namePatter) {
        return sitesService.getSitesStats(namePatter);
    }

}
