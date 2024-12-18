package com.vmetl.incy;

import com.vmetl.incy.db.SiteRepository;
import com.vmetl.incy.db.WordStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DbService {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private final SiteRepository siteRepository;

    public DbService(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate, SiteRepository siteRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.siteRepository = siteRepository;
    }

    public void addSite(String name) {
        siteRepository.addSite(name);
    }

    public Integer getSiteIdByName(String siteName) {
        return siteRepository.getSiteIdByName(siteName).
                orElseThrow(() -> new IllegalStateException("No site found with name: " + siteName));
    }

    @Transactional
    public void updateSiteStatistics(int siteId, Map<String, Integer> statistics) {
        Set<String> words = statistics.keySet();

        Map<String, Object> params = new HashMap<>();
        params.put("words", words);

        String fetchSql = "SELECT value, id FROM words WHERE value IN (:words)";

        final Map<String, Integer> wordToIdMap = new HashMap<>();
        fetchWords(fetchSql, params, wordToIdMap);

        List<String> missingWords =
                words.stream().filter(s -> !wordToIdMap.containsKey(s)).collect(Collectors.toList());

        if (!missingWords.isEmpty()) {
            String insertWordSql = "INSERT INTO words (value) VALUES (?) ON CONFLICT DO NOTHING";
            jdbcTemplate.batchUpdate(insertWordSql,
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setString(1, missingWords.get(i));
                        }

                        @Override
                        public int getBatchSize() {
                            return missingWords.size();
                        }
                    }
            );

            // Re-fetch the newly inserted words to get their IDs
            Map<String, Object> refetchParams = new HashMap<>();
            refetchParams.put("words", missingWords);
            fetchWords(fetchSql, refetchParams, wordToIdMap);
        }

        String upsertSql =
                "INSERT INTO site_statistics (site_id, word_id, word_occurrences) VALUES (?, ?, ?) " +
                        "ON CONFLICT (site_id, word_id) DO UPDATE SET word_occurrences = site_statistics.word_occurrences + EXCLUDED.word_occurrences";

        List<WordStats> wordStats = statistics.entrySet().stream().
                map(entry -> new WordStats(entry.getKey(), entry.getValue())).
                toList();

        jdbcTemplate.batchUpdate(upsertSql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        String word = wordStats.get(i).value();
                        int count = wordStats.get(i).count();

                        Integer wordId = wordToIdMap.get(word);

                        ps.setInt(1, siteId);
                        ps.setInt(2, wordId);
                        ps.setInt(3, count);
                    }

                    @Override
                    public int getBatchSize() {
                        return wordStats.size();
                    }
                }
        );

    }

    private void fetchWords(String fetchSql, Map<String, Object> params, Map<String, Integer> wordToIdMap) {
        namedParameterJdbcTemplate.query(fetchSql, params, rs -> {
            wordToIdMap.put(rs.getString("value"), rs.getInt("id"));
        });
    }

}
