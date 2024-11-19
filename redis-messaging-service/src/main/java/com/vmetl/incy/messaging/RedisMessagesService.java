package com.vmetl.incy.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisMessagesService implements MessagesService<String, String> {

    public static final String STREAM_KEY = "mystream";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void sendMessage(Message<String, String> message) {
        RecordId recordId = redisTemplate.opsForStream().add(
                StreamRecords.mapBacked(message.getPayload()).withStreamKey(STREAM_KEY)
        );

        System.out.println("Produced message with ID: " + recordId.getValue());
    }


}
