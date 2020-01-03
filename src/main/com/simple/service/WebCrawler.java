package com.simple.service;

import com.simple.data.Node;


public interface WebCrawler {
    Node BFS(java.util.Queue<Node> queue, Node currentNode, int level);
}
