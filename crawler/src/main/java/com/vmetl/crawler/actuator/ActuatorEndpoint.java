package com.vmetl.crawler.actuator;

import com.vmetl.incy.CacheAwareDbService;
import com.vmetl.incy.DbMetrics;
import com.vmetl.incy.SiteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.util.Map;

@Endpoint(id = "dbStats")
@Component
public class ActuatorEndpoint {

    @Autowired
    private DbMetrics db;

    @ReadOperation
    public Map<String, String> getStats() {

        return Map.of("totalWords", db.wordsCount() + "", "totalSites", db.sitesCount() + "");
    }
}
