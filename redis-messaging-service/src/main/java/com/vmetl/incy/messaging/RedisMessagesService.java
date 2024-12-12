package com.vmetl.incy.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
public class RedisMessagesService implements MessagesService<String, String> {
    Logger log = LoggerFactory.getLogger(RedisMessagesService.class);

    public static final String STREAM_KEY = "mystream";

    @Autowired
    private ChannelTopic topic;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void sendMessage(Message<String, String> message) {
        RecordId recordId = redisTemplate.opsForStream().add(
                StreamRecords.mapBacked(message.getPayload()).withStreamKey(STREAM_KEY)
        );

        log.info("Produced message with ID: {}", recordId.getValue());
    }

    @Override
    public void sendShutdownMessage() {
        redisTemplate.convertAndSend(topic.getTopic(), "SHUTDOWN");
    }
}
