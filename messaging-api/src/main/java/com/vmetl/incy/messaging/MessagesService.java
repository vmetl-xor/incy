package com.vmetl.incy.messaging;

public interface MessagesService <K, V> {


    void sendMessage(Message<K, V> message);

}
