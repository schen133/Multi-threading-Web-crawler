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
// jsoup library for html parsing 
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.Jsoup;

// uses parser to get information, 
public class Crawler {

    // get the document of the Home page
    public static void getHomePageDoc() {
        String topicsEntryURL = "https://www.cochranelibrary.com/cdsr/reviews/topics";

        HttpClient client = HttpClients.createDefault();

        // should send a request and return us the content
        String responseEntitiyString = sendGetRequest(client, topicsEntryURL);
        // it returns a string, so pass that inside procee and get a document
        Document doc = processContent(responseEntitiyString);
        // iterate document and get all topic name plus url

        // how many topics are there? it should be each li element in the DOM
        // Elements topLis = doc.select("li.bro");
        // Element topDiv = doc.select("dl.dl-section").first();
        // Elements topLis = topDiv.select("li");
        Elements allLis = doc.select("li.browse-by-list-item");
        // for (Element li : allLis) {
        //     System.out.println("This is li content");
        //     System.out.println(li.text());
        // }

        ArrayList<Topic> topics = new ArrayList<Topic>();
        for (Element li : allLis) {
            String tempURL = li.select("a").attr("href");
            String tempName = li.select("button").text();
            // add to arraylist of topics
            topics.add(new Topic(tempName, tempURL));
        }
        
        for (Topic topic : topics) {
            
            System.out.println("Topic name: " + topic.getTopicName());
            System.out.println("Topic URL: " + topic.getTopicURL());
        }

    }

    // get the document of the topic page
    public static Document getTopicPageDoc(String topicURL) {
        HttpClient client = HttpClients.createDefault();
        String responseEntityString = sendGetRequest(client, topicURL);
        Document doc = processContent(responseEntityString);
        return doc;
    }

    // all purpose get request method -> String of entity
    public static String sendGetRequest(HttpClient client, String url) {
        try {
            HttpGet request = new HttpGet(url);
            request.setHeader("User-Agent", "Testing/1.0");
            HttpResponse response = client.execute(request);
            int responseStatusCode = response.getStatusLine().getStatusCode();

            if (responseStatusCode == 200) {
                System.out.println("Response received successfully.");
                System.out.println("Response status code: " + responseStatusCode);
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

        // BufferedReader br = new BufferedReader(new InputStreamReader(content));
        // StringBuilder sb = new StringBuilder();
        // String line;
        // try {
        // while ((line = br.readLine()) != null) {
        // sb.append(line);
        // }
        // } catch (IOException e) {
        // System.out.println("Error reading content: " + e.getMessage());
        // }
        // Document resDocument = Jsoup.parse(sb.toString());
        // return resDocument;
    }

}
