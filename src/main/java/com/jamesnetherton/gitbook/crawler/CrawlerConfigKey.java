package com.jamesnetherton.gitbook.crawler;

public enum CrawlerConfigKey {
    CONNECTION_TIMEOUT("10000"),
    CRAWL_DEPTH("2"),
    CRAWL_URL("http://localhost:4000"),
    DELAY("1500"),
    IGNORED_DOMAINS_PATH("ignored-domains.txt"),
    MAX_CONNECTIONS("1"),
    MAX_THREADS("5"),
    SOCKET_TIMEOUT("10000"),
    STORAGE_FOLDER("/tmp/crawldata");

    private String value;

    CrawlerConfigKey(String value) {
        this.value = value;
    }

    public String getValue() {
        String override = System.getenv(this.name());
        return override != null ? override : this.value;
    }
}
