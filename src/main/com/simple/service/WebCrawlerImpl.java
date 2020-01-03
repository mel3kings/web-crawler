package com.simple.service;

import com.simple.data.Node;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.io.*;
import java.util.stream.Collectors;

@Service
public class WebCrawlerImpl implements WebCrawler {
    private Set<String> visitedURLs = new HashSet<>();
    public static int MAX_LEVEL = 1;

    private static int processed = 0;
    public Node BFS(java.util.Queue<Node> queue, Node currentNode, int level) {
        System.out.println("Current level : " + level);
        if (level > MAX_LEVEL) {
            return BFS(queue, queue.poll(), --level);
        }
        if (queue.isEmpty()) {
            return currentNode;
        }

        Node processNode = queue.poll();

        Set<String> newLinks = getLinksFromURI(processNode.getURI());
        List<Node> associate = new ArrayList<>();
        for (String link : newLinks) {
            System.out.println("link for level " + level + " " + link);
            if (visitedURLs.contains(link)) {
                continue;
            }
            visitedURLs.add(link);
            Node tempNode = new Node();
            tempNode.setURI(link);
            if (processed < 5) {
                processed++;
                queue.offer(tempNode);
            }
            associate.add(tempNode);
            processNode.setNodes(associate);
        }

        return BFS(queue, processNode, ++level);
    }

    public Set<String> getLinksFromURI(String URI) {
        HashSet<String> links = new HashSet<>();
        System.out.println("Visiting website:: " + URI);
        HttpURLConnection con = null;
        try {
            URL url = new URL(URI);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            if (con.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return new HashSet<>();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                links.addAll(getLinks(inputLine));
            }
            br.close();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return new HashSet<>();
        } finally {
            if (null != con) {
                con.disconnect();
            }
        }
        return links;
    }

    public List<String> getLinks(String HTMLBody) {
        if (null == HTMLBody) {
            return new ArrayList<>();
        }
        Document doc = Jsoup.parse(HTMLBody);
        Elements links = doc.select("a");
        final List<String> urls = links.parallelStream()
                .map(a -> processURL(a.attr("href")))
                .filter(href -> href.length() > 0)
                .filter(WebCrawlerImpl::checkValidUrl)
                .collect(Collectors.toList());
        return urls;
    }


    private static boolean checkValidUrl(String uri) {
        if (uri.contains("http")) {
            return true;
        }
        return false;
    }

    private String processURL(String theURL) {
        int endPos;
        if (theURL.indexOf("?") > 0) {
            endPos = theURL.indexOf("?");
        } else if (theURL.indexOf("#") > 0) {
            endPos = theURL.indexOf("#");
        } else {
            endPos = theURL.length();
        }
        return theURL.substring(0, endPos);
    }
}
