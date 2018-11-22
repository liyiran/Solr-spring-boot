/*
 * $Id$
 *
 * Copyright (c) 2003, 2004 WorldTicket A/S
 * All rights reserved.
 */
package index.repository;

import index.entity.WebPage;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Yiran Li / 2M business applications a|s
 * @version $Revision$ $Date$
 */
public interface WebPageRepository extends SolrCrudRepository<WebPage, String> {
    @Query("_text_:?0")
    List<WebPage> findAllByText(String text, Sort sort);
    Optional<WebPage> findById(String id);
}
