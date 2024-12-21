package com.vmetl.parser;

import org.junit.jupiter.api.Test;

class HtmlParserTest {

    @Test
    void parse() {
        HtmlParser.parse("https://donate.wikimedia.org");
    }
}