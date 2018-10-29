/*
 * $Id$
 *
 * Copyright (c) 2003, 2004 WorldTicket A/S
 * All rights reserved.
 */
package yiran;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

/**
 * @author Yiran Li / 2M business applications a|s
 * @version $Revision$ $Date$
 */
@SpringBootConfiguration
@EnableSolrRepositories(
        basePackages = {"yiran.repository"}
)
@ComponentScan
public class SolrConfig {
    @Value("${spring.data.solr.host}")
    String solrURL;

    @Bean
    public SolrClient solrClient() {
        return new HttpSolrClient.Builder(solrURL).build();
    }

    @Bean
    public SolrTemplate solrTemplate(SolrClient client) throws Exception {
        return new SolrTemplate(client);
    }

}
