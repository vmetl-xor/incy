package com.vmetl.crawler.actuator;

import com.vmetl.incy.metrics.db.DbMetrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class SomeHealthIndicator implements HealthIndicator {

    private DbMetrics dbMetrics;

    @Autowired
    public SomeHealthIndicator(DbMetrics dbMetrics) {
        this.dbMetrics = dbMetrics;
    }

    @Override
    public Health health() {
        return Health.up().withDetail("words",dbMetrics.wordsCount().orElse(0)).build();
    }
}
