package com.vmetl.crawler.messaging;

import com.vmetl.incy.messaging.Message;
import com.vmetl.incy.messaging.MessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SimpleTaskProducer {

    @Autowired
    private MessagesService messagesService;

    public void produceMessages() {
        for (int i = 1; i <= 20; i++) {

            Map<String, String> messagePayload = new HashMap<>();
            messagePayload.put("message", "task" + i);

            messagesService.sendMessage(new Message<>("task" + i, messagePayload));
        }
    }


}
