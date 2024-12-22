package com.vmetl.parser;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;


public class LinksNormalizationUtil {
    //todo refine regexs, protocol like 'mailto:', 'ftp:' handling
    private static final String URL_REGEX_PATTERN = "(?i)^(?:https?:\\/\\/)?(?:www\\.)?(?:[a-z0-9-]+\\.){1,9}[a-z]{2,5}(?:\\/.*)?$";

    private static final Set<String> excludedBeginnings = Set.of("#", ".", "//", "?");
    private static final Set<String> excludedPatterns = Set.of("/", "#", ".");
    private static final Pattern protocolPattern = Pattern.compile("^.*:");
    private static final Pattern domainPatter = Pattern.compile(URL_REGEX_PATTERN);

    public static Optional<String> normalizeLink(String linkHref, String originalUrl) {

        URI originalUri = URI.create(originalUrl);
        String host = originalUri.getHost();

        if (linkHref.startsWith("http:") || linkHref.startsWith("https:")) {
            return Optional.of(linkHref);
        }

        if (protocolPattern.matcher(linkHref).find() || linkHref.isBlank()) {
            return Optional.empty();
        }

        boolean isExcluded = excludedPatterns.contains(linkHref) ||
                excludedBeginnings.stream().anyMatch(linkHref::startsWith);
        if (isExcluded) return Optional.empty();

        if (linkHref.startsWith("/")) {
            try {
                return Optional.of(new URI(originalUri.getScheme(), host, linkHref, null).toString());
            } catch (URISyntaxException e) {
                return Optional.empty();
            }
        }

        // this must leave us with relative link, or absolute link without protocol (http:, https:)
        if (domainPatter.matcher(linkHref).matches()) {
            return Optional.of("http://" + linkHref);
        }

        return Optional.of(originalUri.getPath() + linkHref);
    }

}
