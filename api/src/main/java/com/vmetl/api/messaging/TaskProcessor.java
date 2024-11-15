package com.vmetl.api.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamReadOptions;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.connection.stream.Consumer;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class TaskProcessor implements Runnable {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String STREAM_KEY = "mystream";
    private static final String GROUP_NAME = "mygroup";
    private String consumerName;

    public TaskProcessor(String consumerName) {
        this.consumerName = consumerName;
    }

    @Override
    public void run() {
        // Create consumer group if not exists
        createConsumerGroup();

        while (true) {
            try {


                Consumer consumer = Consumer.from(GROUP_NAME, consumerName);
                StreamReadOptions options = StreamReadOptions.empty().count(1).block(Duration.ofSeconds(2));
                StreamOffset<String> offset = StreamOffset.create(STREAM_KEY, ReadOffset.lastConsumed());

                List<MapRecord<String, Object, Object>> messages = redisTemplate.opsForStream()
                        .read(consumer,
                                options,
                                offset);


                if (messages != null && !messages.isEmpty()) {
                    for (MapRecord<String, Object, Object> message : messages) {
                        String messageId = message.getId().getValue();
                        Map<Object, Object> body = message.getValue();

                        System.out.println(consumerName + " processing message ID: " + messageId + ", body: " + body);

                        // Process the message
                        processMessage(body);

                        // Acknowledge the message
                        redisTemplate.opsForStream().acknowledge(GROUP_NAME, message);
                    }
                } else {
                    // No messages, consumer will continue blocking
                    System.out.println(consumerName + " found no messages. Waiting...");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void createConsumerGroup() {
        try {
            // Create the consumer group if it doesn't exist
            redisTemplate.opsForStream().createGroup(STREAM_KEY, ReadOffset.latest(), GROUP_NAME);
            System.out.println("Consumer group '" + GROUP_NAME + "' created.");
        } catch (Exception e) {
            // Group already exists
            System.out.println("Consumer group '" + GROUP_NAME + "' already exists.");
        }
    }

    private void processMessage(Map<Object, Object> message) {
        // Simulate message processing
        try {
            Thread.sleep(2000); // Simulate processing time
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
