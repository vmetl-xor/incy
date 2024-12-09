package com.vmetl.crawler.messaging;

import com.vmetl.incy.messaging.Message;
import com.vmetl.incy.messaging.MessageConsumer;
import org.springframework.stereotype.Component;

@Component
public class SimpleMessageConsumer implements MessageConsumer {
    @Override
    public void consume(Message message) {
        System.out.printf("Consumer: %s%n", message);
        System.out.println("\n");
        try {
            Thread.sleep(2000); // Simulate processing time
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
