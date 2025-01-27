package com.vmetl.incy.messaging;

import java.util.HashMap;
import java.util.Map;

public class Message {
    public static final String URL = "URL";
    public static final String DEPTH = "DEPTH";
    private String id;
    private Map<String, String> payload;

    public Message(MessageBuilder builder) {
        this.id = builder.id;
        this.payload = builder.payload;
    }

    public Message(String id, Map<String, String> payload) {
        this.id = id;
        this.payload = payload;
    }

    public Message() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPayload(Map<String, String> payload) {
        this.payload = payload;
    }

    public Map<String, String> getPayload() {
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
        private final Map<String, String> payload = new HashMap<>();

        public MessageBuilder setId(String id) {
            this.id = id;
            return this;
        }

        public MessageBuilder addUrl(String value) {
            payload.put(URL, value);
            return this;
        }

        public MessageBuilder setPayload(Map<String, String> payload) {
            this.payload.clear();
            this.payload.putAll(payload);

            return this;
        }

        public MessageBuilder addDepth(Object value) {
            payload.put(DEPTH, value + "");
            return this;
        }

        public Message build() {
            return new Message(this);
        }
    }


}
