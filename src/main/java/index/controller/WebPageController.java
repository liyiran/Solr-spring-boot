/*
 * $Id$
 *
 * Copyright (c) 2003, 2004 WorldTicket A/S
 * All rights reserved.
 */
package index.controller;

import index.entity.WebPage;
import index.repository.WebPageRepository;
import index.service.SolrService;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.Suggestion;
import org.apache.tika.exception.TikaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Yiran Li / 2M business applications a|s
 * @version $Revision$ $Date$
 */
@RestController
public class WebPageController {
    @Autowired
    private WebPageRepository webPageRepository;
    @Qualifier("urlMap")
    @Autowired
    private Map<String, String> urlMap;
    @Autowired
    private SolrService solrService;
    private static Logger logger = LoggerFactory.getLogger(WebPageController.class);

    @GetMapping("/mercury/{text}")
    public List<WebPage> getAllWebPage(@PathVariable String text, @RequestParam("sort") String sortBy, @RequestParam("direction") String direction) {
        Sort sort = null;
        if (StringUtils.isNotEmpty(sortBy)) {
            sort = new Sort(Sort.Direction.fromString(direction), sortBy);
        }
        List<WebPage> webPages = webPageRepository.findAllByText(text, sort);

        return webPages.stream().map(webPage -> {
            logger.info(StringUtils.substringAfterLast(webPage.getId(), "/"));
            if (StringUtils.isBlank(webPage.getUrl())) {
                webPage = new WebPage(webPage, urlMap.get(StringUtils.substringAfterLast(webPage.getId(), "/")));
            }
            try {
                webPage.setOgDescription(solrService.getSnippet(webPage.getUrl(), text));
            } catch (IOException | TikaException e) {
                logger.error("snippet for URL:" + webPage.getUrl(), e);
            }
            return webPage;
        }).limit(10).collect(Collectors.toList());
    }

    @GetMapping("/mercury/suggest/{text}")
    public List<String> getSuggestions(@PathVariable String text) throws IOException, SolrServerException {
        List<Suggestion> suggestions = solrService.getSuggestion(text);
        return suggestions.stream().map(Suggestion::getTerm).collect(Collectors.toList());
    }

    @GetMapping("/mercury/correction/{text}")
    public String getCorrection(@PathVariable String text) {
        return solrService.correct(text);
    }

//    @GetMapping("/mercury/snippet/{text}")
//    public String getSnippet(@RequestParam("url") String url, @PathVariable String text) throws IOException, TikaException {
//        return solrService.getSnippet(URLDecoder.decode(url, Charset.defaultCharset()), text);
//    }
}
