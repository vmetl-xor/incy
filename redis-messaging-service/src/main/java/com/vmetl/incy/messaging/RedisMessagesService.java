package com.vmetl.incy.messaging;

import com.vmetl.incy.cache.VisitedRefsCache;
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


    public RedisMessagesService(RedisTemplate<String, Object> redisTemplate, ChannelTopic topic) {
        this.redisTemplate = redisTemplate;
        this.topic = topic;
    }

    @Override
    public void sendMessage(Message message, Function<Object, Object> postAction) {
        String url = MessageUtil.getUrl(message);

        RecordId recordId = redisTemplate.opsForStream().add(
                StreamRecords.mapBacked(message.getPayload()).withStreamKey(STREAM_KEY)
        );
        postAction.apply(url);

        log.debug("Produced message with ID: {}, url: {}", recordId.getValue(), url);
    }

    @Override
    public void sendShutdownMessage() {
        redisTemplate.convertAndSend(topic.getTopic(), "SHUTDOWN");
    }
}
