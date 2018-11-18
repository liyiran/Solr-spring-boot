/*
 * $Id$
 *
 * Copyright (c) 2003, 2004 WorldTicket A/S
 * All rights reserved.
 */
package index.service;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.SuggesterResponse;
import org.apache.solr.client.solrj.response.Suggestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author Yiran Li / 2M business applications a|s
 * @version $Revision$ $Date$
 */
@Service
public class SolrServiceImpl implements SolrService {
    @Autowired
    private HttpSolrClient client;

    @Override
    public List<Suggestion> getSuggestion(String text) throws IOException, SolrServerException {
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setRequestHandler("/myexample/suggest");
        solrQuery.setQuery(text);
        SuggesterResponse suggesterResponse = client.query(solrQuery).getSuggesterResponse();
        return suggesterResponse.getSuggestions().get("suggest");
    }
}
