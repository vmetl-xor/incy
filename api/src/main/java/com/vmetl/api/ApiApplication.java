package com.vmetl.api;

import com.vmetl.api.data.DbConfig;
import com.vmetl.api.messaging.MultiTaskProcessor;
import com.vmetl.api.messaging.SimpleTaskProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class ApiApplication
//{
        implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory
            .getLogger(ApiApplication.class);
    @Autowired
    private Demo demo;

    @Autowired
    private SimpleTaskProducer taskProducer;

    @Autowired
    private MultiTaskProcessor taskProcessor;

    public static void main(String[] args) {

//        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("com.vmetl.api");
//
//        // Start the producer
//        SimpleTaskProducer producer = context.getBean(SimpleTaskProducer.class);
//        producer.produceMessages();
//
//        // Start consumers
//        MultiTaskProcessor mainConsumer = new MultiTaskProcessor();
//        mainConsumer.start();
//
//        context.registerShutdownHook();


                LOG.info("STARTING THE APPLICATION");
        SpringApplication.run(ApiApplication.class, args);
        LOG.info("APPLICATION FINISHED");
    }

    @Override
    public void run(String... args) {
//        LOG.info("EXECUTING : command line runner");
//
//        for (int i = 0; i < args.length; ++i) {
//            LOG.info("args[{}]: {}", i, args[i]);
//        }
//
//        demo.save("123");
//
//        LOG.info("find by id returned {}", demo.findById("123"));

        taskProducer.produceMessages();
        taskProcessor.start();
    }

    @Component
    public static class Demo {

        private RedisTemplate<String, String> redisTemplate;

        @Autowired
        public Demo(RedisTemplate<String, String> redisTemplate) {
            this.redisTemplate = redisTemplate;
        }

        public void save(String longStr) {
            redisTemplate.opsForValue().set(longStr, longStr);
//            redisTemplate.
        }

        public String findById(String id) {
            return redisTemplate.opsForValue().get(id);
        }

    }

}
