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
        String timeout = getEnvironmentVariable(CrawlerConfigKey.CONNECTION_TIMEOUT);
        return Integer.parseInt(timeout);
    }

    public static int getCrawlDepth() {
        String depth = getEnvironmentVariable(CrawlerConfigKey.CRAWL_DEPTH);
        return Integer.parseInt(depth);
    }

    public static String getCrawlUrl() {
        return getEnvironmentVariable(CrawlerConfigKey.CRAWL_URL);
    }

    public static int getDelay() {
        String delay = getEnvironmentVariable(CrawlerConfigKey.DELAY);
        return Integer.parseInt(delay);
    }

    public static List<String> getExcludedDomains() {
        if (excludedDomains == null) {
            loadExcludedDomains();
        }
        return excludedDomains;
    }

    public static String getIgnoredDomainsPath() {
        return getEnvironmentVariable(CrawlerConfigKey.IGNORED_DOMAINS_PATH);
    }

    public static int getMaxConnections() {
        String maxConnections = getEnvironmentVariable(CrawlerConfigKey.MAX_CONNECTIONS);
        return Integer.parseInt(maxConnections);
    }

    public static int getMaxThreads() {
        String maxThreads = getEnvironmentVariable(CrawlerConfigKey.MAX_THREADS);
        return Integer.parseInt(maxThreads);
    }

    public static int getSocketTimeout() {
        String timeout = getEnvironmentVariable(CrawlerConfigKey.SOCKET_TIMEOUT);
        return Integer.parseInt(timeout);
    }

    public static String getStorageFolder() {
        return getEnvironmentVariable(CrawlerConfigKey.STORAGE_FOLDER);
    }

    private static String getEnvironmentVariable(CrawlerConfigKey configKey) {
        String value = System.getenv(configKey.name());
        return value != null ? value : configKey.getValue();
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
