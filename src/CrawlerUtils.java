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
import java.util.logging.*;

// uses parser to get information, 
public class CrawlerUtils {

    // Return an arraylist of topics, review array of each topic still empty
    public static ArrayList<Topic> crawlHomePage(String url, CloseableHttpClient client) {

        String topicsEntryURL = url;

        // HttpClient client = HttpClients.createDefault();

        // should send a request and return us the content
        String responseEntityString = sendGetRequest(client, topicsEntryURL);
        // it returns a string, so pass that inside procee and get a document
        Document doc = processContent(responseEntityString);

        // iterate document and get all topic name plus url
        Elements allLis = doc.select("li.browse-by-list-item");

        ArrayList<Topic> topics = new ArrayList<Topic>();
        for (Element li : allLis) {
            String tempURL = li.select("a").attr("href");
            String tempName = li.select("button").text();
            // add to arraylist of topics
            topics.add(new Topic(tempName, tempURL));
        }
        return topics;
    }

    // FOR MULTI-THREADING, gets all pages then create separate thread on each page
    // (Inconsistent)
    // takes in a topic instance and modify its information
    public static void crawlTopicPage(Topic topic, CloseableHttpClient client) {
        Logger.getLogger("org.apache.http.client.protocol.ResponseProcessCookies").setLevel(Level.OFF);

        // HttpClient client = HttpClients.createDefault();
        // String responseEntityString = sendGetRequest(client, topic.getTopicURL());
        String responseEntityString = sendGetRequest(client, topic.getTopicURL());

        Document doc = processContent(responseEntityString);

        Element paginationUL = doc.select("ul.pagination-page-list").first();

        // else it could be the case that there is no ul, just one page. so we only
        // append the topic url
        if (paginationUL == null) {
            // System.out.println("No pagination ul found for topic: " +
            topic.addPageURL(topic.getTopicURL());
            // return to prevent looking for lis from ul
            return;
        }

        // for each page, get the url and add to arraylist
        Elements paginiationLis = paginationUL.select("li");

        // get all pagination urls for each topic page
        int totalPages = 0;
        for (Element li : paginiationLis) {
            totalPages++;
            // for each li, get the a tag's href address and append it
            String tempPaginationUrl = li.select("a").attr("href");
            topic.addPageURL(tempPaginationUrl);
            // System.out.println("this is pagination url: " + tempPaginationUrl);
            // UNCOMMENT THIS
            // setUpReviewsOfCurrentPage(topic, tempPaginationUrl, client);
        }
        topic.setTotalPage(totalPages);
    }

    // For single thread version program, while there is still a next button, keep
    // crawling
    public static void crawlTopicPageNextButton(Topic topic, CloseableHttpClient client) {
        Logger.getLogger("org.apache.http.client.protocol.ResponseProcessCookies").setLevel(Level.OFF);

        String responseEntityString = sendGetRequest(client, topic.getTopicURL());

        // initial doc
        Document doc = processContent(responseEntityString);
        // setup with initial thread
        setUpReviewsOfCurrentPageNext(topic, doc);
                

        // find next button
        // while there is still nex
        while (true) {
            Element nextButton = doc.select("div.pagination-next-link").first().select("a").first();
            if (nextButton == null) {
                break;
            } else {

                // there is a next button
                // get link, send request and process it to get doc
                String nextLink = nextButton.select("a").attr("href");
                // System.out.println(nextLink + "\n\n");
                doc = processContent(sendGetRequest(client, nextLink));
                // doc now is updated, it's the doc of next page
                setUpReviewsOfCurrentPageNext(topic, doc);
            }
        }
    }

    // access the pagination url and get all reviews of one page
    public static void setUpReviewsOfCurrentPage(Topic topic, String pageUrl, CloseableHttpClient client) {
        // HttpClient client = HttpClients.createDefault();
        // System.out.println("make request to \n\n" + pageUrl);
        // System.out.println("\n\n");

        topic.incrementTotalPage();
        // System.out.println("page exist for topic");

        String responseEntityString = sendGetRequest(client, pageUrl);

        Document doc = processContent(responseEntityString);

        // System.out.println(doc);

        Elements reviewDivs = doc.select("div.search-results-item-body");
        // System.out.println("Review dic" + reviewDivs);

        for (Element reviewDiv : reviewDivs) {
            Element h_3 = reviewDiv.select("h3").first();
            String reviewTopic = topic.getTopicName();
            String tempUrl= h_3.select("a").attr("href");
            String reviewUrl = Parser.parseReviewUrl(tempUrl);
            String reviewTitle = h_3.select("a").text();
            String reviewAuthor = reviewDiv.select("div.search-result-authors").text();
            String reviewDate = reviewDiv.select("div.search-result-date").text();
            reviewDate = Parser.parseDate(reviewDate);
            // System.out.println("review name: " + reviewTitle);

            topic.addReview(new Review(reviewUrl, reviewTopic, reviewTitle, reviewAuthor, reviewDate));
        }
        // System.out.println("Reviews of topic: " + topic.getTopicName() + " has size:"
        // + topic.getReviews().size());

    }
    public static void setUpReviewsOfCurrentPageNext(Topic topic, Document doc ) {
        topic.incrementTotalPage();
        // System.out.println("page exist for topic");

        Elements reviewDivs = doc.select("div.search-results-item-body");
        // System.out.println("Review dic" + reviewDivs);

        for (Element reviewDiv : reviewDivs) {
            Element h_3 = reviewDiv.select("h3").first();
            String reviewTopic = topic.getTopicName();
            String tempUrl= h_3.select("a").attr("href");
            String reviewUrl = Parser.parseReviewUrl(tempUrl);
            String reviewTitle = h_3.select("a").text();
            String reviewAuthor = reviewDiv.select("div.search-result-authors").text();
            String reviewDate = reviewDiv.select("div.search-result-date").text();
            reviewDate = Parser.parseDate(reviewDate);
            // System.out.println("review name: " + reviewTitle);

            topic.addReview(new Review(reviewUrl, reviewTopic, reviewTitle, reviewAuthor, reviewDate));
        }
        // System.out.println("Reviews of topic: " + topic.getTopicName() + " has size:"
        // + topic.getReviews().size());

    }

    // TODO@schen133: heavy function, need more error handling
    // all purpose get request method -> String of entity
    public static String sendGetRequest(HttpClient client, String url) {
        try {
            HttpGet request = new HttpGet(url);
            request.setHeader("User-Agent",
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36");
            request.setHeader("Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
            request.setHeader("Connection", "keep-alive");
            // the topic page is redirecting to another page, so we need to allow circular
            request.setConfig(RequestConfig.custom().setCircularRedirectsAllowed(true).build());

            // request.setConfig(RequestConfig.custom().setRedirectsEnabled(true).build());

            HttpResponse response = client.execute(request);
            int responseStatusCode = response.getStatusLine().getStatusCode();

            if (responseStatusCode == 200) {
                // System.out.println("Response received successfully.");
                // System.out.println("Response status code: " + responseStatusCode);
                String testingHtml = "output.html";
                HttpEntity responseEntity = response.getEntity();
                String responseString = EntityUtils.toString(responseEntity);

                return responseString;
            } else if (responseStatusCode == 412) {
                System.out.println("request code 412");
                return null;

            } else {
                System.out.println("request code not 200");

                return null;
            }
        } catch (IOException e) {
            // System.out.println("Error occurred while sending get request: " +
            // e.getMessage());
            System.out.println("some other shit went wrong");
            return null;
        }
    }

    // all purpose string -> document method
    public static Document processContent(String responseEntityString) {

        Document resDoc = Jsoup.parse(responseEntityString);
        return resDoc;
    }

}
