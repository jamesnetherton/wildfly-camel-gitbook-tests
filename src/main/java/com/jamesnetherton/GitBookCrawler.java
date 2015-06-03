package com.jamesnetherton;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;

import java.util.ArrayList;
import java.util.List;

public class GitBookCrawler extends WebCrawler {

    private static List<String> crawledUrls = new ArrayList<String>();
    private static List<String> excludedPaths = new ArrayList<String>();
    private static List<String> failures = new ArrayList<String>();

    public GitBookCrawler() {
        excludedPaths.add("gitbook.com");
        excludedPaths.add("hostname");
        excludedPaths.add("console");
    }

    @Override
    protected void onUnexpectedStatusCode(String urlStr, int statusCode, String contentType, String description) {
        super.onUnexpectedStatusCode(urlStr, statusCode, contentType, description);

        StringBuilder builder = new StringBuilder();
        builder.append("[").append(statusCode).append("] ").append(urlStr);
        failures.add(builder.toString());
    }

    @Override
    public boolean shouldVisit(Page page, WebURL url) {
        boolean shouldVisit = true;
        String href = url.getURL().toLowerCase();

        if(!crawledUrls.contains(href) && (url.getParentUrl().equals("") || url.getParentUrl().contains("localhost"))) {
            for (String excludedHost : excludedPaths) {
                if (href.contains(excludedHost)) {
                    shouldVisit = false;
                    break;
                }
            }
        } else {
            shouldVisit = false;
        }

        crawledUrls.add(href);

        return shouldVisit;
    }

    @Override
    public void visit(Page page) {
        System.out.println(page.getWebURL().getURL());
    }

    public static void main(String[] args) throws Exception {
        String crawlStorageFolder = "/tmp/crawldata";
        int numberOfCrawlers = 5;

        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setMaxDepthOfCrawling(2);
        config.setConnectionTimeout(10000);
        config.setSocketTimeout(10000);
        config.setPolitenessDelay(1000);
        config.setIncludeBinaryContentInCrawling(false);
        config.setResumableCrawling(false);
        config.setMaxConnectionsPerHost(1);

        PageFetcher pageFetcher = new PageFetcher(config);

        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        robotstxtConfig.setEnabled(false);

        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
        controller.addSeed("http://localhost:4000");


        controller.start(GitBookCrawler.class, numberOfCrawlers);

        if (failures.size() > 0) {
            System.out.println("Failures:\n");
            for(String failure: failures) {
               System.out.println(failure);
            }
            System.exit(1);
        }
    }
}
