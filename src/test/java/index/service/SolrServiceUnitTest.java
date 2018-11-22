/*
 * $Id$
 *
 * Copyright (c) 2003, 2004 WorldTicket A/S
 * All rights reserved.
 */
package index.service;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Yiran Li / 2M business applications a|s
 * @version $Revision$ $Date$
 */
public class SolrServiceUnitTest {
    @Test
    public void testSnippetGeneration() {
        SolrServiceImpl solrServiceImpl = new SolrServiceImpl(null, null, null);
        String orginal = "Donald John Trump (born June 14, 1946) is the 45th and current President of the United States. " +
                "Before entering politics, he was a businessman and television personality.\n " +
                "Trump was born and raised in the New York City borough of Queens.";
        String result = solrServiceImpl.findSnippet(orginal, "John Trump");
        Assert.assertEquals("Donald John Trump (born June 14, 1946) is the 45th and current President of the United States",result);

        result = solrServiceImpl.findSnippet(orginal, "in the");
        Assert.assertEquals("Trump was born and raised in the New York City borough of Queens.",result);

        result = solrServiceImpl.findSnippet(orginal, "Donald Trump");
        Assert.assertEquals("Donald John Trump (born June 14, 1946) is the 45th and current President of the United States",result);


        result = solrServiceImpl.findSnippet(orginal, "Trump born");
        Assert.assertEquals("Donald John Trump (born June 14, 1946) is the 45th and current President of the United States",result);

    }
}
