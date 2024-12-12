package com.vmetl.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SiteInformation {

    private final List<String> references;
    private final Map<String, Integer> wordsFrequency;

    public SiteInformation() {
        this.references = new ArrayList<>();
        this.wordsFrequency = new HashMap<>();
    }

    public void addAllReferences(List<String> references) {
        this.references.addAll(references);
    }

    public void addReference(String reference) {
        references.add(reference);
    }

    public void addWordFrequency(String word, int frequency) {
        wordsFrequency.put(word, frequency);
    }

    public List<String> getReferences() {
        return references;
    }

    public int getWordFrequency(String word) {
        return wordsFrequency.get(word);
    }

    public Map<String, Integer> getWordsFrequency() {
        return wordsFrequency;
    }
}
