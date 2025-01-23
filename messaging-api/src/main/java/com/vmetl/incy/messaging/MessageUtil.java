package com.vmetl.incy.messaging;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.vmetl.incy.messaging.Message.*;

public class MessageUtil {

    public static final String DOMAIN_REGEX_PATTERN = "(?i)^(?:https?:\\/\\/)?(?:www\\.)?(?:[a-z0-9-]+\\.){1,9}[a-z]{2,5}";

    private static final Pattern domainPatter = Pattern.compile(DOMAIN_REGEX_PATTERN);

    public static String getUrl(Message message) {
        return message.getPayload().get(URL);
    }

    /**
     * extract site name (domain) from url
     * @param message message
     * @return domain name, i.e. 'wwww.example.com'
     */
    public static String getDomain(Message message) {
        String url = message.getPayload().get(URL).toString();
        Matcher matcher = domainPatter.matcher(url);
        if (matcher.find()) {
            return matcher.group();
        }

        return url;
    }

    public static int getCurrentRefDepth(Message message) {
        return Integer.parseInt(message.getPayload().get(DEPTH));
    }
}
