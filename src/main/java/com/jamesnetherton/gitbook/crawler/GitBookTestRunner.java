package com.jamesnetherton.gitbook.crawler;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class GitBookTestRunner {

    public static void main(String... args) throws Exception {
        CrawlConfig config = new CrawlConfig();
        config.setConnectionTimeout(CrawlerConfig.getConnectionTimeout());
        config.setCrawlStorageFolder(CrawlerConfig.getStorageFolder());
        config.setMaxConnectionsPerHost(CrawlerConfig.getMaxConnections());
        config.setMaxDepthOfCrawling(CrawlerConfig.getCrawlDepth());
        config.setPolitenessDelay(CrawlerConfig.getDelay());
        config.setIncludeBinaryContentInCrawling(false);
        config.setResumableCrawling(false);
        config.setSocketTimeout(CrawlerConfig.getSocketTimeout());

        PageFetcher pageFetcher = new PageFetcher(config);

        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        robotstxtConfig.setEnabled(false);
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);

        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
        controller.addSeed(CrawlerConfig.getCrawlUrl());
        controller.start(Crawler.class, CrawlerConfig.getMaxThreads());

        CrawlerRecorder recorder = CrawlerRecorder.getInstance();
        recorder.recordResults();
    }
}
