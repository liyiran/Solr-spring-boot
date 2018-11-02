/*
 * $Id$
 *
 * Copyright (c) 2003, 2004 WorldTicket A/S
 * All rights reserved.
 */
package index.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import index.entity.WebPage;
import index.repository.WebPageRepository;

import java.util.List;

/**
 * @author Yiran Li / 2M business applications a|s
 * @version $Revision$ $Date$
 */
@RestController
public class WebPageController {
    @Autowired
    private WebPageRepository webPageRepository;

    @GetMapping("/product/{text}")
    public List<WebPage> getAllWebPage(@PathVariable String text) {
        return webPageRepository.findAllByText(text);
    }
}
