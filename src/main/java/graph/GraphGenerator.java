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
import java.io.Reader;

/**
 * @author Yiran Li / 2M business applications a|s
 * @version $Revision$ $Date$
 */
public class GraphGenerator {
    private static Logger logger = LoggerFactory.getLogger(GraphGenerator.class);

    public static void main(String... args) throws IOException {
        File dir = new File("src/main/resources/webpages");
        Reader in = new FileReader("src/main/resources/URLtoHTML_mercury.csv");
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
        BiMap<String, String> urlFileMap = HashBiMap.create();
        for (CSVRecord record : records) {
            urlFileMap.put(record.get(0), record.get(1));
        }
        logger.info("size" + urlFileMap.size());
        for (File file : dir.listFiles()) {
            Document doc = Jsoup.parse(file, "UTF-8", urlFileMap.get(file.getName()));
            Elements links = doc.select("a[href]");
            logger.info("links: " + links.size());
            for(Element link : links) {
                String url = link.attr("href").trim();
                if(urlFileMap.inverse().containsKey(url)) {
                    logger.info(file.getName() + " " + urlFileMap.inverse().get(url));
                }
            }
        }
    }
}
