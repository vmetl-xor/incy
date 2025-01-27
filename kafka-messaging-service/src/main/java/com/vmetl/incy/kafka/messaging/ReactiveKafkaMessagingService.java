package com.vmetl.incy.kafka.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmetl.incy.messaging.Message;
import com.vmetl.incy.messaging.MessagesService;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

@Component
@Primary
public class ReactiveKafkaMessagingService implements MessagesService {
    Logger log = LoggerFactory.getLogger(ReactiveKafkaMessagingService.class);

    private final KafkaSender<String, String> kafkaSender;
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public ReactiveKafkaMessagingService(KafkaSender<String, String> kafkaSender) {
        this.kafkaSender = kafkaSender;
    }

    @Override
    public void sendMessageReactive(Mono<Message> inMsg, Function<Object, Object> postAction) {

        Mono<SenderRecord<String, String, String>> mono = inMsg.map(message -> {
            String msgString = "";
            try {
                msgString = mapper.writeValueAsString(message);
            } catch (JsonProcessingException ex) {
                log.error("Error serializing message", ex);
            }

            ProducerRecord<String, String> producerRecord =
                    new ProducerRecord<>("topic1", msgString);

            return SenderRecord.create(producerRecord, msgString);
        });

        kafkaSender.send(mono).
                doOnNext(record -> log.debug("Sent message: {}", record)).
                doOnNext(postAction::apply).
                doOnError(error -> log.error("Error sending message", error)).
                subscribe();
    }

    @Override
    public void sendShutdownMessage() {
        String shutdown = "SHUTDOWN";
        ProducerRecord<String, String> producerRecord =
                new ProducerRecord<>("topic2", shutdown);

        SenderRecord<String, String, String> rc = SenderRecord.create(producerRecord, shutdown);

        kafkaSender.send(Mono.just(rc)).
                doOnNext(record -> log.info("Sent SHUTDOWN message: {}", record)).
                doOnError(error -> log.error("Error sending SHUTDOWN message", error)).
                subscribe();
    }

    @Override
    public void sendMessage(Message message, Function<Object, Object> postAction) {
        this.sendMessageReactive(Mono.just(message), postAction);
    }
}
