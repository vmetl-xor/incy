package com.vmetl.incy.db.reactive;

import com.vmetl.incy.SiteStats;
import com.vmetl.incy.WordStats;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class SitesReactiveRepository {

    public static final String SITE_STATS_QUERY = "select w.value as word_value, sum(st.word_occurrences) as word_count " +
            "from site_statistics st " +
            "         inner join sites s on s.id = st.site_id " +
            "         inner join words w on w.id = st.word_id " +
            "where s.id = :site_id " +
            "group by w.value " +
            "order by sum(word_occurrences) desc";

    private final DatabaseClient client;


    @Autowired
    public SitesReactiveRepository(ConnectionFactory connectionFactory) {
        this.client = DatabaseClient.create(connectionFactory);
    }

    public Flux<SiteStats> getSiteStatsv1() {

        Flux<SiteStats> allSites = client.sql("SELECT name as site_name, id as site_id FROM SITES").
                map(row -> new SiteStats(row.get("site_name", String.class), row.get("site_id", Long.class), new ArrayList<>())).
                all();

        return allSites.
                flatMap(site -> {
                    Map<String, ?> params = Map.of("site_id", site.id());
                    return client.sql(SITE_STATS_QUERY).bindValues(params).
                            map((row, rowMetadata) -> {
                                site.wordStats().
                                        add(new WordStats(row.get("word_value", String.class), row.get("word_count", Integer.class)));
                                return site;
                            }).
                            all().
                            collectList().map(wordStatsList -> {
                                List<WordStats> wStats = wordStatsList.isEmpty() ? Collections.emptyList() : wordStatsList.getFirst().wordStats();
                                site.wordStats().addAll(wStats);
                                return site;              // Return *one* site object per site
                            });
                }, 5);
    }

    public Flux<SiteStats> getSiteStats() {

        Flux<SiteStats> allSites = client.sql("SELECT name as site_name, id as site_id FROM SITES").
                map(row -> new SiteStats(row.get("site_name", String.class), row.get("site_id", Long.class), new ArrayList<>())).
                all();

        return allSites.
                flatMap(site ->
                        client.sql(SITE_STATS_QUERY).
                                bindValues(Map.of("site_id", site.id())).
                                map((row, rowMetadata) ->
                                        new WordStats(row.get("word_value", String.class), row.get("word_count", Integer.class))).
                                all().
                                doOnNext(wordStats -> site.wordStats().add(wordStats)).
                                then(Mono.just(site)), 5);

    }

}
