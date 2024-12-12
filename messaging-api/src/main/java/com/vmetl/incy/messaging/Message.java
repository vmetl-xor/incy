package com.vmetl.incy.messaging;

import java.util.Map;

public class Message<K, V> {
    private final String id;
    private final Map<K, V> payload;

    private Message(String id, Map<K, V> payload) {
        this.id = id;
        this.payload = payload;
    }

    public Map<K, V> getPayload() {
        return payload;
    }

    public static <K, V> Message<K, V> of(String id, Map<K, V> payload) {
        return new Message<>(id, payload);
    }

    @Override
    public String toString() {
        return id + ": " + payload;
    }
}
