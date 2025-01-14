package com.vmetl.incy;

import com.vmetl.incy.messaging.Message;
import com.vmetl.incy.messaging.MessageConsumer;
import com.vmetl.incy.messaging.MessagesService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.GenericContainer;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {TestConfiguration.class})
class RedisTaskProcessorIntegrationTest {

    @MockBean
    private MessageConsumer messageConsumer;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private MessagesService messagesService;

    private static final GenericContainer<?> redisContainer =
            new GenericContainer<>("redis:7-alpine").withExposedPorts(6379);

    @BeforeAll
    static void beforeAll() {
        redisContainer.start();
    }

    @BeforeEach
    void setUp() {
    }

    @Test
    void whenMessageSent_shouldProcessTask() throws InterruptedException {

        messagesService.sendMessage(
                new Message.MessageBuilder().
                        addUrl("http://www.test.org").
                        addDepth(0).
                        build(),
                o -> null);

        ProcessorsRunningState runningState = new ProcessorsRunningState();
        RedisTaskProcessor taskProcessor = new RedisTaskProcessor(redisTemplate,
                                        runningState, "test-consumer", messageConsumer);

        CompletableFuture.runAsync(taskProcessor);
        Thread.sleep(1_000);
        runningState.stop();

        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        verify(messageConsumer, times(1)).consume(messageCaptor.capture());

        Message message = messageCaptor.getValue();
        assertThat(message.getPayload()).containsKey("URL");
        assertThat(message.getPayload()).containsValue("http://www.test.org");
    }

    @DynamicPropertySource
    static void overrideRedisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redisContainer::getHost);
        registry.add("spring.data.redis.port",
                () -> redisContainer.getMappedPort(6379));
    }


}