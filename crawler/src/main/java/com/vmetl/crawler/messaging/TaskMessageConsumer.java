package com.vmetl.crawler.messaging;

import com.vmetl.incy.SiteDao;
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

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public class TaskMessageConsumer implements MessageConsumer {
    Logger log = LoggerFactory.getLogger(TaskMessageConsumer.class);

    private final MessagesService messagesService;
    private final SiteDao dbService;
    private final VisitedRefsCache cache;

    @Value("${incy.crawler.default.depth}")
    private int defaultDepth;

    @Autowired
    public TaskMessageConsumer(MessagesService messagesService, CacheAwareDbService dbService, VisitedRefsCache cache) {
        this.messagesService = messagesService;
        this.dbService = dbService;
        this.cache = cache;
    }

    @Override
    public void consume(Message message) {
        log.debug("Received message: {}", message);

        String siteName = MessageUtil.getDomain(message);
        dbService.addSite(siteName);

        Optional<SiteInformation> siteInfo = HtmlParser.parse(siteName);

        siteInfo.ifPresent(siteInformation -> {

            boolean shouldProcessDeeper = MessageUtil.getCurrentRefDepth(message) < defaultDepth;

            if (shouldProcessDeeper) {
                siteInformation.getReferences().stream().
                        filter(ref -> !cache.exists(ref)).
                        forEach(ref -> {

                            Message newMessage =
                                    new Message.MessageBuilder().
                                            setId(UUID.randomUUID().toString()).
                                            addDepth(MessageUtil.getCurrentRefDepth(message) + 1).
                                            addUrl(ref).
                                            build();
                            messagesService.
                                    sendMessage(
                                            newMessage, o -> 0);
                        });
            } else {
                log.info("REACHED MAX DEPTH OF {}, terminating", MessageUtil.getCurrentRefDepth(message));
            }

            Map<String, Integer> wordStats = siteInformation.getWordsFrequency();

            dbService.getSiteIdByName(siteName).
                    ifPresentOrElse(siteId -> dbService.updateSiteStatistics(siteId, wordStats),
                            () -> log.error("No site found: {}", siteName));
//            idle();
        });
    }

    private static void idle() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
