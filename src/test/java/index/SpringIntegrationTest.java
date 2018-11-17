/*
 * $Id$
 *
 * Copyright (c) 2003, 2004 WorldTicket A/S
 * All rights reserved.
 */
package index;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Yiran Li / 2M business applications a|s
 * @version $Revision$ $Date$
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringIntegrationTest {
    @LocalServerPort
    private int port;
    TestRestTemplate restTemplate = new TestRestTemplate();
    private Logger logger = LoggerFactory.getLogger(SpringIntegrationTest.class);

    @Test
//    @Ignore
    public void testRetrieveStudentCourse() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        File file = new File("resources/result_desc.txt");
        String[] queries = {"Donald Trump", "LA Lakers", "Star Wars", "Lebron James", "2018 World Cup", "North Korea", "Hurricane Florence", "Paul Allen"};
        for (String queryString : queries) {
            String query = String.format("/mercury/%s?sort=pageRankFile&direction=desc", queryString);
            ResponseEntity<List> response = restTemplate.exchange(
                    createURLWithPort(query),
                    HttpMethod.GET, entity, List.class);
            Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
//        response.getBody().stream().forEach(item->{logger.info(((LinkedHashMap)item).get("url").toString());});
            List<String> urls = (List<String>) response.
                    getBody().
                    stream().
                    map(item -> ((LinkedHashMap) item).get("url").toString()).
                    collect(Collectors.toList());
            FileUtils.writeStringToFile(file, queryString + "\n", Charset.defaultCharset(),true);
            FileUtils.writeStringToFile(file, urls.toString()+"\n", Charset.defaultCharset(), true);
        }
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

}
