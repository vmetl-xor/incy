package com.vmetl.incy.pubsub;

import com.vmetl.incy.TaskProcessorsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
public class RedisMessageSubscriber implements MessageListener {

    Logger log = LoggerFactory.getLogger(RedisMessageSubscriber.class);

    private final TaskProcessorsManager taskProcessorsManager;

    @Autowired
    public RedisMessageSubscriber(TaskProcessorsManager taskProcessorsManager) {
        this.taskProcessorsManager = taskProcessorsManager;
    }

    public void onMessage(Message message, byte[] pattern) {
        if (message.toString().equalsIgnoreCase("\"SHUTDOWN\"")) {
            taskProcessorsManager.stopAllProcessors();
        }

        log.info("Message received: {}", message);
    }
}