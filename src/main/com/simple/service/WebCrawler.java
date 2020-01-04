package com.simple.service;

import com.simple.data.Node;

import java.util.Queue;
import java.util.Set;

public interface WebCrawler {
    Node webCrawl(Queue<Node> queue, Node currentNode, int level);

    Set<String> getLinks(StringBuffer HTMLBody);

    String getTitle(StringBuffer HTMLBody);

    boolean checkValidUrl(String URL);
}
