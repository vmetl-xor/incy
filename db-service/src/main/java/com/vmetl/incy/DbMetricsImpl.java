package com.vmetl.incy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.OptionalInt;

@Component
public class DbMetricsImpl implements DbMetrics {


    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DbMetricsImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public OptionalInt wordsCount() {
        Integer words = jdbcTemplate.queryForObject("select count(*) from words", Integer.class);
        return words == null ? OptionalInt.empty() : OptionalInt.of(words);
    }

    @Override
    public OptionalInt sitesCount() {
        Integer sites = jdbcTemplate.queryForObject("select count(*) from sites", Integer.class);
        return sites == null ? OptionalInt.empty() : OptionalInt.of(sites);
    }
}
