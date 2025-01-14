package com.vmetl.incy;

import com.vmetl.incy.messaging.MessageConsumer;
import com.vmetl.incy.task.TaskProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class ModuleConfig {
    @Autowired
    private ProcessorsRunningState runningState;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Bean
    @Scope("prototype")
    @DependsOn("ConsumerGroupInitializer")
    public TaskProcessor streamConsumer(String consumerName, MessageConsumer messageConsumer) {
        return new RedisTaskProcessor(redisTemplate, runningState, consumerName, messageConsumer);
    }
}
