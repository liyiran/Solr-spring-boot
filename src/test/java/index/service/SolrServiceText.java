/*
 * $Id$
 *
 * Copyright (c) 2003, 2004 WorldTicket A/S
 * All rights reserved.
 */
package index.service;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.tika.exception.TikaException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @author Yiran Li / 2M business applications a|s
 * @version $Revision$ $Date$
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SolrServiceText {
    @Autowired
    private HttpSolrClient client;
    @Autowired
    private SolrService solrService;

    @Test
    public void testSuggest() {
        String result = solrService.correct("acess");
        Assert.assertEquals("access", result);
    }

    @Test
    public void testSnippet() throws IOException, TikaException {
        String result = solrService.getSnippet("http://www-scf.usc.edu/~liyiran/a4.html");
        System.out.println(result);
    }
}
