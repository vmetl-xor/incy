package com.vmetl.incy;


import com.vmetl.incy.messaging.MessageConsumer;
import com.vmetl.incy.pubsub.RedisMessageSubscriber;
import com.vmetl.incy.task.TaskProcessor;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import static com.vmetl.incy.messaging.RedisMessagesService.STREAM_KEY;

@Configuration
public class ModuleConfig {
    Logger log = LoggerFactory.getLogger(ModuleConfig.class);

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
    public TaskProcessor streamConsumer(String consumerName, MessageConsumer<Object, Object> messageConsumer) {
        return new RedisTaskProcessor(consumerName, messageConsumer);
    }

    @Bean
    @ConditionalOnProperty(prefix = "message", name = "receiver")
    MessageListenerAdapter messageListener(TaskProcessorsManager taskProcessorsManager) {
        log.info("Redis PubSub listener initialized");
        return new MessageListenerAdapter(new RedisMessageSubscriber(taskProcessorsManager));
    }

    @Bean
    @ConditionalOnProperty(prefix = "message", name = "receiver")
    RedisMessageListenerContainer redisContainer(TaskProcessorsManager taskProcessorsManager) {
        RedisMessageListenerContainer container
                = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(messageListener(taskProcessorsManager), topic());

        return container;
    }

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    ChannelTopic topic() {
        return new ChannelTopic("ProcessorsStatusQueue");
    }

}
