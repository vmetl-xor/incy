package com.vmetl.api.service;

import com.vmetl.api.db.SiteRepository;
import org.springframework.stereotype.Component;

@Component
public class DbService {

    private final SiteRepository siteRepository;

    public DbService(SiteRepository siteRepository) {
        this.siteRepository = siteRepository;
    }

    public void addSite(String name) {
        siteRepository.addSite(name);
    }

}
