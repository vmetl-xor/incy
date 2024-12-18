package com.vmetl.crawler.cache;

import com.vmetl.incy.cache.SiteNameCache;
import org.springframework.stereotype.Component;

@Component
public class SitesCache implements SiteNameCache {


    @Override
    public String getIdByName(String siteName) {

        return "";
    }
}
