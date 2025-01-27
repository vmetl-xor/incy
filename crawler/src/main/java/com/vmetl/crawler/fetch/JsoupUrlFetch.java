package com.vmetl.crawler.fetch;

import com.vmetl.parser.HtmlParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JsoupUrlFetch implements UrlFetch {
    private static final Logger log = LoggerFactory.getLogger(HtmlParser.class);

    @Override
    public Optional<UrlContent> fetchUrl(String url) {
        Document doc;
        try {
            doc = Jsoup.connect(url).get();
        } catch (Exception e) {
            log.error("Error fetching URL {}", url);
            log.error("Error message {}", e.getMessage());

            return Optional.empty();
        }

        return Optional.of(new UrlContent(url, doc));
    }
}
