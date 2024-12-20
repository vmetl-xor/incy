package com.vmetl.parser;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.util.Set;
import java.util.stream.Collectors;

class StringSplitterTest {

//    private static final String TEXT_STRING = "Depending on the interview, the output of the traversed web pages may be used for different purposes. This can have some consequences on the overall design. For example, a search engine would need to index the data and rank it (using PageRank or other algorithms), while a company like OpenAI would dump the raw text from the pages into a database to be used to train LLMs (Large Language Models). Regardless of the use case, the interview is likely to focus on the crawling task—how can we efficiently crawl the web, extract the necessary data, and store it in a way that is easily accessible?\n" +
//            "For our purposes, we'll design a web crawler whose goal is to extract text data from the web to train an LLM. This could be used by a company like OpenAI to train their GPT-4 model, Google to train Gemini, Meta to train LLaMA, etc.";
    public static final int ITERATIONS = 100;
    private String TEXT_STRING;

    @BeforeEach
    void setUp() throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("little_dorrit.txt");

        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
                (is, StandardCharsets.UTF_8))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }
        TEXT_STRING = textBuilder.toString();
    }

    @Test
    void getWordsStreamManual() {
        Clock clock = Clock.systemUTC();
        long start = clock.millis();
        Set<String> words = null;
        for (int i = 0; i < ITERATIONS; i++) {
            words = StringSplitter.getWordsStreamSlowest(TEXT_STRING).collect(Collectors.toSet());
        }
        long end = clock.millis();
        System.out.println("Start - end = " + (end - start) + " size: " + words.size());

//        Assertions.assertThat(words).containsAll(Set.of("THis", "is", "a", "difficult", "string", "no"));
        Assertions.assertThat(words.size()).isGreaterThan(0);
    }

    @Test
    void getWordsStreamFast() {
        Clock clock = Clock.systemUTC();
        long start = clock.millis();
        Set<String> words = null;
        for (int i = 0; i < ITERATIONS; i++) {
            words = StringSplitter.getWordsStreamSlow(TEXT_STRING).collect(Collectors.toSet());
        }
        long end = clock.millis();
        System.out.println("Start - end = " + (end - start) + " size: " + words.size());

//        Assertions.assertThat(words).containsAll(Set.of("THis", "is", "a", "difficult", "string", "no"));
        Assertions.assertThat(words.size()).isGreaterThan(0);
    }

    @Test
    void getWordsStreamRegex() {
        Clock clock = Clock.systemUTC();
        long start = clock.millis();
        Set<String> words = null;
        for (int i = 0; i < ITERATIONS; i++) {
            words = StringSplitter.getWordsStreamRegex(TEXT_STRING).collect(Collectors.toSet());
        }
        long end = clock.millis();
        System.out.println("Start - end = " + (end - start) + " size: " + words.size());

//        Assertions.assertThat(words).containsAll(Set.of("THis", "is", "a", "difficult", "string", "no"));
        Assertions.assertThat(words.size()).isGreaterThan(0);

    }

    @Test
    void getWordsStream() {
        Clock clock = Clock.systemUTC();
        long start = clock.millis();
        Set<String> words = null;
        for (int i = 0; i < ITERATIONS; i++) {
            words = StringSplitter.getWordsStream(TEXT_STRING).collect(Collectors.toSet());
        }
        long end = clock.millis();
        System.out.println("Start - end = " + (end - start) + " size: " + words.size());

//        Assertions.assertThat(words).containsAll(Set.of("THis", "is", "a", "difficult", "string", "no"));
        Assertions.assertThat(words.size()).isGreaterThan(0);

    }
}