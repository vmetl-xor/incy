package com.vmetl.incy.messaging;

import java.util.Optional;

import static com.vmetl.incy.messaging.Message.*;

public class MessageUtil {

    public static String getSite(Message message) {
        return message.getPayload().get(SITE).toString();
    }

    public static int getCurrentRefDepth(Message message) {
        Object v = message.getPayload().get(DEPTH);
        return (Integer) v;
    }

    public static int getGlobalRefDepth(Message message) {
        Object v = message.getPayload().get(GLOBAL_DEPTH);
        return (Integer) v;
    }

    public static void addRefDepth(Message message, int depth) {
        message.getPayload().put(DEPTH, depth);
    }

    public static void addGlobalDepth(Message message, int depth) {
        message.getPayload().put(GLOBAL_DEPTH, depth);
    }

    public static void addSite(Message message, String site) {
        message.getPayload().put(SITE, site);
    }

}
