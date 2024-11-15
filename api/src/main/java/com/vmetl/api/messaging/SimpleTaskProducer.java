package com.vmetl.api.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SimpleTaskProducer {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String STREAM_KEY = "mystream";

    public void produceMessages() {
        for (int i = 1; i <= 20; i++) {
            Map<String, String> message = new HashMap<>();
            message.put("message", "task" + i);

            // Create a record
            RecordId recordId = redisTemplate.opsForStream().add(
                    StreamRecords.mapBacked(message).withStreamKey(STREAM_KEY)
            );

            System.out.println("Produced message with ID: " + recordId.getValue());
        }
    }


}
