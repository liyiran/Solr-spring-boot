/*
 * $Id$
 *
 * Copyright (c) 2003, 2004 WorldTicket A/S
 * All rights reserved.
 */
package yiran.repository;

import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;
import yiran.entity.WebPage;

import java.util.List;

/**
 * @author Yiran Li / 2M business applications a|s
 * @version $Revision$ $Date$
 */
public interface WebPageRepository extends SolrCrudRepository<WebPage, String> {
    @Query("_text_:*?0*")
    List<WebPage> findAllByText(String text);
}
