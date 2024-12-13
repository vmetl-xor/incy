package com.vmetl.incy.messaging;

import java.util.HashMap;
import java.util.Map;

public class Message {
    public static final String SITE = "SITE";
    public static final String DEPTH = "DEPTH";
    public static final String GLOBAL_DEPTH = "GLOBAL_DEPTH";
    private final String id;
    private final Map<String, Object> payload;

    private Message(MessageBuilder builder) {
        this.id = builder.id;
        this.payload = builder.payload;
        payload.put(GLOBAL_DEPTH, 2);
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return id + ": " + payload;
    }


    public static class MessageBuilder {
        private String id;
        private final Map<String, Object> payload = new HashMap<>();

        public MessageBuilder setId(String id) {
            this.id = id;
            return this;
        }

        public MessageBuilder addSite(Object value) {
            payload.put(SITE, value);
            return this;
        }

        public MessageBuilder setPayload(Map<String, Object> payload) {
            this.payload.clear();
            this.payload.putAll(payload);

            return this;
        }

        public MessageBuilder addDepth(Object value) {
            payload.put(DEPTH, value);
            return this;
        }

        public Message build() {
            return new Message(this);
        }
    }


}
