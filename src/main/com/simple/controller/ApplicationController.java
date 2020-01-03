package com.simple.controller;

import com.simple.data.Node;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@Slf4j
public class ApplicationController {



    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public List<Node> get(@RequestParam("url") String url) {
        Node a = new Node();
        a.setTitle(url);
        ArrayList<Node> list = new ArrayList();
        list.add(a);
        return list;
    }

}
