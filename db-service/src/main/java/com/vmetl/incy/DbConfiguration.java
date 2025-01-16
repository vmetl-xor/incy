package com.vmetl.incy;

import io.r2dbc.proxy.ProxyConnectionFactory;
import io.r2dbc.proxy.support.QueryExecutionInfoFormatter;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

import javax.sql.DataSource;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@Configuration
@EnableJdbcRepositories(basePackages = "com.vmetl.incy.db")
public class DbConfiguration {


    private static final Logger logger = LoggerFactory.getLogger(DbConfiguration.class);

    private final QueryExecutionInfoFormatter formatter = QueryExecutionInfoFormatter.showAll();

    @Value("${spring.datasource.url}") String dataSourceUrl;
    @Value("${spring.datasource.username}") String username;
    @Value("${spring.datasource.password}") String password;
    @Value("${spring.datasource.driver-class-name}") String driverClassName;

    @Bean
    public ConnectionFactory connectionFactory() {
        ConnectionFactory original = ConnectionFactories.get(
                builder()
                        .option(DRIVER, "postgresql")
                        .option(HOST, "localhost")
                        .option(USER, "postgres")
                        .option(PASSWORD, "postgres")
                        .option(PORT, 5432)
                        .option(DATABASE, "incy")
                        .build());

//        logger.warn("Using PostgreSQL connection factory: {}", original);

        return ProxyConnectionFactory.builder(original)
                .onAfterQuery(queryInfo -> {  // listener
//                    logger.info(formatter.format(queryInfo));
                })
                .build();
    }

    @Bean
    public DataSource dataSource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(driverClassName);
        dataSourceBuilder.url(dataSourceUrl);
        dataSourceBuilder.username(username);
        dataSourceBuilder.password(password);

        return dataSourceBuilder.build();
    }


}
