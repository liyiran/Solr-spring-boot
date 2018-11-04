/*
 * $Id$
 *
 * Copyright (c) 2003, 2004 WorldTicket A/S
 * All rights reserved.
 */
package index;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Yiran Li / 2M business applications a|s
 * @version $Revision$ $Date$
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean(name = "urlMap")
    public Map<String, String> getURLMap() throws IOException {
        Reader in = new FileReader("src/main/resources/URLtoHTML_mercury.csv");
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
        Map<String, String> urlFileMap = new HashMap<>();
        for (CSVRecord record : records) {
            urlFileMap.put(record.get(0), record.get(1));
        }
        return urlFileMap;
    }
}
