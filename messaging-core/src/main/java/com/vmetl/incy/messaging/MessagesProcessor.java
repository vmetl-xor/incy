package com.vmetl.incy.messaging;

import com.vmetl.incy.task.TaskProcessor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class MessagesProcessor {

    private static final int NUMBER_OF_CONSUMERS = 10;

    @Autowired
    public MessagesProcessor(MessageConsumer messageConsumer, ObjectProvider<TaskProcessor> taskProcessorProvider) {


        for (int i = 1; i <= NUMBER_OF_CONSUMERS; i++) {
            String consumerName = "consumer-" + i;
            TaskProcessor consumer = taskProcessorProvider.getObject(consumerName, messageConsumer);

            ExecutorService executors = Executors.newFixedThreadPool(NUMBER_OF_CONSUMERS);
            executors.submit(consumer);
        }
    }

}
