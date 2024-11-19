package com.vmetl.incy.messaging;

import java.util.Map;

public class Message<K, V> {
    private String id;
    private Map<K, V> payload;

    public Message(String id, Map<K, V> payload) {
        this.payload = payload;
    }

    public Map<K, V> getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return id + ": " + payload;
    }
}
