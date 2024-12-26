package com.vmetl.incy;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@Configuration
@EnableJdbcRepositories(basePackages = "com.vmetl.incy.db")
public class DbConfiguration {

//    @Bean
//    public ConnectionFactory connectionFactory() {
//        return ConnectionFactories.get(
//                ConnectionFactoryOptions.builder()
//                        .option(DRIVER, "postgresql")
//                        .option(HOST, "localhost")
//                        .option(USER, "user")
//                        .option(PASSWORD, "secret")
//                        .option(DATABASE, "studentdb")
//                        .build());
//    }

}
