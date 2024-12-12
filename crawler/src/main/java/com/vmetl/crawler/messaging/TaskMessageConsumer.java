package com.vmetl.crawler.messaging;

import com.vmetl.incy.DbService;
import com.vmetl.incy.messaging.Message;
import com.vmetl.incy.messaging.MessageConsumer;
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
public class TaskMessageConsumer implements MessageConsumer<Object, Object> {
    Logger log = LoggerFactory.getLogger(TaskMessageConsumer.class);

    private final MessagesService<String, String> messagesService;
    private final DbService dbService;

    @Autowired
    public TaskMessageConsumer(MessagesService<String, String> messagesService, DbService dbService) {
        this.messagesService = messagesService;
        this.dbService = dbService;
    }

    @Override
    public void consume(Message<Object, Object> message) {
        log.info("Received message: {}", message);
        Optional<SiteInformation> siteInfo = HtmlParser.parse(message.getPayload().get("site").toString());
        siteInfo.ifPresent(siteInformation -> {
            siteInformation.getReferences().
                    forEach(urlRef -> messagesService.
                            sendMessage(Message.of(UUID.randomUUID().toString(), Map.of("site", urlRef))));

            dbService.updateSiteStatistics(siteInformation);
        });
    }
}
