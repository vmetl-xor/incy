package com.vmetl.incy;

import com.vmetl.incy.messaging.MessageConsumer;
import com.vmetl.incy.messaging.RedisTaskProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@ComponentScan
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
    public RedisTaskProcessor streamConsumer(String consumerName, MessageConsumer messageConsumer) {
        return new RedisTaskProcessor(consumerName, messageConsumer);
    }
}
