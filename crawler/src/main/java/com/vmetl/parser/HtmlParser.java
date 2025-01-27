package com.vmetl.parser;

import com.vmetl.crawler.fetch.UrlContent;
import com.vmetl.incy.SiteInformation;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static com.vmetl.parser.LinksNormalizationUtil.normalizeLink;
import static com.vmetl.parser.StringSplitter.getWordsStream;

public class HtmlParser {

    private static final Logger log = LoggerFactory.getLogger(HtmlParser.class);

    //todo decouple url fetch and parsing
    public static Optional<SiteInformation> parse(UrlContent urlContent) {

        Document doc = urlContent.document();
        String url = urlContent.url();

        SiteInformation siteInformation = new SiteInformation();
        Elements refs = doc.getElementsByTag("a");

        List<String> internalRefs = refs.stream().
                map(element -> normalizeLink(element.attr("href"), url)).
                filter(Optional::isPresent).
                map(Optional::get).toList();

        siteInformation.addAllReferences(internalRefs);


        Map<String, Integer> wordsCount =
                getWordsStream(doc.text()).
                        collect(Collectors.toMap(word -> word, word -> 1, Integer::sum, HashMap::new));
        siteInformation.addAllWordFrequency(wordsCount);


        return Optional.of(siteInformation);
    }


}
