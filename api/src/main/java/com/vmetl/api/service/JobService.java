package com.vmetl.api.service;

import com.vmetl.api.rest.dto.Job;
import com.vmetl.incy.cache.VisitedRefsCache;
import com.vmetl.incy.messaging.Message;
import com.vmetl.incy.messaging.MessagesService;
import io.micrometer.observation.annotation.Observed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class JobService {
    private final JdbcTemplate jdbcTemplate;
    Logger log = LoggerFactory.getLogger(JobService.class);

    private final MessagesService messagesService;
    private final VisitedRefsCache cache;

    @Autowired
    public JobService(MessagesService messagesService, VisitedRefsCache cache, JdbcTemplate jdbcTemplate) {
        this.messagesService = messagesService;
        this.cache = cache;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Observed
    public Optional<Job> createJob(Job job) {
        String url = job.getUrl();

        if (cache.exists(url)) {
            log.debug("Url {} has already been processed", url);
            return Optional.empty();
        }
        else cache.add(url);

        String id = UUID.randomUUID().toString();

        Message newMessage =
                new Message.MessageBuilder().
                        setId(id).
                        addDepth(job.getDepth()).
                        addUrl(url).
                        build();

        messagesService.sendMessage(newMessage, o -> null);

        return Optional.of(job);
    }

    public void stopAllProcessors() {
        messagesService.sendShutdownMessage();
    }

}
