package com.vmetl.parser;

import com.vmetl.crawler.fetch.UrlContent;
import com.vmetl.crawler.fetch.JsoupUrlFetch;
import org.junit.jupiter.api.Test;

import java.util.Optional;

class HtmlParserTest {

    @Test
    void parse() {
        String url = "https://donate.wikimedia.org";
        Optional<UrlContent> urlContent = new JsoupUrlFetch().fetchUrl(url);
        HtmlParser.parse(urlContent.orElseThrow());
    }
}