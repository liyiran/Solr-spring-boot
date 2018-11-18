/*
 * $Id$
 *
 * Copyright (c) 2003, 2004 WorldTicket A/S
 * All rights reserved.
 */
package index.service;

import index.SolrConfig;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.SuggesterResponse;
import org.apache.solr.client.solrj.response.Suggestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
        Optional<String> e1 = known(edits1(text)).max((a, b) -> dict.get(a) - dict.get(b));
        if (e1.isPresent()) return dict.containsKey(text) ? text : e1.get();
        Optional<String> e2 = known(edits1(text).map(this::edits1).flatMap((x) -> x)).max((a, b) -> dict.get(a) - dict.get(b));
        return (e2.orElse(text));
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

}
