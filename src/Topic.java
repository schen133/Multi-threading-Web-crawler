
public class Topic {

    private String url;
    private String topicName;
    // list of reviews belong to this topic?

    private Review[] reviews;

    public Topic(String topicName, String url) {
        this.url = url;
        this.topicName = topicName;
    }
    
    public String getTopicName() {
        return topicName;
    }

    public String getTopicURL() {
        return url;
    }

}
