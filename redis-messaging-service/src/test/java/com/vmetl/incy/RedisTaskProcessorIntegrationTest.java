package com.vmetl.incy;

import com.vmetl.incy.messaging.MessageConsumer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.GenericContainer;

import java.util.concurrent.CompletableFuture;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {TestRedisApplication.class})
class RedisTaskProcessorIntegrationTest {

    @MockBean
    private MessageConsumer messageConsumer;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static GenericContainer<?> redisContainer =
            new GenericContainer<>("redis:7-alpine").withExposedPorts(6379);

    @BeforeAll
    static void beforeAll() {
//        redisContainer =
//                new GenericContainer<>("redis:7-alpine").withExposedPorts(6379);
        redisContainer.start();
    }

    @BeforeEach
    void setUp() {
    }

    @AfterAll
    static void afterAll() {
        redisContainer.stop();
    }

    @Test
    void whenMessageSent_shouldProcessTask() {

        ProcessorsRunningState runningState = new ProcessorsRunningState();
        RedisTaskProcessor taskProcessor = new RedisTaskProcessor(redisTemplate,
                                        runningState, "test-consumer", messageConsumer);

        CompletableFuture.runAsync(taskProcessor).thenRun(() -> {
            // add assertions here
            System.out.println("Stopped processor");
        });

        runningState.stop();
    }

    @DynamicPropertySource
    static void overrideRedisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.redis.host", redisContainer::getHost);
        registry.add("spring.redis.port",
                () -> redisContainer.getMappedPort(6379));
    }


}