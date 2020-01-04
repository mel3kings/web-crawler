package com.simple.service;

import com.simple.data.Node;

import java.util.Queue;

public interface WebCrawler {
    Node WebCrawl(Queue<Node> queue, Node currentNode, int level);
}
