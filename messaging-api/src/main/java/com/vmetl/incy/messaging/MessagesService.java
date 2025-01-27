package com.vmetl.incy.messaging;

import reactor.core.publisher.Mono;

import java.util.function.Function;

public interface MessagesService {

    default void sendMessage(Message message, Function<Object, Object> postAction) {}
    void sendShutdownMessage();

    default void sendMessageReactive(Mono<Message> message, Function<Object, Object> postAction) {}
}
