package yiran;/*
 * $Id$
 *
 * Copyright (c) 2003, 2004 WorldTicket A/S
 * All rights reserved.
 */

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import yiran.entity.WebPage;
import yiran.repository.WebPageRepository;

import java.util.List;

/**
 * @author Yiran Li / 2M business applications a|s
 * @version $Revision$ $Date$
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SolrSpringIntegration {
    @Autowired
    private WebPageRepository webPageRepository;

    @Test
    public void testWebPageRepository() {
        List<WebPage> result = webPageRepository.findAllByText("1236");
        assertEquals(9, result.size());
    }
}
