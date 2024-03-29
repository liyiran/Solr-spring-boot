/*
 * $Id$
 *
 * Copyright (c) 2003, 2004 WorldTicket A/S
 * All rights reserved.
 */
package graph;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Yiran Li / 2M business applications a|s
 * @version $Revision$ $Date$
 */
public class GraphGenerator {
    private static Logger logger = LoggerFactory.getLogger(GraphGenerator.class);

    public static void main(String... args) throws IOException {
        File dir = new File("/Users/liyiran/solr-7.5.0/server/solr/myexample/mercurynews");
        Reader in = new FileReader("src/main/resources/URLtoHTML_mercury.csv");
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
        BiMap<String, String> urlFileMap = HashBiMap.create();
        Set<String> edgeSet = new HashSet<>();
        double totalLink = 0;
        for (CSVRecord record : records) {
            urlFileMap.put(record.get(0), record.get(1));
        }
        logger.info("size" + urlFileMap.size());
        PrintWriter writer = new PrintWriter("edgeList.txt");
        for (File file : dir.listFiles()) {
            if(!file.getName().endsWith(".html")) {
                logger.info(file.getName());
                continue;
            }
            Document doc = Jsoup.parse(file, "UTF-8", urlFileMap.get(file.getName()));
            Elements links = doc.select("a[href]");
            totalLink += links.size();
//            logger.info("links: " + links.size());
            for(Element link : links) {
                String url = link.attr("href").trim();
                if(urlFileMap.inverse().containsKey(url)) {
//                    logger.info(file.getName() + " " + urlFileMap.inverse().get(url));
                    edgeSet.add(file.getName() + " " + urlFileMap.inverse().get(url));
                }
            }
        }
        for (String edge : edgeSet) {
            writer.println(edge);
        }
        writer.flush();
        writer.close();
        logger.info("total link: " +totalLink);
    }
}
