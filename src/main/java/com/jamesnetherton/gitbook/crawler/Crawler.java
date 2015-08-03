package com.jamesnetherton.gitbook.crawler;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;

import static com.jamesnetherton.gitbook.crawler.CrawlerLogger.log;

public class Crawler extends WebCrawler {

    private CrawlerRecorder recorder = CrawlerRecorder.getInstance();

    @Override
    protected void onContentFetchError(WebURL webUrl) {
        super.onContentFetchError(webUrl);
        recorder.recordFailure(webUrl.getURL(), "CONTENT FETCH ERROR");
    }

    @Override
    protected void onParseError(WebURL webUrl) {
        super.onParseError(webUrl);
        recorder.recordFailure(webUrl.getURL(), "PARSE ERROR");
    }

    @Override
    protected void onUnexpectedStatusCode(String urlStr, int statusCode, String contentType, String description) {
        super.onUnexpectedStatusCode(urlStr, statusCode, contentType, description);
        recorder.recordFailure(urlStr, statusCode);
    }

    @Override
    public boolean shouldVisit(Page page, WebURL url) {
        boolean shouldVisit = true;
        String href = url.getURL().toLowerCase();

        if (href.contains("localhost")) {
            return true;
        }

        if (!recorder.urlVisited(href) && (url.getParentUrl().equals("") || url.getParentUrl().contains("localhost"))) {
            for (String domain : CrawlerConfig.getExcludedDomains()) {
                if (href.contains(domain)) {
                    log.debug("Banned domain {}", domain);
                    shouldVisit = false;
                    break;
                }
            }
        } else {
            log.debug("Visited {}", href);
            shouldVisit = false;
        }

        recorder.recordUrl(href);

        return shouldVisit;
    }

    @Override
    public void visit(Page page) {
        log.info(page.getWebURL().getURL());
    }
}
