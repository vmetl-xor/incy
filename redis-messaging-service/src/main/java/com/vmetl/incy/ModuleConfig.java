package com.vmetl.incy;


import com.vmetl.incy.messaging.MessageConsumer;
import com.vmetl.incy.task.TaskProcessor;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.core.RedisTemplate;

import static com.vmetl.incy.messaging.RedisMessagesService.STREAM_KEY;

@Configuration
public class ModuleConfig {

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
    @DependsOn("ConsumerGroupInitializer")
    public TaskProcessor streamConsumer(String consumerName, MessageConsumer messageConsumer) {
        return new RedisTaskProcessor(consumerName, messageConsumer);
    }

}
