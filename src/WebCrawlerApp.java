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
        for (Topic topic : topics) {
            System.out.print(topic.getTopicName() + " | ");
        }
        System.out.println("\n");
        
        
        for (Topic topic : topics) {
            // CrawlerUtils.crawlTopicPage(topic);
            Crawler crawler = new Crawler(topic);
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
        System.out.println("All crawlers are done.");

        // Crawler.crawlTopicPage(topics.get(0));
        long endTime = System.currentTimeMillis();
        double duration = (endTime - startTime) / 1000.0;
        System.out.println("Time taken to crawl all topic page: " + duration + " seconds");
        System.out.println("total topics: " + topics.size());

        // this gets all pagination urls for one topic

        // Crawler.crawlTopicPage(topics.get(0));

        // let's do a multi-thread here to get visit all topic pages

        // and fill up all

        // crawl for 10 times, once that's done, we have urls for each review

        // 5 pages each topic, 20 topics
        // we should crawl separate for each topic?
        // let's do each topic, since that's our function's logic
        // or should we crawl each topic one by one, but start threads on each page

        // if each topic's pages are very different, then it would be better
        // multithread the pages

        // but if all are almost the same, we can just multithread each topic
        //
        // each page has

        File file = new File("cochrane_reviews.txt");
        writeToFile(topics, file);
    }

    //
    // public String parseTime

    public static void writeToFile(ArrayList<Topic> topics, File file) {
        Topic topic = topics.get(0);
        ArrayList<Review> reviews = topic.getReviews();

        try {
            FileWriter writer = new FileWriter(file);
            writer.write("testrun\n\n");
            writer.write("Topic: " + topic.getTopicName() + "\n\n");
            for (Review review : reviews) {
                String tempForReview = Parser.getDesiredReviewString(review);
                writer.write(tempForReview + "\n\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
