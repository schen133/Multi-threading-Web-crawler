import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class WebCrawlerApp {

    public static void main(String[] args) {
        // Crawler.crawlTopicPage(
        // "https://www.cochranelibrary.com/en/search?p_p_id=scolarissearchresultsportlet_WAR_scolarissearchresults&p_p_lifecycle=0&p_p_state=normal&p_p_mode=view&p_p_col_id=column-1&p_p_col_count=1&_scolarissearchresultsportlet_WAR_scolarissearchresults_displayText=Allergy+%26+intolerance&_scolarissearchresultsportlet_WAR_scolarissearchresults_searchText=Allergy+%26+intolerance&_scolarissearchresultsportlet_WAR_scolarissearchresults_searchType=basic&_scolarissearchresultsportlet_WAR_scolarissearchresults_facetQueryField=topic_id&_scolarissearchresultsportlet_WAR_scolarissearchresults_searchBy=13&_scolarissearchresultsportlet_WAR_scolarissearchresults_orderBy=displayDate-true&_scolarissearchresultsportlet_WAR_scolarissearchresults_facetDisplayName=Allergy+%26+intolerance&_scolarissearchresultsportlet_WAR_scolarissearchresults_facetQueryTerm=z1506030924307755598196034641807&_scolarissearchresultsportlet_WAR_scolarissearchresults_facetCategory=Topics");
        test();
    }

    public static void test() {

        String topicsEntryURL = "https://www.cochranelibrary.com/cdsr/reviews/topics";

        // single url visit and crawl, get all topics' url and set up instances of them
        ArrayList<Topic> topics = CrawlerUtils.crawlHomePage(topicsEntryURL);

        long startTime = System.currentTimeMillis();
        System.out.println("Crawling topic page...");

        List<Crawler> crawlers = new ArrayList<Crawler>();
        // for (Topic topic : topics) {
        // System.out.print(topic.getTopicName() + " | ");
        // }
        // System.out.println("\n");

        // for (Topic topic : topics) {
        for (int i = 0; i < 2; i++) {
            Topic topic = topics.get(i);
            // CrawlerUtils.crawlTopicPage(topic);
            Crawler crawler = new Crawler(topic, Crawler.CrawlType.CRAWL_INITIAL_TOPIC_URL);
            crawlers.add(crawler);
            crawler.start();

        }
        // pause here until all threads are done
        for (Crawler crawler : crawlers) {
            try {
                crawler.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("All initial topic crawlers are done.");

        Topic t1 = topics.get(0);
        String yo = "https://www.cochranelibrary.com/search?p_p_id=scolarissearchresultsportlet_WAR_scolarissearchresults&p_p_lifecycle=0&p_p_state=normal&p_p_mode=view&p_p_col_id=column-1&p_p_col_count=1&_scolarissearchresultsportlet_WAR_scolarissearchresults_displayText=Allergy+%26+intolerance&_scolarissearchresultsportlet_WAR_scolarissearchresults_searchText=Allergy+%26+intolerance&_scolarissearchresultsportlet_WAR_scolarissearchresults_searchType=basic&_scolarissearchresultsportlet_WAR_scolarissearchresults_facetQueryField=topic_id&_scolarissearchresultsportlet_WAR_scolarissearchresults_searchBy=13&_scolarissearchresultsportlet_WAR_scolarissearchresults_orderBy=displayDate-true&_scolarissearchresultsportlet_WAR_scolarissearchresults_facetDisplayName=Allergy+%26+intolerance&_scolarissearchresultsportlet_WAR_scolarissearchresults_facetQueryTerm=z1506030924307755598196034641807&_scolarissearchresultsportlet_WAR_scolarissearchresults_facetCategory=Topics";
        // CrawlerUtils.setUpReviewsOfCurrentPage(t1, t1.getFirstPageUrlString());
        CrawlerUtils.setUpReviewsOfCurrentPage(t1, yo);

        // for (Topic topic : topics) {
        // for (int i = 0; i < 1; i++) {
        // Topic topic = topics.get(i);
        // // for each url
        // ArrayList<String> pageURLS = topic.getPageURLS();
        // List<Crawler> pageCrawlers = new ArrayList<Crawler>();
        // for (String pageURL : pageURLS) {
        // Crawler crawler = new Crawler(topic, Crawler.CrawlType.CRAWL_PAGE_URL,
        // pageURL);
        // pageCrawlers.add(crawler);
        // crawler.start();
        // }
        // // pause, wait for crawling of all pages
        // // should be ok if order of review doesn't matter, we just simutanously crawl
        // // each page of a topic and add reviews to it
        // for (Crawler crawler : pageCrawlers) {
        // try {
        // crawler.join();
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }
        // }
        // System.out
        // .println("All page crawlers for topic: " + topic.getTopicName() + " are done.
        // Reviews are setup!");
        // }

        long endTime = System.currentTimeMillis();
        double duration = (endTime - startTime) / 1000.0;
        System.out.println("Time taken to crawl all topic page: " + duration + " seconds");
        System.out.println("total topics: " + topics.size());

        // now for each topic, for each page in pageURLS, start a thread to crawl

        File file = new File("cochrane_reviews.txt");
        writeToFile(topics, file);
    }

    public static void writeToFile(ArrayList<Topic> topics, File file) {
        try {
            FileWriter writer = new FileWriter(file);
            for (Topic topic : topics) {
                ArrayList<Review> reviews = topic.getReviews();
                writer.write("Topic: " + topic.getTopicName() + "\n\n");
                for (Review review : reviews) {
                    String tempForReview = Parser.getDesiredReviewString(review);
                    writer.write(tempForReview + "\n\n");
                }
            }
            System.out.println("file written to: " + file.getAbsolutePath() + " successfully");
            
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
