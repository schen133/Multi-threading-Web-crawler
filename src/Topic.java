import java.util.ArrayList;

public class Topic {

    private String url;
    private String topicName;
    private int totalPage;
    private ArrayList<String> pageURLS;

    // list of views in this one?
    private ArrayList<Review> reviews;

    public Topic(String topicName, String url) {
        this.url = url;
        this.topicName = topicName;
        this.pageURLS = new ArrayList<String>();
        this.reviews = new ArrayList<Review>();
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public String getTopicName() {
        return topicName;
    }

    public String getTopicURL() {
        return url;
    }

    public void addPageURL(String url) {
        pageURLS.add(url);
    }

    public void printAllPageURLS() {
        for (String url : pageURLS) {
            System.out.println(url);
        }
    }
    public ArrayList<String> getPageURLS() {
        return pageURLS;
    }

    public String getFirstPageUrlString() {
        return pageURLS.get(0);
    }

    public void addReview(Review review) {
        reviews.add(review);
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

}
