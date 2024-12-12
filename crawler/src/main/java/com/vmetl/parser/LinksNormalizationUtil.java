package com.vmetl.parser;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

public class LinksNormalizationUtil {
    private static final Set<String> excludedBeginnings = Set.of("#", ".");
    private static final Set<String> excludedPatterns = Set.of("/", "#", ".");
    private static final Pattern protocolPattern = Pattern.compile(".*:");
    private static final Pattern domainPatter = Pattern.compile("(?i)^(?:https?:\\/\\/)?(?:www\\.)?(?:[a-z0-9-]+\\.){1,9}[a-z]{2,5}(?:\\/.*)?$");

    public static Optional<String> normalizeLink(String linkHref, String url) {

        URI uri = URI.create(url);
        String host = uri.getHost();

        if (linkHref.startsWith("http:") || linkHref.startsWith("https:")) {
            return Optional.of(linkHref);
        }

        if (protocolPattern.matcher(linkHref).matches() || linkHref.isBlank()) {
            return Optional.empty();
        }

        boolean isExcluded = excludedPatterns.contains(linkHref) ||
                excludedBeginnings.stream().anyMatch(linkHref::startsWith);
        if (isExcluded) return Optional.empty();

        if (linkHref.startsWith("/")) {
            try {
                return Optional.of(new URI(uri.getScheme(), host, linkHref, null).toString());
            } catch (URISyntaxException e) {
                return Optional.empty();
            }
        }

        // this must leave us with relative link, or absolute linke without protocol (http:, https:)
        if (domainPatter.matcher(linkHref).matches()) {
            return Optional.of("http://" + linkHref);
        }

        return Optional.of(uri.getPath() + linkHref);
    }

}
