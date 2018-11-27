/*
 * $Id$
 *
 * Copyright (c) 2003, 2004 WorldTicket A/S
 * All rights reserved.
 */
package index.service;

import index.SolrConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.SuggesterResponse;
import org.apache.solr.client.solrj.response.Suggestion;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;

/**
 * @author Yiran Li / 2M business applications a|s
 * @version $Revision$ $Date$
 */
@Service
public class SolrServiceImpl implements SolrService {
    private final HttpSolrClient client;
    private final SolrConfig solrConfig;
    private Map<String, Integer> dict = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(StringBuilder.class);
    private static final Pattern END_OF_SENTENCE = Pattern.compile("\\.\\s+");

    @Autowired
    public SolrServiceImpl(HttpSolrClient client, SolrConfig dictPath) {
        this.client = client;
        this.solrConfig = dictPath;
    }

    @Override
    public List<Suggestion> getSuggestion(String text) throws IOException, SolrServerException {
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setRequestHandler("/myexample/suggest");
        solrQuery.setQuery(text);
        SuggesterResponse suggesterResponse = client.query(solrQuery).getSuggesterResponse();
        return suggesterResponse.getSuggestions().get("suggest");
    }

    @Override
    public String correct(String text) {
        Optional<String> e1 = known(edits1(text)).max(Comparator.comparingInt(a -> dict.get(a)));
        if (e1.isPresent()) return dict.containsKey(text) ? text : e1.get();
        Optional<String> e2 = known(edits1(text).map(this::edits1).flatMap((x) -> x)).max(Comparator.comparingInt(a -> dict.get(a)));
        return (e2.orElse(text));
    }

    @Override
    public String getSnippet(String url, String text) throws IOException, TikaException {
        Tika tika = new Tika();
        String html = tika.parseToString(new URL(url)).replaceAll("[^a-zA-Z0-9]", " ").replaceAll("\\s+", " ");
        return findSnippet(html, text);
    }

    @PostConstruct
    public void initAfterStartup() throws IOException {
        Stream.of(new String(Files.readAllBytes(Paths.get(solrConfig.getDictPath()))).toLowerCase().replaceAll("[^a-z ]", "").split(" ")).forEach((word) -> {
            dict.compute(word, (k, v) -> v == null ? 1 : v + 1);
        });
    }

    private Stream<String> known(Stream<String> words) {
        return words.filter((word) -> dict.containsKey(word));
    }

    private Stream<String> edits1(final String word) {
        Stream<String> deletes = IntStream.range(0, word.length()).mapToObj((i) -> word.substring(0, i) + word.substring(i + 1));
        Stream<String> replaces = IntStream.range(0, word.length()).boxed().flatMap((i) -> "abcdefghijklmnopqrstuvwxyz".chars().mapToObj((c) -> word.substring(0, i) + (char) c + word.substring(i + 1)));
        Stream<String> inserts = IntStream.range(0, word.length() + 1).boxed().flatMap((i) -> "abcdefghijklmnopqrstuvwxyz".chars().mapToObj((c) -> word.substring(0, i) + (char) c + word.substring(i)));
        Stream<String> transposes = IntStream.range(0, word.length() - 1).mapToObj((i) -> word.substring(0, i) + word.substring(i + 1, i + 2) + word.charAt(i) + word.substring(i + 2));
        return Stream.of(deletes, replaces, inserts, transposes).flatMap((x) -> x);
    }

    String findSnippet(String original, String target) {
//        original = original.toLowerCase();
//        target = target.toLowerCase();
        String[] sentences = original.split("\\.\\s+");
        String allWords = null;
        String noOrder = null;
        String partial = null;
        int index = 0;
        for (String sentence : sentences) {
            if (allWords == null && StringUtils.containsIgnoreCase(sentence, target)) {
                allWords = sentence;
                index = StringUtils.indexOfIgnoreCase(sentence,target);
            } else {
                String[] ta = target.split(" ");
                int allIndex = containsAll(sentence, ta);
                int anyIndex = containAny(sentence, ta);
                if (noOrder == null && allIndex != -1) {
                    noOrder = sentence;
                    index = allIndex;
                } else if (partial == null && anyIndex != -1) {
                    partial = sentence;
                    index = anyIndex;
                }
            }
        }
        String snippet = allWords != null ? allWords : noOrder != null ? noOrder : partial;
        if (StringUtils.isNotEmpty(snippet)) {
            snippet = snippet.substring(index, index + 160);
        }
        return snippet;
    }

    private int containsAll(String original, String[] targets) {
        for (String target : targets) {
            if (!StringUtils.containsIgnoreCase(original, target)) {
                return -1;
            }
        }
        return StringUtils.indexOfIgnoreCase(original, targets[0]);
    }

    private int containAny(String orignal, String[] targets) {
        for (String target : targets) {
            if (StringUtils.containsIgnoreCase(orignal, target)) {
                return StringUtils.indexOfIgnoreCase(orignal, target);
            }
        }
        return -1;
    }

    private List<Integer> findIndex(String sentence, String[] targets) {
        return Arrays.stream(targets).map(sentence::indexOf).collect(Collectors.toList());
    }

}
