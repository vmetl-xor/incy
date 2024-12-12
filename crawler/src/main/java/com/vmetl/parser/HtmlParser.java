package com.vmetl.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.vmetl.parser.LinksNormalizationUtil.normalizeLink;

public class HtmlParser {

    private static final Logger log = LoggerFactory.getLogger(HtmlParser.class);

    private static int counter = 0;

    public static Optional<SiteInformation> parse(String url) {
        if (counter++ > 50) return Optional.empty();
        Document doc;
        try {
            doc = Jsoup.connect(url).get();
        } catch (Exception e) {
            log.error("Error parsing {}", url);
            log.error("Error message {}", e.getMessage());

            return Optional.empty();
//            throw new RuntimeException(e);
        }

        SiteInformation siteInformation = new SiteInformation();
//        Elements refs = doc.select("a[href]");
        Elements refs = doc.getElementsByTag("a");
//        List<String> links = new ArrayList<>();

        List<String> internalRefs = refs.stream().
                peek(ref -> log.info("found link: {} : {}", ref.attr("href"), ref.text())).
                map(element -> normalizeLink(element.attr("href"), url)).
                filter(Optional::isPresent).
                map(Optional::get).toList();

        siteInformation.addAllReferences(internalRefs);

//        links.forEach(HtmlParser::parse);

        return Optional.of(siteInformation);
//        Elements p = doc.select("p");
//        for (Element element : p) {
//            System.out.println(p.val());
//        }
//        p.forEach(System.out::println);
    }


}
