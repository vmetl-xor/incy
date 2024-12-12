package com.vmetl.api.service;

import com.vmetl.incy.messaging.Message;
import com.vmetl.incy.messaging.MessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class JobService {
    private final MessagesService<String, String> messagesService;

    @Autowired
    public JobService(MessagesService<String, String> messagesService) {
        this.messagesService = messagesService;
    }

    public Message<String, String> sendMessage(String message) {
        String id = UUID.randomUUID().toString();
        Message<String, String> newMessage = Message.of(id, Map.of("task", message));
        messagesService.sendMessage(newMessage);

        return newMessage;
    }

    public void stopAllProcessors() {
        messagesService.sendShutdownMessage();
    }

}
