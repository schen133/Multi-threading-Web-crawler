import java.util.ArrayList;
public class Topic {

    private String url;
    private String topicName;
    private int totalPage;
    private ArrayList<String> pageURLS;

    // list of views in this one?
    private Review[] reviews;

    public Topic(String topicName, String url) {
        this.url = url;
        this.topicName = topicName;
        this.pageURLS = new ArrayList<String>();
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

}
