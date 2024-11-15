package com.vmetl.api.data;

import com.vmetl.api.messaging.TaskProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class DbConfig {
//    @Bean
//    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
//        RedisTemplate<String, String> template = new RedisTemplate<>();
//        template.setConnectionFactory(connectionFactory);
//
//        // Add some specific configuration here. Key serializers, etc.
//        return template;
//    }

    @Bean
    @Scope("prototype")
    public TaskProcessor streamConsumer(String consumerName) {
        return new TaskProcessor(consumerName);
    }
}
