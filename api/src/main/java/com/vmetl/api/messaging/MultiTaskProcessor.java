package com.vmetl.api.messaging;


import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class MultiTaskProcessor  {

    private static final int NUMBER_OF_CONSUMERS = 5;
    @Autowired
    private ObjectProvider<TaskProcessor> taskProcessorProvider;

    ExecutorService executors = Executors.newFixedThreadPool(NUMBER_OF_CONSUMERS);

    public void start() {

        for (int i = 1; i <= NUMBER_OF_CONSUMERS; i++) {
            String consumerName = "consumer-" + i;
            TaskProcessor consumer = taskProcessorProvider.getObject(consumerName);

            executors.submit(consumer);
        }
    }

}
