package com.vmetl.crawler.messaging;

import com.vmetl.incy.cache.RefsCache;
import com.vmetl.incy.DbService;
import com.vmetl.incy.messaging.Message;
import com.vmetl.incy.messaging.MessageConsumer;
import com.vmetl.incy.messaging.MessageUtil;
import com.vmetl.incy.messaging.MessagesService;
import com.vmetl.parser.HtmlParser;
import com.vmetl.incy.SiteInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public class TaskMessageConsumer implements MessageConsumer {
    Logger log = LoggerFactory.getLogger(TaskMessageConsumer.class);

    private final MessagesService messagesService;
    private final DbService dbService;
    private final RefsCache cache;

    @Autowired
    public TaskMessageConsumer(MessagesService messagesService, DbService dbService, RefsCache cache) {
        this.messagesService = messagesService;
        this.dbService = dbService;
        this.cache = cache;
    }

    @Override
    public void consume(Message message) {
        log.debug("Received message: {}", message);

        boolean shouldProcessDeeper = MessageUtil.getCurrentRefDepth(message) < MessageUtil.getGlobalRefDepth(message);

        String siteName = MessageUtil.getSite(message);
        Optional<SiteInformation> siteInfo = HtmlParser.parse(siteName);

        siteInfo.ifPresent(siteInformation -> {

            if (shouldProcessDeeper) {
                siteInformation.getReferences().stream().
                        filter(ref -> !cache.exists(ref)).
                        forEach(urlRef -> {

                            Message newMessage =
                                    new Message.MessageBuilder().
                                            setId(UUID.randomUUID().toString()).
                                            addDepth(MessageUtil.getCurrentRefDepth(message) + 1).
                                            addSite(urlRef).
                                            build();

                            messagesService.
                                    sendMessage(
                                            newMessage, o -> cache.add(urlRef));
                        });
            } else {
                log.info("REACHED MAX DEPTH OF {}, terminating", MessageUtil.getCurrentRefDepth(message));
            }

            Map<String, Integer> wordStats = siteInformation.getWordsFrequency();

            dbService.getSiteIdByName(siteName);
            dbService.updateSiteStatistics(1, wordStats);

        });
    }
}
