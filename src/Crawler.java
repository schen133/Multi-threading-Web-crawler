import java.util.*;
import java.io.*;
// apache http client library
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.apache.http.entity.ContentType;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.config.RequestConfig;

// jsoup library for html parsing 
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.Jsoup;

// uses parser to get information, 
public class Crawler {

    // Return an arraylist of topics, review array of each topic still empty
    public static ArrayList<Topic> crawlHomePage(String url) {

        String topicsEntryURL = url;

        HttpClient client = HttpClients.createDefault();

        // should send a request and return us the content
        String responseEntityString = sendGetRequest(client, topicsEntryURL);
        // it returns a string, so pass that inside procee and get a document
        Document doc = processContent(responseEntityString);

        // iterate document and get all topic name plus url
        Elements allLis = doc.select("li.browse-by-list-item");
        // for (Element li : allLis) {
        // System.out.println("This is li content");
        // System.out.println(li.text());
        // }

        ArrayList<Topic> topics = new ArrayList<Topic>();
        for (Element li : allLis) {
            String tempURL = li.select("a").attr("href");
            String tempName = li.select("button").text();
            // add to arraylist of topics
            topics.add(new Topic(tempName, tempURL));
        }
        return topics;
    }

    // takes in a topic instance and modify its information
    public static void crawlTopicPage(Topic topic) {
        HttpClient client = HttpClients.createDefault();
        // String responseEntityString = sendGetRequest(client, topic.getTopicURL());
        String responseEntityString = sendGetRequest(client, topic.getTopicURL());

        Document doc = processContent(responseEntityString);

        Element paginationUL = doc.select("ul.pagination-page-list").first();

        // for each page, get the url and add to arraylist
        Elements paginiationLis = paginationUL.select("li");

        // get all pagination urls for each topic page
        int totalPages = 0;
        for (Element li : paginiationLis) {
            totalPages++;
            // for each li, get the a tag's href address and append it
            String tempPaginationUrl = li.select("a").attr("href");
            topic.addPageURL(tempPaginationUrl);

            // UNCOMMENT THIS
            // setUpReviewsOfCurrentPage(topic, tempPaginationUrl, client);
        }
        topic.setTotalPage(totalPages);
        // TESTING, only crawl for one page url
        // setUpReviewsOfCurrentPage(topic, topic.getFirstPageUrlString(), client);
    }

    public static void setUpReviewsOfCurrentPage(Topic topic, String pageUrl, HttpClient client) {
        String responseEntityString = sendGetRequest(client, pageUrl);

        Document doc = processContent(responseEntityString);

        Elements reviewDivs = doc.select("div.search-results-item-body");

        for (Element reviewDiv : reviewDivs) {
            Element h_3 = reviewDiv.select("h3").first();
            String reviewTopic = topic.getTopicName();
            String reviewUrl = h_3.select("a").attr("href");
            String reviewTitle = h_3.select("a").text();
            String reviewAuthor = reviewDiv.select("div.search-result-authors").text();
            String reviewDate = reviewDiv.select("div.search-result-date").text();
            topic.addReview(new Review(reviewUrl, reviewTopic, reviewTitle, reviewAuthor, reviewDate));
        }
    }

    // TODO@schen133: heavy function, need more error handling
    // all purpose get request method -> String of entity
    public static String sendGetRequest(HttpClient client, String url) {
        try {
            HttpGet request = new HttpGet(url);
            request.setHeader("User-Agent", "Testing/1.0");
            // the topic page is redirecting to another page, so we need to allow circular
            request.setConfig(RequestConfig.custom().setCircularRedirectsAllowed(true).build());

            HttpResponse response = client.execute(request);
            int responseStatusCode = response.getStatusLine().getStatusCode();

            if (responseStatusCode == 200) {
                // System.out.println("Response received successfully.");
                // System.out.println("Response status code: " + responseStatusCode);
                HttpEntity responseEntity = response.getEntity();
                String responseString = EntityUtils.toString(responseEntity);
                return responseString;
            } else {
                System.out.println("Request failed..");
                return null;
            }
        } catch (IOException e) {
            System.out.println("Error occurred while sending get request: " + e.getMessage());
            return null;
        }
    }

    // all purpose string -> document method
    public static Document processContent(String responseEntityString) {
        Document resDoc = Jsoup.parse(responseEntityString);
        return resDoc;
    }

}
