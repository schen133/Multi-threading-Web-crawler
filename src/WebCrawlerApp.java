import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WebCrawlerApp {

    public static void main(String[] args) {
        // Crawler.crawlTopicPage(
        // "https://www.cochranelibrary.com/en/search?p_p_id=scolarissearchresultsportlet_WAR_scolarissearchresults&p_p_lifecycle=0&p_p_state=normal&p_p_mode=view&p_p_col_id=column-1&p_p_col_count=1&_scolarissearchresultsportlet_WAR_scolarissearchresults_displayText=Allergy+%26+intolerance&_scolarissearchresultsportlet_WAR_scolarissearchresults_searchText=Allergy+%26+intolerance&_scolarissearchresultsportlet_WAR_scolarissearchresults_searchType=basic&_scolarissearchresultsportlet_WAR_scolarissearchresults_facetQueryField=topic_id&_scolarissearchresultsportlet_WAR_scolarissearchresults_searchBy=13&_scolarissearchresultsportlet_WAR_scolarissearchresults_orderBy=displayDate-true&_scolarissearchresultsportlet_WAR_scolarissearchresults_facetDisplayName=Allergy+%26+intolerance&_scolarissearchresultsportlet_WAR_scolarissearchresults_facetQueryTerm=z1506030924307755598196034641807&_scolarissearchresultsportlet_WAR_scolarissearchresults_facetCategory=Topics");
        test();
    }

    public static void test() {

        String topicsEntryURL = "https://www.cochranelibrary.com/cdsr/reviews/topics";

        ArrayList<Topic> topics = Crawler.crawlHomePage(topicsEntryURL);

        // this gets all pagination urls for one topic  
        Crawler.crawlTopicPage(topics.get(0));

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
