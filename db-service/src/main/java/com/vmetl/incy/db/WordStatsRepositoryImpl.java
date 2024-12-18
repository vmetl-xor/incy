package com.vmetl.incy.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class SiteRepositoryImpl implements SiteRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public SiteRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean addSite(String name) {
    }

    @Override
    public boolean updateWordsStats(List<String> words) {
        jdbcTemplate.batchUpdate("INSERT INTO site_statistics (site_id, word_occurrences, word_id) " +
                        "VALUES (?, ?, ?)",
                List.of(),
                100,
                (PreparedStatement ps, String product) -> {
                    ps.setString(1, "product.getTitle()");
                    ps.setTimestamp(2, Timestamp.valueOf("product.getCreatedTs()"));
                    ps.setBigDecimal(3, BigDecimal.valueOf(10));
                });
        return false;
    }
}
