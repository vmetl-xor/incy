package com.vmetl.incy.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Repository
public class WordStatsRepositoryImpl implements WordStatsRepository {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public WordStatsRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public boolean updateWordsStats(Map<String, Integer> dictionary) {



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
