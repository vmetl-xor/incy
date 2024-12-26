package com.vmetl.incy;

import com.vmetl.incy.cache.SiteNameCache;
import com.vmetl.incy.db.SiteRepository;
import com.vmetl.incy.db.reactive.SitesReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class CacheAwareDbService implements SiteDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SiteRepository siteRepository;
    private final SiteNameCache sitesCache;
    private final SitesReactiveRepository sitesReactiveRepository;

    @Autowired
    public CacheAwareDbService(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                               SiteRepository siteRepository, SiteNameCache sitesCache, SitesReactiveRepository sitesReactiveRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.siteRepository = siteRepository;
        this.sitesCache = sitesCache;
        this.sitesReactiveRepository = sitesReactiveRepository;
    }

    @Override
    public void addSite(String name) {
        siteRepository.addSite(name);
        // todo add to cache
    }

    @Override
    public Optional<Integer> getSiteIdByName(String siteName) {
        return sitesCache.getIdByName(siteName).
                or(() -> siteRepository.getSiteIdByName(siteName).
                        map(siteId -> {
                            sitesCache.addSiteName(siteName, siteId);
                            return siteId;
                        }));
    }

    @Override
    @Transactional
    public void updateSiteStatistics(int siteId, Map<String, Integer> statistics) {
        Set<String> words = statistics.keySet();

        final Map<String, Integer> wordToIdMap = new HashMap<>();
        fetchWords(words, wordToIdMap);

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
            fetchWords(missingWords, wordToIdMap);
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

    @Override
    public Flux<SiteStats> getSiteStatsStream() {
        return sitesReactiveRepository.getSiteStats();
    }

    private void fetchWords(Collection<String> words, Map<String, Integer> wordToIdMap) {
        Map<String, Object> params = new HashMap<>();
        params.put("words", words);

        String fetchSql = "SELECT value, id FROM words WHERE value IN (:words)";

        namedParameterJdbcTemplate.query(fetchSql, params, rs -> {
            wordToIdMap.put(rs.getString("value"), rs.getInt("id"));
        });
    }

}
