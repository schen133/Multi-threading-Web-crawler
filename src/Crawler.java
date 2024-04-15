public class Crawler extends Thread {

    private Topic topic;

    public Crawler(Topic topic) {
        this.topic = topic;
    }

    @Override
    public void run() {
        // CrawlerUtils.crawlTopicPage(topic);

        try {
            CrawlerUtils.crawlTopicPage(topic);
            System.out.println("Crawling topic: " + topic.getTopicName() + " is done.");

        } catch (Exception e) {
            System.out.println("Error crawling topic: " + topic.getTopicName() + ".");
        }



    }

}
