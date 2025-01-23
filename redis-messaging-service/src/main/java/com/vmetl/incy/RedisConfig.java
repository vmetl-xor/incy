package com.vmetl.incy;


import com.vmetl.incy.pubsub.RedisMessageSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class RedisConfig {
    Logger log = LoggerFactory.getLogger(RedisConfig.class);

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private String redisPort;


//    @Bean
//    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        RedisTemplate<String, String> template = new RedisTemplate<>();
//        template.setConnectionFactory(redisConnectionFactory);
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
//        template.afterPropertiesSet();
//
//        return template;
//    }

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory(new RedisStandaloneConfiguration(redisHost, Integer.parseInt(redisPort)));
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
    ChannelTopic topic() {
        return new ChannelTopic("ProcessorsStatusQueue");
    }

}
