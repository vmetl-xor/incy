package com.vmetl.incy.config;

import io.r2dbc.proxy.ProxyConnectionFactory;
import io.r2dbc.proxy.support.QueryExecutionInfoFormatter;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

import javax.sql.DataSource;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@Configuration
@EnableJdbcRepositories(basePackages = "com.vmetl.incy.db")
@ConfigurationPropertiesScan(basePackages = "com.vmetl.incy")
public class DbConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(DbConfiguration.class);

    private final QueryExecutionInfoFormatter formatter = QueryExecutionInfoFormatter.showAll();

    @Bean
    public ConnectionFactory connectionFactory(DataSourceConfiguration config) {
        ConnectionFactory original = ConnectionFactories.get(
                builder()
                        .option(DRIVER, config.driver())
                        .option(HOST, config.host())
                        .option(USER, config.username())
                        .option(PASSWORD, config.password())
                        .option(PORT, config.port())
                        .option(DATABASE, config.database())
                        .build());

        logger.warn("Using PostgreSQL connection factory: {}", original);

        return ProxyConnectionFactory.builder(original)
                .onAfterQuery(queryInfo -> {  // listener
                    logger.debug(formatter.format(queryInfo));
                })
                .build();
    }

    @Bean
    public DataSource dataSource(DataSourceConfiguration config) {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(config.driverClassName());
        dataSourceBuilder.url(buildUrl(config));
        dataSourceBuilder.username(config.username());
        dataSourceBuilder.password(config.password());

        return dataSourceBuilder.build();
    }

    private static String buildUrl(DataSourceConfiguration config) {
        return "jdbc:" + config.driver() + "://" + config.host() + ":" + config.port() + "/" + config.database();
    }

}
