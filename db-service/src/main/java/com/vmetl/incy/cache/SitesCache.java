package com.vmetl.incy.cache;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SitesCache implements SiteNameCache {


    @Override
    public Optional<Integer> getIdByName(String siteName) {

        return Optional.empty();
    }

    @Override
    public boolean addSiteName(String siteName, Integer siteId) {
        return false;
    }
}
