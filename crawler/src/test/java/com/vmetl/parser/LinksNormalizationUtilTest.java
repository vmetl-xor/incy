package com.vmetl.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class LinksNormalizationUtilTest {

    @Test
    void testNormalizedEmptyLinkIsAbsent() {
        Optional<String> normalize = LinksNormalizationUtil.normalizeLink("", "");
        Assertions.assertFalse(normalize.isPresent());
    }

    @Test
    void testJavascriptLinkIsAbsent() {
        Optional<String> normalize = LinksNormalizationUtil.normalizeLink("javascript:print()", "www.test.com");
        Assertions.assertFalse(normalize.isPresent());
    }

    @ParameterizedTest
    @MethodSource("getIncorrectBeginningsLinks")
    void testExcludedBeginningsReturnEmpty(String internalUrl, String originalUrl) {
        Optional<String> normalize = LinksNormalizationUtil.normalizeLink(internalUrl, originalUrl);
        Assertions.assertFalse(normalize.isPresent());
    }


    public static Stream<Arguments> getIncorrectBeginningsLinks() {
        return Stream.of(
                Arguments.of("#", "www.test.com"),
                Arguments.of(".", "www.test.com")
        );
    }

}