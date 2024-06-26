public class Review {

    private String url;
    private String topic;
    private String title;
    private String author;
    private String date;

    public Review(String url, String topic, String title, String author, String date) {
        this.url = url;
        this.topic = topic;
        this.title = title;
        this.author = author;
        this.date = date;
    }

    public String getReviewUrl() {
        return url;
    }

    public String getReviewTopic() {
        return topic;
    }

    public String getReviewTitle() {
        return title;
    }

    public String getReviewAuthor() {
        return author;
    }

    public String getReviewDate() {
        return date;
    }

    // Helper function
    public void printReview() {
        System.out.println("Review URL: " + url);
        System.out.println("Review Topic: " + topic);
        System.out.println("Review Title: " + title);
        System.out.println("Review Author: " + author);
        System.out.println("Review Date: " + date + "\n");
    }

}