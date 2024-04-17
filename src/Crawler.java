import java.net.http.HttpClient;

import org.apache.http.impl.client.CloseableHttpClient;

public class Crawler extends Thread {

    public enum CrawlType {
        CRAWL_PAGE_URL, CRAWL_INITIAL_TOPIC_URL
    }

    public Topic topic;
    private CrawlType crawlType;
    private String pageUrl;
    private String prefixUrl = "https://www.cochranelibrary.com";
    private CloseableHttpClient client;

    public Crawler(Topic topic, CrawlType crawlType, CloseableHttpClient client) {
        this.topic = topic;
        this.crawlType = crawlType;
        this.client = client;

    }

    // for page url crawling
    public Crawler(Topic topic, CrawlType crawlType, String pageUrl, CloseableHttpClient client) {
        this.topic = topic;
        this.crawlType = crawlType;
        this.pageUrl = pageUrl;
        this.client = client;
    }

    @Override
    public void run() {
        try {
            switch (crawlType) {
                case CRAWL_INITIAL_TOPIC_URL:
                    CrawlerUtils.crawlTopicPage(topic, client);
                    System.out.println("Crawling initial topic page: " + topic.getTopicName() + " finished.");
                    System.out.println(topic.getTopicName() + " has " + topic.getTotalPage() + " pages." + "\n");
                    break;
                case CRAWL_PAGE_URL:
                    CrawlerUtils.setUpReviewsOfCurrentPage(topic, pageUrl, client);
                    // System.out.println("Crawling individual page for: " + topic.getTopicName() + " finished.");
                default:
                    break;
            }

        } catch (Exception e) {
            // System.out.println(e
            System.out.println("Error crawling topic: " + topic.getTopicName() + ".");
            throw new RuntimeException(e);
        }
    }
}
