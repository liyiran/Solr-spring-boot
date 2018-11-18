package index.service;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.Suggestion;

import java.io.IOException;
import java.util.List;

/**
 * @author Yiran Li / 2M business applications a|s
 * @version $Revision$ $Date$
 */
public interface SolrService {
    List<Suggestion> getSuggestion(String text) throws IOException, SolrServerException;
}
