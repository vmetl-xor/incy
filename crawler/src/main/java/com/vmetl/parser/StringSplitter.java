package com.vmetl.parser;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class StringSplitter {
    private static final Set<Character> splitSymbols = Set.of(' ', '\t', '\n', '\r', '.', ',', '!', '?', ')', '(');
    public static final String DELIMITER_REGEX = "[ \t\n\r.,!?()]";
    public static  final Pattern pattern = Pattern.compile(DELIMITER_REGEX);

    public static Stream<String> getWordsStreamRegex(String text) {
        return Arrays.stream(text.split(DELIMITER_REGEX)).filter(s -> !s.isEmpty());
    }

    //the fastest, default to go
    public static Stream<String> getWordsStream(String text) {
        return pattern.splitAsStream(text).filter(s -> !s.isEmpty());
    }

    // this only exists for demo purposes, it is slower ~3x times
    public static Stream<String> getWordsStreamSlowest(String text) {
        return text.chars().
                mapToObj(c -> (char) c).
                collect(() -> new ArrayList<StringBuilder>() {{
                            add(new StringBuilder());
                        }},
                        (stringBuilders, character) -> {
                            if (splitSymbols.contains(character)) {

                                if (!stringBuilders.getLast().isEmpty()) {
                                    stringBuilders.add(new StringBuilder());
                                }
                            } else {
                                stringBuilders.getLast().append(character);
                            }
                        },
                        (left, right) -> {
                        }
                ).
                stream().
                filter(stringBuilder -> !stringBuilder.isEmpty()).
                map(StringBuilder::toString);
    }

    // a bit faster.. still slow
    public static Stream<String> getWordsStreamSlow(String text) {

        List<StringBuilder> stringBuilders = new ArrayList<>();
        stringBuilders.add(new StringBuilder());

        text.chars().mapToObj(value -> (char) value).forEach(character -> {
            if (splitSymbols.contains(character)) {

                if (!stringBuilders.getLast().isEmpty()) {
                    stringBuilders.add(new StringBuilder());
                }
            } else {
                stringBuilders.getLast().append(character);
            }
        });

        return stringBuilders.
                stream().
                filter(stringBuilder -> !stringBuilder.isEmpty()).
                map(StringBuilder::toString);
    }
}
