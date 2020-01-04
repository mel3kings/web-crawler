package com.simple.controller;

import com.simple.service.WebCrawler;
import com.simple.service.WebCrawlerImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertFalse;

public class WebCrawlerTest {
    private WebCrawler webCrawler;

    @Before
    public void init() {
        webCrawler = new WebCrawlerImpl();
    }

    @Test
    public void testValidHtml() {
        StringBuffer html = new StringBuffer("<html> <body> <a href='http://google.com'>google</a>" +
                "<a href='https://au.yahoo.com'>yahoo</a> <li> <a href='http://google.com'> goolge again</a></li> " +
                "</body></html>");
        Set<String> linksSet = webCrawler.getLinks(html);
        assert (linksSet.size() == 2);
        assert (linksSet.contains("https://au.yahoo.com"));
    }

    @Test
    public void testinvalidUrlHtml() {
        StringBuffer html = new StringBuffer("<html> <body> <a href='http://facebook.com'>facebook</a>" +
                "<a href='asdsafafa.xasxa'>yahoo</a> <li> <a href='http://google.com'> goolge again</a></li> " +
                "</body></html>");
        Set<String> linksSet = webCrawler.getLinks(html);
        assert (linksSet.size() == 2);
        assert (linksSet.contains("http://google.com"));
        assertFalse(linksSet.contains("asdsafafa.xasxa"));

    }

    @Test
    public void testReallyNestedUrl() {
        StringBuffer html = new StringBuffer("<html> <body><p><ul><li>" +
                "<a href='http://foo.com'>foo</a></li></ul></p> </body></html>");
        Set<String> linksSet = webCrawler.getLinks(html);
        assert (linksSet.size() == 1);
        assert (linksSet.contains("http://foo.com"));
    }

    @Test
    public void testNoUrlinHref() {
        StringBuffer html = new StringBuffer("<html> <body><p><ul><li><a>foo</a></li></ul></p> </body></html>");
        Set<String> linksSet = webCrawler.getLinks(html);
        assert (linksSet.size() == 0);
    }

     @Test
    public void testBlankUrlinHref() {
        StringBuffer html = new StringBuffer("<html> <body><p><ul><li><a href=''>foo</a></li></ul></p> </body></html>");
        Set<String> linksSet = webCrawler.getLinks(html);
        assert (linksSet.size() == 0);
    }

    @Test
    public void testGetTitle() {
        StringBuffer html = new StringBuffer("<html><title>Google</title>" +
                " <body><p><ul><li><a href=''>foo</a></li></ul></p> </body></html>");
        String title = webCrawler.getTitle(html);
        assert (title.equals("Google"));
    }

    @Test
    public void testNormalText() {
        StringBuffer html = new StringBuffer("Random Text");
        Set<String> linksSet = webCrawler.getLinks(html);
        assert (linksSet.size() == 0);
    }

    @Test
    public void testEdgeCases() {
        StringBuffer html = new StringBuffer("");
        Set<String> linksSet = webCrawler.getLinks(html);
        assert (linksSet.size() == 0);

        html = null;
        linksSet = webCrawler.getLinks(html);
        assert (linksSet.size() == 0);
    }
}
