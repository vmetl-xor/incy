package com.vmetl.incy.kafka.tasks;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmetl.incy.messaging.Message;
import com.vmetl.incy.messaging.MessageConsumer;
import com.vmetl.incy.task.TaskProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.scheduler.Schedulers;
import reactor.kafka.receiver.KafkaReceiver;

public class KafkaTaskProcessor implements TaskProcessor {
    Logger log = LoggerFactory.getLogger(KafkaTaskProcessor.class);
    private final KafkaReceiver<String, String> kafkaReceiver;

    private final String consumerName;
    private final MessageConsumer consumer;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public KafkaTaskProcessor(KafkaReceiver<String, String> kafkaReceiver, String consumerName, MessageConsumer consumer) {
        this.kafkaReceiver = kafkaReceiver;
        this.consumerName = consumerName;
        this.consumer = consumer;
    }

    @Override
    public void run() {
        kafkaReceiver.receive().
                subscribeOn(Schedulers.boundedElastic()).
                doOnNext(record -> {
                    String value = record.value();
                    String key = record.key();
                    log.info("Received message: key={}, value={}, partition={}, offset={}}",
                            key, value, record.partition(), record.offset());
                    // Acknowledge the message
                    record.receiverOffset().acknowledge();
                }).
                map(record -> {
                    String value = record.value();
                    Message message;
                    try {
                        message = objectMapper.readValue(value, Message.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    return message;
                }).
                publishOn(Schedulers.boundedElastic()). //todo make all steps using reactive approach
                flatMap(message -> consumer.consumeAsync(message).thenReturn(message)).
                subscribe();
    }
}
