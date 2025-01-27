package com.vmetl.incy.messaging;

import reactor.core.publisher.Mono;

public interface MessageConsumer {
    void consume(Message message);

    Mono<Void> consumeAsync(Message message);
}
