package com.vmetl.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(scanBasePackages = {"com.vmetl.incy", "com.vmetl.api"})
public class ApiApplication {

    private static final Logger LOG = LoggerFactory
            .getLogger(ApiApplication.class);

    public static void main(String[] args) {
        LOG.info("STARTING THE APPLICATION");
        SpringApplication.run(ApiApplication.class, args);
        LOG.info("APPLICATION FINISHED");
    }

}
