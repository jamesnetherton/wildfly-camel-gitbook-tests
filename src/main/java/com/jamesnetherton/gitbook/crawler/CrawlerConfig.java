package com.jamesnetherton.gitbook.crawler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.jamesnetherton.gitbook.crawler.CrawlerLogger.log;

public class CrawlerConfig {

    private static List<String> excludedDomains;

    public static int getConnectionTimeout() {
        return Integer.parseInt(CrawlerConfigKey.CONNECTION_TIMEOUT.getValue());
    }

    public static int getCrawlDepth() {
        return Integer.parseInt(CrawlerConfigKey.CRAWL_DEPTH.getValue());
    }

    public static String getCrawlUrl() {
        return CrawlerConfigKey.CRAWL_URL.getValue();
    }

    public static int getDelay() {
        return Integer.parseInt(CrawlerConfigKey.DELAY.getValue());
    }

    public static List<String> getExcludedDomains() {
        if (excludedDomains == null) {
            loadExcludedDomains();
        }
        return excludedDomains;
    }

    public static String getIgnoredDomainsPath() {
        return CrawlerConfigKey.IGNORED_DOMAINS_PATH.getValue();
    }

    public static int getMaxConnections() {
        return Integer.parseInt(CrawlerConfigKey.MAX_CONNECTIONS.getValue());
    }

    public static int getMaxThreads() {
        return Integer.parseInt(CrawlerConfigKey.MAX_THREADS.getValue());
    }

    public static int getSocketTimeout() {
        return Integer.parseInt(CrawlerConfigKey.SOCKET_TIMEOUT.getValue());
    }

    public static String getStorageFolder() {
        return CrawlerConfigKey.STORAGE_FOLDER.getValue();
    }

    private static List<String> loadExcludedDomains() {
        String ignoredDomainsPath = getIgnoredDomainsPath();
        excludedDomains = new ArrayList<>();

        try (FileReader fr = new FileReader(ignoredDomainsPath);
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            while((line = (br.readLine())) != null) {
                if (!line.trim().equals("")) {
                    log.debug("Excluding domain {}", line);
                    excludedDomains.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            log.error("Error initializing excluded domains. {} does not exist", ignoredDomainsPath);
        } catch (IOException e) {
            log.error("Error reading excluded domains. {} does not exist", ignoredDomainsPath);
        }

        return excludedDomains;
    }
}
