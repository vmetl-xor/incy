package com.vmetl.incy.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.datasource")
public record DataSourceConfiguration(String driverClassName,
                                      String host,
                                      int port,
                                      String database,
                                      String username,
                                      String password,
                                      String driver
) {}

