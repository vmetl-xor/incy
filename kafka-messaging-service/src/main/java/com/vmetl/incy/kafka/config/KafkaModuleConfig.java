package com.vmetl.incy.kafka.config;

import com.vmetl.incy.kafka.tasks.KafkaTaskProcessor;
import com.vmetl.incy.messaging.MessageConsumer;
import com.vmetl.incy.task.TaskProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import reactor.kafka.receiver.KafkaReceiver;

@Configuration
public class KafkaModuleConfig {

    @Autowired
    private KafkaReceiver<String, String> receiver;

    @Bean
    @Scope("prototype")
    @Primary
    public TaskProcessor streamConsumer(String consumerName, MessageConsumer messageConsumer) {
        return new KafkaTaskProcessor(receiver, consumerName, messageConsumer);
    }

}
