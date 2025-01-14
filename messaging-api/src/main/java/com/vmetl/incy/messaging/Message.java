package com.vmetl.incy.messaging;

import java.util.HashMap;
import java.util.Map;

public class Message {
    public static final String URL = "URL";
    public static final String DEPTH = "DEPTH";
    private final String id;
    private final Map<String, Object> payload;

    private Message(MessageBuilder builder) {
        this.id = builder.id;
        this.payload = builder.payload;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", payload=" + payload +
                '}';
    }


    public static class MessageBuilder {
        private String id;
        private final Map<String, Object> payload = new HashMap<>();

        public MessageBuilder setId(String id) {
            this.id = id;
            return this;
        }

        public MessageBuilder addUrl(Object value) {
            payload.put(URL, value);
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
