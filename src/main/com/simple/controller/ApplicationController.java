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
        Stack<String> urls = new Stack<>();
        urls.add(url);
//        List<String> listOfHref = webCrawler.getLinksFromURI(url);
        Node a = new Node();
        a.setURI(url);
        a.setTitle("ROOT");
     //  a.setParentNode(null);
       List<Node> children = new ArrayList<>();
        Node child = new Node();
        child.setTitle("CHILD");
       // child.setParentNode(a);
        children.add(child);
        a.setNodes(children);
        Queue<Node> queue = new LinkedList<>();
        queue.add(a);

        Node result = webCrawler.WebCrawl(queue, a, 1);

//        ArrayList<Node> list = new ArrayList();
//        list.add(a);
        return a;
    }

}
