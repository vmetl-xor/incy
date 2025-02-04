package com.vmetl.incy.messaging;

import com.vmetl.incy.task.TaskProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class MessagesProcessor {

    Logger log = LoggerFactory.getLogger(MessagesProcessor.class);
    private static final int NUMBER_OF_CONSUMERS = 1;

    @Autowired
    public MessagesProcessor(MessageConsumer messageConsumer, ObjectProvider<TaskProcessor> taskProcessorProvider) {

        try (ExecutorService executors = Executors.newFixedThreadPool(NUMBER_OF_CONSUMERS)) {

            for (int i = 1; i <= NUMBER_OF_CONSUMERS; i++) {
                String consumerName = "consumer-" + i;
                TaskProcessor consumer = taskProcessorProvider.getObject(consumerName, messageConsumer);

                executors.submit(consumer);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
