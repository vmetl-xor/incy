package com.vmetl.crawler.fetch;

import java.util.Optional;

public interface UrlFetch {
    Optional<UrlContent> fetchUrl(String url);
}
