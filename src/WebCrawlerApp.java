import java.util.ArrayList;

public class WebCrawlerApp {

    public static void main(String[] args) {
        // Crawler.crawlTopicPage(
        // "https://www.cochranelibrary.com/en/search?p_p_id=scolarissearchresultsportlet_WAR_scolarissearchresults&p_p_lifecycle=0&p_p_state=normal&p_p_mode=view&p_p_col_id=column-1&p_p_col_count=1&_scolarissearchresultsportlet_WAR_scolarissearchresults_displayText=Allergy+%26+intolerance&_scolarissearchresultsportlet_WAR_scolarissearchresults_searchText=Allergy+%26+intolerance&_scolarissearchresultsportlet_WAR_scolarissearchresults_searchType=basic&_scolarissearchresultsportlet_WAR_scolarissearchresults_facetQueryField=topic_id&_scolarissearchresultsportlet_WAR_scolarissearchresults_searchBy=13&_scolarissearchresultsportlet_WAR_scolarissearchresults_orderBy=displayDate-true&_scolarissearchresultsportlet_WAR_scolarissearchresults_facetDisplayName=Allergy+%26+intolerance&_scolarissearchresultsportlet_WAR_scolarissearchresults_facetQueryTerm=z1506030924307755598196034641807&_scolarissearchresultsportlet_WAR_scolarissearchresults_facetCategory=Topics");
        test();
    }

    public static void test() {

        String topicsEntryURL = "https://www.cochranelibrary.com/cdsr/reviews/topics";

        ArrayList<Topic> topics = Crawler.crawlHomePage(topicsEntryURL);
        // Crawler.crawlTopicPage(topics.get(0));
        // for (Review review : topics.get(0).getReviews()) {
        // review.printReview();
        // }

        for (Topic topic : topics) {
            // System.out.println("Topic name: " + topic.getTopicName());
            // System.out.println("Topic URL: " + topic.getTopicURL());

            // crawl each topic's url
            // pass in each topic instance's reference

            // heavy
            Crawler.crawlTopicPage(topic);
            // each topic should have bunch of pagniation urls in page urls variable now

            // for each page it has to visit, we do with a different thread...
            // that way we finish off each topic faster?
            // setUpReviewsOfCurrentPage(topic, tempPaginationUrl, client);
            


        } // Inserted closing curly brace here

    }

}
