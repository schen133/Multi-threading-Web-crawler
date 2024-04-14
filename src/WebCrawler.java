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
// jsoup library for html parsing 
import org.jsoup.nodes.Document;
import ogk.jsoup.Jsoup;


public class WebCrawler {

    public static void main(String[] args) {
        String topicsEntryURL = "https://www.cochranelibrary.com/cdsr/reviews/topics";

        HttpClient client = HttpClients.createDefault();

        // should send a request and return us the content
        sendGetRequest(client, topicsEntryURL);
    }

    // all purpose get request method
    public static void sendGetRequest(HttpClient client, String url) {

        try {
            HttpGet request = new HttpGet(url);
            // Header[] headers = {
            // new BasicHeader("User-Agent", "Testing/1.0")
            // };

            // request.setHeaders(headers);
            request.setHeader("User-Agent", "Testing/1.0");
            HttpResponse response = client.execute(request);

            int responseStatusCode = response.getStatusLine().getStatusCode();

            if (responseStatusCode == 200) {
                System.out.println("Response received successfully.");
                System.out.println("Response status code: " + responseStatusCode);
                HttpEntity responseEntity = response.getEntity();
                processContent(responseEntity.getContent());
            } else {
                System.out.println("Request failed..");
                return;
            }

        } catch (IOException e) {
            System.out.println("Error occurred while sending get request: " + e.getMessage());
        }
    }

    // this should be used to process the content of the response. Return a string
    public static void processContent(InputStream content) {
        System.out.println(content);
        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
        
        StringBuffer result = new StringBuffer();
        while ()

    }

    public static void startCrawl(String url) {
        System.out.println("starts crawling");

    }

}
