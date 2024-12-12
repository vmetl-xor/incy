package com.vmetl.crawler.messaging;

import com.vmetl.incy.messaging.Message;
import com.vmetl.incy.messaging.MessageConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SimpleMessageConsumer implements MessageConsumer<Object, Object> {
    Logger log = LoggerFactory.getLogger(SimpleMessageConsumer.class);

    @Override
    public void consume(Message<Object, Object> message) {
        log.info("Consumer: {}", message);
        try {
            Thread.sleep(2000); // Simulate processing time
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
