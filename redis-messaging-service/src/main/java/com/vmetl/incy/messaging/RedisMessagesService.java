package com.vmetl.incy.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class RedisMessagesService implements MessagesService {
    Logger log = LoggerFactory.getLogger(RedisMessagesService.class);

    public static final String STREAM_KEY = "stream_1";

    @Autowired
    private ChannelTopic topic;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void sendMessage(Message message, Function<Object, Object> postAction) {
        RecordId recordId = redisTemplate.opsForStream().add(
                StreamRecords.mapBacked(message.getPayload()).withStreamKey(STREAM_KEY)
        );
        String site = MessageUtil.getUrl(message);
        postAction.apply(site);

        log.info("Produced message with ID: {}, site: {}", recordId.getValue(), site);
    }

    @Override
    public void sendShutdownMessage() {
        redisTemplate.convertAndSend(topic.getTopic(), "SHUTDOWN");
    }
}
