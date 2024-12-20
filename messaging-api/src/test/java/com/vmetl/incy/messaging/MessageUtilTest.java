package com.vmetl.incy.messaging;

import org.junit.jupiter.api.Test;

class MessageUtilTest {

    @Test
    void getUrl() {
        Message.MessageBuilder builder = new Message.MessageBuilder();
        builder.addUrl("http://example.com/url");
        MessageUtil.getDomain(builder.build());
    }

    @Test
    void getDomain() {
    }

    @Test
    void getCurrentRefDepth() {
    }

    @Test
    void getGlobalRefDepth() {
    }

    @Test
    void addRefDepth() {
    }

    @Test
    void addGlobalDepth() {
    }

    @Test
    void addSite() {
    }
}