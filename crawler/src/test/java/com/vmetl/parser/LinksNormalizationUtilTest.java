package com.vmetl.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

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
}