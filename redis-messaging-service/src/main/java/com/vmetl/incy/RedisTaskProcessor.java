package com.vmetl.incy;

import com.vmetl.incy.messaging.Message;
import com.vmetl.incy.messaging.MessageConsumer;
import com.vmetl.incy.task.TaskProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.UUID;

public class RedisTaskProcessor implements TaskProcessor {

    Logger log = LoggerFactory.getLogger(RedisTaskProcessor.class);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ProcessorsRunningState runningState;

    private static final String STREAM_KEY = "mystream";
    private static final String GROUP_NAME = "mygroup";
    private String consumerName;
    private final MessageConsumer<Object, Object> consumer;

    public RedisTaskProcessor(String consumerName, MessageConsumer<Object, Object> consumer) {
        this.consumerName = consumerName;
        this.consumer = consumer;
    }

    @Override
    public void run() {

        log.info("Starting task processor {}", consumerName);
        while (runningState.isRunning()) {
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

                        log.info("{} processing message ID: {}, body: {}", consumerName, messageId, body);

                        // Process the message
                        processMessage(body);

                        // Acknowledge the message
                        redisTemplate.opsForStream().acknowledge(GROUP_NAME, message);
                    }
                } else {
                    // No messages, consumer will continue blocking
                    log.info("{} found no messages. Waiting...", consumerName);
                }
            } catch (Exception e) {
                log.error("An exception occurred during processing", e);
            }
        }

        log.info("Stopping task processor {}", consumerName);
    }

    private void processMessage(Map<Object, Object> message) {
        // Simulate message processing
        consumer.consume(Message.of(UUID.randomUUID().toString(), message));
    }
}
