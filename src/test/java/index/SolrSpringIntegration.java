package index;/*
 * $Id$
 *
 * Copyright (c) 2003, 2004 WorldTicket A/S
 * All rights reserved.
 */

import static org.junit.Assert.assertEquals;

import index.entity.WebPage;
import index.repository.WebPageRepository;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.core.CoreContainer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author Yiran Li / 2M business applications a|s
 * @version $Revision$ $Date$
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SolrSpringIntegration {
    private EmbeddedSolrServer server;
    private CoreContainer container;
    @Autowired
    private WebPageRepository webPageRepository;
    private Logger logger = LoggerFactory.getLogger(SolrSpringIntegration.class);

    @Test
    public void testWebPageRepository() {
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        List<WebPage> result = webPageRepository.findAllByText("*", sort);
        assertEquals("Dunkin’ Donuts declares the start of pumpkin spice season", result.get(4).getTitle());

        sort = new Sort(Sort.Direction.DESC, "id");
        result = webPageRepository.findAllByText("*", sort);
        assertEquals("Dunkin’ Donuts declares the start of pumpkin spice season", result.get(0).getTitle());
    }
}
