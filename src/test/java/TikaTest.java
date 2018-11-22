/*
 * $Id$
 *
 * Copyright (c) 2003, 2004 WorldTicket A/S
 * All rights reserved.
 */

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Yiran Li / 2M business applications a|s
 * @version $Revision$ $Date$
 */
public class TikaTest {
    @Test
    public void testTika() throws IOException, TikaException, SAXException {
        //detecting the file type
        File dir = new File("/Users/liyiran/solr-7.5.0/server/solr/myexample/mercurynews");
        StringBuilder result = new StringBuilder();
        int counter = 0;
        for (File file : dir.listFiles()) {
            BodyContentHandler handler = new BodyContentHandler();
            ParseContext pcontext = new ParseContext();
            FileInputStream inputStream = new FileInputStream(file);
            //Html parser 
            HtmlParser htmlparser = new HtmlParser();
            Metadata metadata = new Metadata();
            htmlparser.parse(inputStream, handler, metadata, pcontext);
            result.append(handler.toString().replaceAll("[^a-zA-Z0-9]", " ").replaceAll("\\s+", " ")).append("\n");
//            System.out.println("Contents of the document:" + handler.toString().replaceAll("[^a-zA-Z0-9]", " ").replaceAll("\\s+", " "));
//            System.out.println("Metadata of the document:");
//            String[] metadataNames = metadata.names();
//
//            for(String name : metadataNames) {
//                System.out.println(name + ":   " + metadata.get(name));
//            }
            System.out.println(++counter);
        }
//        System.out.println(result);
        Files.write(Paths.get("big.txt"), result.toString().getBytes());
    }
}
