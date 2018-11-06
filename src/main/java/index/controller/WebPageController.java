/*
 * $Id$
 *
 * Copyright (c) 2003, 2004 WorldTicket A/S
 * All rights reserved.
 */
package index.controller;

import graph.GraphGenerator;
import index.entity.WebPage;
import index.repository.WebPageRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    private static Logger logger = LoggerFactory.getLogger(GraphGenerator.class);

    @GetMapping("/mercury/{text}")
    public List<WebPage> getAllWebPage(@PathVariable String text, @RequestParam("sort") String sortBy, @RequestParam("direction") String direction) {
        Sort sort = null;
        if (StringUtils.isNotEmpty(sortBy)) {
            sort = new Sort(Sort.Direction.fromString(direction), sortBy);
        } else {
            sort = new Sort(Sort.Direction.ASC, "id");
        }
        List<WebPage> webPages = webPageRepository.findAllByText(text, sort);

        return webPages.stream().map(webPage -> {
            logger.info(StringUtils.substringAfterLast(webPage.getId(), "/"));
            return new WebPage(webPage, urlMap.get(StringUtils.substringAfterLast(webPage.getId(), "/")));
        }).collect(Collectors.toList());
    }
}
