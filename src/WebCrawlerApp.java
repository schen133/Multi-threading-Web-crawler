import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.http.HttpClient;
import java.util.List;
import java.util.Scanner;
import java.util.HashMap;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class WebCrawlerApp {

    public static void main(String[] args) {
        String topicsEntryURL = "https://www.cochranelibrary.com/cdsr/reviews/topics";
        System.out.println("\nHi, this is a web crawler for Cochrane Library.");
        System.out.println("The multiple-threaded version is currently disabled due to inconsistent results.");
        System.out.println("\nEnter y to run the single-threaded version, or n to exit:");
        // ask for input
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if (input.equals("y")) {
            CloseableHttpClient client = HttpClients.createDefault();
            ArrayList<Topic> topics = CrawlerUtils.crawlHomePage(topicsEntryURL, client);
            System.out.println("\nHere are the topics available on the website: " + "\n");
            HashMap<Integer, Topic> topicMap = new HashMap<Integer, Topic>();
            int i = 0;
            for (Topic topic : topics) {
                topicMap.put(i, topic);
                System.out.print(i + ". " + topic.getTopicName() + " | ");
                i++;

            }

            System.out.println("\n\nPlease enter corresponding the index for the topic you want to crawl: ");
            String wantedTopic = scanner.nextLine();

            // check if program is terminated and print out current crawled
            Runtime.getRuntime().addShutdownHook(
                    new Thread(() -> System.out.println("\nProgram terminated. Reviews crawled: "
                            + topicMap.get(Integer.parseInt(wantedTopic)).getReviews().size())));

            if (topicMap.containsKey(Integer.parseInt(wantedTopic))) {
                singleRun(topics, topicMap.get(Integer.parseInt(wantedTopic)).getTopicName(), client);
                File file = new File("cochrane_reviews.txt");
                writeToFile(topics, file);
                System.out.println("All done! Please check the file cochrane_reviews.txt for the results.");
            } else {
                System.out.println("Invalid input. Exiting...");
            }
        } else {
            System.out.println("Exiting...");
        }
    }

    public static void singleRun(ArrayList<Topic> topics, String wantedTopic, CloseableHttpClient client) {
        // CloseableHttpClient client = HttpClients.createDefault();

        // ArrayList<Topic> topics = CrawlerUtils.crawlHomePage(topicsEntryURL, client);

        for (Topic topic : topics) {

            if (topic.getTopicName().equals(wantedTopic)) {
                CrawlerUtils.crawlTopicPage(topic, client);
                CrawlerUtils.crawlTopicPageNextButton(topic, client);
                System.out.println("Total reviews for topic: " + topic.getTopicName() + " is: "
                        + topic.getReviews().size() + "\n");
                break;
            }

        }

    }

    public static void test() {

        String topicsEntryURL = "https://www.cochranelibrary.com/cdsr/reviews/topics";
        CloseableHttpClient client = HttpClients.createDefault();

        // single url visit and crawl, get all topics' url and set up instances of them
        ArrayList<Topic> topics = CrawlerUtils.crawlHomePage(topicsEntryURL, client);

        long startTime = System.currentTimeMillis();
        System.out.println("Crawling topic page...");

        List<Crawler> crawlers = new ArrayList<Crawler>();
        // for (Topic topic : topics) {
        // System.out.print(topic.getTopicName() + " | ");
        // }
        // System.out.println("\n");

        for (Topic topic : topics) {
            // for (int i = 0; i < 1; i++) {
            // Topic topic = topics.get(i);
            // CrawlerUtils.crawlTopicPage(topic);
            Crawler crawler = new Crawler(topic, Crawler.CrawlType.CRAWL_INITIAL_TOPIC_URL, client);
            crawlers.add(crawler);
            crawler.start();

        }
        // // pause here until all threads are done
        for (Crawler crawler : crawlers) {
            try {
                crawler.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("All initial topic crawlers are done.");

        // Topic t1 = topics.get(0);
        // yo works but, t1.getFirstPageUrlString() doesn't work?

        // String yo =
        // "https://www.cochranelibrary.com/search?p_p_id=scolarissearchresultsportlet_WAR_scolarissearchresults&p_p_lifecycle=0&p_p_state=normal&p_p_mode=view&p_p_col_id=column-1&p_p_col_count=1&_scolarissearchresultsportlet_WAR_scolarissearchresults_displayText=Allergy+%26+intolerance&_scolarissearchresultsportlet_WAR_scolarissearchresults_searchText=Allergy+%26+intolerance&_scolarissearchresultsportlet_WAR_scolarissearchresults_searchType=basic&_scolarissearchresultsportlet_WAR_scolarissearchresults_facetQueryField=topic_id&_scolarissearchresultsportlet_WAR_scolarissearchresults_searchBy=13&_scolarissearchresultsportlet_WAR_scolarissearchresults_orderBy=displayDate-true&_scolarissearchresultsportlet_WAR_scolarissearchresults_facetDisplayName=Allergy+%26+intolerance&_scolarissearchresultsportlet_WAR_scolarissearchresults_facetQueryTerm=z1506030924307755598196034641807&_scolarissearchresultsportlet_WAR_scolarissearchresults_facetCategory=Topics";
        // String yo =
        // "https://www.cochranelibrary.com/en/c/portal/render_portlet?p_l_id=20759&p_p_id=scolarissearchresultsportlet_WAR_scolarissearchresults&p_p_lifecycle=0&p_t_lifecycle=0&p_p_state=normal&p_p_mode=view&p_p_col_id=column-1&p_p_col_pos=0&p_p_col_count=1&p_p_isolated=1&currentURL=%2Fsearch";
        // String yo = t1.getFirstPageUrlString();

        // System.out.println("first page url: " + yo);

        // CrawlerUtils.setUpReviewsOfCurrentPage(t1, t1.getFirstPageUrlString());
        // same client! YESSS
        // CrawlerUtils.setUpReviewsOfCurrentPage(t1, yo, client);

        // for each topic, go throughh all url
        for (Topic topic : topics) {
            ArrayList<String> pageURLS = topic.getPageURLS();
            List<Crawler> pageCrawlers = new ArrayList<Crawler>();
            for (String pageURL : pageURLS) {
                Crawler crawler = new Crawler(topic, Crawler.CrawlType.CRAWL_PAGE_URL,
                        pageURL, client);
                pageCrawlers.add(crawler);
                crawler.start();
            }
            // pause, wait for crawling of all pages
            // should be ok if order of review doesn't matter, we just simutanously crawl
            // each page of a topic and add reviews to it
            for (Crawler crawler : pageCrawlers) {
                try {
                    crawler.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out
                    .println("Total reviews for topic: " + topic.getTopicName() + " is: "
                            + topic.getReviews().size() + "\n");

        }
        // for (Topic topic : topics) {
        // ArrayList<String> pageURLS = topic.getPageURLS();
        // System.out.println("Total pages for topic: " + topic.getTopicName() + " is: "
        // + topic.getTotalPage());
        // for (String url : pageURLS) {
        // CrawlerUtils.setUpReviewsOfCurrentPage(topic, pageURLS.get(0), client);
        // }
        // System.out.println("Crawling for topic: " + topic.getTopicName() + " is done,
        // total reviews: "
        // + topic.getReviews().size());

        // }

        // ArrayList<Review> reviews = t1.getReviews();
        // ArrayList<String> pageURLS = t1.getPageURLS();
        // System.out.println(t1.getTotalPage());

        // for (String url : t1.getPageURLS()) {
        // CrawlerUtils.setUpReviewsOfCurrentPage(t1, url, client);
        // // System.out.println("this is all page urls");
        // // System.out.println(url);

        // }

        long endTime = System.currentTimeMillis();
        double duration = (endTime - startTime);

        System.out.println("All done! Time taken to crawl all topic page: " + duration + " seconds");

        File file = new File("cochrane_reviews.txt");
        writeToFile(topics, file);
    }

    public static void writeToFile(ArrayList<Topic> topics, File file) {
        try {
            FileWriter writer = new FileWriter(file);
            for (Topic topic : topics) {
                ArrayList<Review> reviews = topic.getReviews();
                for (Review review : reviews) {
                    String tempForReview = Parser.getDesiredReviewString(review);
                    writer.write(tempForReview + "\n\n");
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
