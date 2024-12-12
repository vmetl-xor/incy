package com.vmetl.crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = {"com.vmetl.crawler", "com.vmetl.incy"})
public class CrawlerApplication {

    private static final Logger LOG = LoggerFactory.getLogger(CrawlerApplication.class);

    public static void main(String[] args) {

        LOG.info("STARTING THE APPLICATION");
        ConfigurableApplicationContext context = SpringApplication.run(CrawlerApplication.class, args);

//        // Start the producer
//        SimpleTaskProducer producer = context.getBean(SimpleTaskProducer.class);
//        producer.produceMessages();

        LOG.info("APPLICATION FINISHED");
    }

}
