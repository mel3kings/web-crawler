package com.simple.controller;

import com.simple.data.Node;
import com.simple.service.WebCrawler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@Slf4j
public class ApplicationController {

    @Autowired
    private WebCrawler webCrawler;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public Node get(@RequestParam("url") String url) {
        Node root = new Node();
        root.setURI(url);
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        webCrawler.webCrawl(queue, root, 1);
        return root;
    }

}
