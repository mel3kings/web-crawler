package com.simple.service;

import com.simple.data.Node;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
    public static int MAX_DEPTH = 2;
    private static int MAX_NODES_VISIT = 20;
    private static int processed = 0;

    public Node WebCrawl(java.util.Queue<Node> queue, Node currentNode, int level) {
        if (level > MAX_DEPTH) {
            return WebCrawl(queue, queue.poll(), --level);
        }
        if (queue.isEmpty()) {
            return currentNode;
        }
        Node processNode = queue.poll();
        StringBuffer htmlBody = getHtmlBody(processNode);
        processNode.setTitle(getTitle(htmlBody));
        Set<String> newLinks = getLinks(htmlBody);
        List<Node> associate = new ArrayList<>();
        for (String link : newLinks) {
            if (visitedURLs.contains(link)) {
                continue;
            }
            visitedURLs.add(link);
            Node tempNode = new Node();
            tempNode.setURI(link);
            if (processed < MAX_NODES_VISIT) {
                processed++;
                queue.offer(tempNode);
            }
            associate.add(tempNode);
            processNode.setNodes(associate);
        }

        return WebCrawl(queue, processNode, ++level);
    }

    public StringBuffer getHtmlBody(Node processNode) {
        System.out.println("Visiting website:: " + processNode.getURI());
        HttpURLConnection con = null;
        StringBuffer htmlBody = new StringBuffer();
        try {
            URL url = new URL(processNode.getURI());
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            if (con.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return htmlBody;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                htmlBody.append(inputLine);
            }
            br.close();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return htmlBody;
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
        return htmlBody;
    }

    public Set<String> getLinks(StringBuffer HTMLBody) {
        if (null == HTMLBody) {
            return new HashSet<>();
        }
        Document doc = Jsoup.parse(HTMLBody.toString());
        Elements links = doc.select("a");
        return links.parallelStream()
                .map(a -> a.attr("href"))
                .filter(href -> href.length() > 0)
                .filter(WebCrawlerImpl::checkValidUrl)
                .collect(Collectors.toSet());
    }

    public String getTitle(StringBuffer HTMLBody) {
        if (null == HTMLBody) {
            return "";
        }
        Document doc = Jsoup.parse(HTMLBody.toString());
        Element title = doc.selectFirst("title");
        if (null == title) {
            return "";
        }
        return title.html();
    }

    public static boolean checkValidUrl(String uri) {
        if (uri.contains("http")) {
            return true;
        }
        return false;
    }

}
