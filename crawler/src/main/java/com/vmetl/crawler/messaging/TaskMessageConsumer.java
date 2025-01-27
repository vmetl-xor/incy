package com.vmetl.crawler.messaging;

import com.vmetl.crawler.fetch.UrlContent;
import com.vmetl.crawler.fetch.UrlFetch;
import com.vmetl.incy.dao.SiteDao;
import com.vmetl.incy.cache.VisitedRefsCache;
import com.vmetl.incy.CacheAwareDbService;
import com.vmetl.incy.messaging.Message;
import com.vmetl.incy.messaging.MessageConsumer;
import com.vmetl.incy.messaging.MessageUtil;
import com.vmetl.incy.messaging.MessagesService;
import com.vmetl.parser.HtmlParser;
import com.vmetl.incy.SiteInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public class TaskMessageConsumer implements MessageConsumer {
    Logger log = LoggerFactory.getLogger(TaskMessageConsumer.class);

    private final MessagesService messagesService;
    private final SiteDao dbService;
    private final VisitedRefsCache cache;
    private final UrlFetch urlFetch;

    @Value("${incy.crawler.default.depth}")
    private int defaultDepth;

    @Autowired
    public TaskMessageConsumer(MessagesService messagesService, CacheAwareDbService dbService, VisitedRefsCache cache, UrlFetch urlFetch) {
        this.messagesService = messagesService;
        this.dbService = dbService;
        this.cache = cache;
        this.urlFetch = urlFetch;
    }

    @Override
    public void consume(Message message) {
        consumeMessage(message);
    }

    @Override
    public Mono<Void> consumeAsync(Message message) {
        return Mono.fromRunnable(() -> consumeMessage(message));
    }

    private void consumeMessage(Message message) {
        log.info("Received message: {}", message);

        String siteName = MessageUtil.getDomain(message);
        dbService.addSite(siteName);

        urlFetch.fetchUrl(siteName).flatMap(HtmlParser::parse).
                ifPresent(siteInformation -> {
                    processMessage(message, siteInformation, siteName);
                });
    }

    private void processMessage(Message message, SiteInformation siteInformation, String siteName) {
        boolean shouldProcessDeeper = MessageUtil.getCurrentRefDepth(message) < defaultDepth;

        if (shouldProcessDeeper) {
            siteInformation.getReferences().stream().
                    filter(ref -> !cache.exists(ref)).
                    distinct().
                    forEach(ref -> sendMessage(message, ref));
        } else {
            log.info("REACHED MAX DEPTH OF {} FOR {}, terminating", MessageUtil.getCurrentRefDepth(message), MessageUtil.getUrl(message));
        }

        Map<String, Integer> wordStats = siteInformation.getWordsFrequency();

        dbService.getSiteIdByName(siteName).
                ifPresentOrElse(siteId -> dbService.updateSiteStatistics(siteId, wordStats),
                        () -> log.error("No site found: {}", siteName));
        idle();
    }

    private void sendMessage(Message message, String ref) {
        Message newMessage =
                new Message.MessageBuilder().
                        setId(UUID.randomUUID().toString()).
                        addDepth(MessageUtil.getCurrentRefDepth(message) + 1).
                        addUrl(ref).
                        build();
        messagesService.
                sendMessage(
                        newMessage, o -> cache.add(ref));
    }

    private static void idle() {
        try {
            Thread.sleep(5_000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
