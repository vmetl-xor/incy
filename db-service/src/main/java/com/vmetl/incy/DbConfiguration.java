package com.vmetl.incy;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@Configuration
@EnableJdbcRepositories(basePackages = "com.vmetl.incy.db")
public class DbConfiguration {
}
