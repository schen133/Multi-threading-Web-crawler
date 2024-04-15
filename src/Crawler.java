public class Crawler extends Thread {

    public enum CrawlType {
        CRAWL_PAGE_URL, CRAWL_INITIAL_TOPIC_URL
    }

    private Topic topic;
    private CrawlType crawlType;
    private String pageUrl;
    private String prefixUrl = "https://www.cochranelibrary.com";

    public Crawler(Topic topic, CrawlType crawlType) {
        this.topic = topic;
        this.crawlType = crawlType;
    }

    // for page url crawling
    public Crawler(Topic topic, CrawlType crawlType, String pageUrl) {
        this.topic = topic;
        this.crawlType = crawlType;
        this.pageUrl = pageUrl;
    }

    @Override
    public void run() {
        try {
            switch (crawlType) {
                case CRAWL_INITIAL_TOPIC_URL:
                    CrawlerUtils.crawlTopicPage(topic);
                    System.out.println("Crawling initial topic page: " + topic.getTopicName() + " finished.");
                    break;
                case CRAWL_PAGE_URL:
                    CrawlerUtils.setUpReviewsOfCurrentPage(topic, pageUrl);
                    System.out.println("Crawling individual page for: " + topic.getTopicName() + " finished.");
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
