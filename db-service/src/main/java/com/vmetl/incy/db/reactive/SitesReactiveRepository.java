package com.vmetl.incy.db.reactive;

import com.vmetl.incy.SiteStats;
import com.vmetl.incy.WordStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
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

    private final R2dbcEntityTemplate template;

    @Autowired
    public SitesReactiveRepository(DatabaseClient client, R2dbcEntityTemplate template) {
        this.client = client;
        this.template = template;
    }

    public Flux<SiteStats> getSiteStats() {

//        List<Site> allSiteNames = new ArrayList<>();
        return client.sql("SELECT name as site_name, id as site_id FROM SITES").
                map(row -> new SiteStats(row.get("site_name", String.class), row.get("id", Long.class), new ArrayList<>())).
                all().
                map(site -> {
                    Map<String, ?> params = Map.of("site_id", site.id());
                    client.sql(SITE_STATS_QUERY).bindValues(params).
                            map((row, rowMetadata) ->
                                    site.wordStats().
                                            add(new WordStats(row.get("word_value", String.class), row.get("word_count", Integer.class))));
                    return site;
                });

    }
}
