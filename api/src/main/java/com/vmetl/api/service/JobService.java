package com.vmetl.api.service;

import com.vmetl.api.rest.Job;
import com.vmetl.incy.messaging.Message;
import com.vmetl.incy.messaging.MessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class JobService {
    private final MessagesService messagesService;

    @Autowired
    public JobService(MessagesService messagesService) {
        this.messagesService = messagesService;
    }

    public Message sendMessage(Job job) {
        String id = UUID.randomUUID().toString();

        Message newMessage =
                new Message.MessageBuilder().
                        setId(id).
                        addDepth(job.getDepth()).
                        addUrl(job.getUrl()).
                        build();

        messagesService.sendMessage(newMessage, o -> null);

        return newMessage;
    }

    public void stopAllProcessors() {
        messagesService.sendShutdownMessage();
    }

}
