package com.vmetl.incy.messaging;

public interface MessageConsumer<K, V> {
    void consume(Message<K, V> message);
}
