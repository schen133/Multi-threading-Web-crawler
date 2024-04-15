import org.jsoup.nodes.Document;

import java.util.HashMap;

import org.jsoup.Jsoup;

// takes in a jsoup document and return desired objects
public class Parser {

    private Document doc;

    public static String parseDate(String date) {
        // hashmap of months and their corresponding month in numebers
        HashMap<String, String> months = new HashMap<String, String>();
        months.put("January", "01");
        months.put("February", "02");
        months.put("March", "03");
        months.put("April", "04");
        months.put("May", "05");
        months.put("June", "06");
        months.put("July", "07");
        months.put("August", "08");
        months.put("September", "09");
        months.put("October", "10");
        months.put("November", "11");
        months.put("December", "12");

        String res;

        String[] dateArr = date.split(" ");
        String day = dateArr[0];
        String month = dateArr[1];
        String year = dateArr[2];

        // ensure day is in 2 digits
        if (day.length() == 1) {
            day = "0" + day;
        }
        res = year + "-" + months.get(month) + "-" + day;

        return res;
    }

    public static String getDesiredReviewString(Review review) {
        String res = review.getReviewUrl() + " | " + review.getReviewTopic() + " | "
                + review.getReviewTitle() + " | " + review.getReviewAuthor() + " | " + review.getReviewDate();
        return res;
    }

}
