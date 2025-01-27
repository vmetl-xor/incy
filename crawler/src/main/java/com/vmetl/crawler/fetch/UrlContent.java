package com.vmetl.crawler.fetch;

import org.jsoup.nodes.Document;

public record UrlContent (String url, Document document) {}
