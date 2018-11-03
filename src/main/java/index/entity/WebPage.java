/*
 * $Id$
 *
 * Copyright (c) 2003, 2004 WorldTicket A/S
 * All rights reserved.
 */
package index.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

/**
 * @author Yiran Li / 2M business applications a|s
 * @version $Revision$ $Date$
 */
@SolrDocument(solrCoreName = "myexample")
public class WebPage {
    @Id
    @Indexed(name = "id", type = "string")
    private String id;

    @Indexed(name = "title", type = "string")
    private String title;

    @Indexed(name = "og_url", type = "string")
    private String url;

    @Indexed(name = "og_site_name", type = "string")
    private String siteName;

    @Indexed(name = "article_published_time", type = "pdates")
    private String publishedDateTime;

    @Indexed(name = "og_description", type = "pdates")
    private String description;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getPublishedDateTime() {
        return this.publishedDateTime;
    }

    public void setPublishedDateTime(String publishedDateTime) {
        this.publishedDateTime = publishedDateTime.toString();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
