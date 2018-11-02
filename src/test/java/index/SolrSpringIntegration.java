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
        List<WebPage> result = webPageRepository.findAllByText("1236");
        assertEquals(9, result.size());
    }
//    @BeforeAll
//    public void setup() {
//        this.container = new CoreContainer("testdata/solr");
//        this.container.load();
//
//        this.server = new EmbeddedSolrServer(container, "collection1");
//
//
//        try {
//            server.deleteByQuery("*:*");
//            server.commit();
//        } catch (SolrServerException e) {
//            logger.error("error", e);
//        } catch (IOException e) {
//            logger.error("error", e);
//        }
//
//        SolrInputDocument document = new SolrInputDocument();
//        document.addField("name", "bob");
//        document.addField("text", "Lorem ipsum");
//        document.addField("id", "" + System.currentTimeMillis());
//
//        try {
//            server.add(document);
//            server.commit();
//        } catch (SolrServerException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
