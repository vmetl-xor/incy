package com.vmetl.incy.messaging;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class RedisMessagesService implements MessagesService {

    private static final String STREAM_KEY = "mystream";
    private static final String GROUP_NAME = "mygroup";
    private static final int NUMBER_OF_CONSUMERS = 10;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ObjectProvider<RedisTaskProcessor> taskProcessorProvider;

    @Autowired
    private MessageConsumer messageConsumer;

    ExecutorService executors = Executors.newFixedThreadPool(NUMBER_OF_CONSUMERS);

    public RedisMessagesService() {
        createConsumerGroup();
    }


    @PostConstruct
    public void start() {

        for (int i = 1; i <= NUMBER_OF_CONSUMERS; i++) {
            String consumerName = "consumer-" + i;
            RedisTaskProcessor consumer = taskProcessorProvider.getObject(consumerName, messageConsumer);

            executors.submit(consumer);
        }
    }

    @Override
    public void sendMessage(Message message) {
        RecordId recordId = redisTemplate.opsForStream().add(
                StreamRecords.mapBacked(message.getPayload()).withStreamKey(STREAM_KEY)
        );

        System.out.println("Produced message with ID: " + recordId.getValue());
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

}
