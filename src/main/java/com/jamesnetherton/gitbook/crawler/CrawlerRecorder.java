package com.jamesnetherton.gitbook.crawler;

import java.util.ArrayList;
import java.util.List;

public class CrawlerRecorder {
    private List<String> urls;
    private List<String> failures;
    private static CrawlerRecorder instance = new CrawlerRecorder();


    private CrawlerRecorder() {
        urls = new ArrayList<>();
        failures = new ArrayList<>();
    }

    public static CrawlerRecorder getInstance() {
        return instance;
    }

    public void recordUrl(String url) {
        synchronized (this) {
            urls.add(url);
        }
    }

    public void recordFailure(String url, String reason) {
        StringBuffer builder = new StringBuffer();
        builder.append("[").append(reason).append("] ").append(url);
        synchronized (this) {
            failures.add(builder.toString());
        }
    }

    public void recordFailure(String url, int statusCode) {
        StringBuffer builder = new StringBuffer();
        builder.append("[").append(statusCode).append("] ").append(url);
        synchronized (this) {
            failures.add(builder.toString());
        }
    }

    public void recordResults() {
        if (failures.size() > 0) {
            System.out.println("\nFAILURES:\n");
            for (String failure : failures) {
                System.out.println(failure);
            }
        }
    }

    public boolean urlVisited(String url) {
        synchronized (this) {
            return urls.contains(url);
        }
    }
}
