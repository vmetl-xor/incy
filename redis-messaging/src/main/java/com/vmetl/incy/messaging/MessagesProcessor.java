package com.vmetl.incy.messaging;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.vmetl.incy.messaging.RedisMessagesService.STREAM_KEY;

@Component
public class MessagesProcessor {

    private static final String GROUP_NAME = "mygroup";
    private static final int NUMBER_OF_CONSUMERS = 10;

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public MessagesProcessor(MessageConsumer messageConsumer, ObjectProvider<RedisTaskProcessor> taskProcessorProvider,
                             RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;

        createConsumerGroup();

        for (int i = 1; i <= NUMBER_OF_CONSUMERS; i++) {
            String consumerName = "consumer-" + i;
            RedisTaskProcessor consumer = taskProcessorProvider.getObject(consumerName, messageConsumer);

            ExecutorService executors = Executors.newFixedThreadPool(NUMBER_OF_CONSUMERS);
            executors.submit(consumer);
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

}
