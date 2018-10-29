/*
 * $Id$
 *
 * Copyright (c) 2003, 2004 WorldTicket A/S
 * All rights reserved.
 */

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @author Yiran Li / 2M business applications a|s
 * @version $Revision$ $Date$
 */
public class SolrTest {
    @Test
    public void testSolr() throws IOException, SolrServerException {
        String urlString = "http://localhost:8983/solr/myexample";
        HttpSolrClient solr = new HttpSolrClient.Builder(urlString).build();
        solr.setParser(new XMLResponseParser());
        SolrQuery query = new SolrQuery();
        query.setQuery("*:*");
        QueryResponse response = solr.query(query);
        assertEquals(11594, response.getResults().getNumFound());
    }
}
