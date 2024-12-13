package com.vmetl.incy;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import static com.vmetl.incy.messaging.RedisMessagesService.STREAM_KEY;

@Component("ConsumerGroupInitializer")
public class ConsumerGroupInitializer implements InitializingBean {

    private static final String GROUP_NAME = "mygroup";
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private void initConsumerGroup() {
        try {
            // Create the consumer group if it doesn't exist
            redisTemplate.opsForStream().createGroup(STREAM_KEY, ReadOffset.latest(), GROUP_NAME);
            System.out.println("Consumer group '" + GROUP_NAME + "' created.");
        } catch (Exception e) {
            // Group already exists
            System.out.println("Consumer group '" + GROUP_NAME + "' already exists.");
        }
    }

    @Override
    public void afterPropertiesSet() {
        initConsumerGroup();
    }
}
