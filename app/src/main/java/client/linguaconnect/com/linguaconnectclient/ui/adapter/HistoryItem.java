package client.linguaconnect.com.linguaconnectclient.ui.adapter;

/**
 * Created by anisha on 7/1/16.
 */
public class HistoryItem {
    int rating;
    String pictureUrl, firstName, lastName, language, duration, status, bookingTime;

    public HistoryItem(String firstName, String lastName, String language, String duration,
                       String pictureUrl, int rating, String status, String bookingTime) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.language = language;
        this.duration = duration;
        this.pictureUrl = pictureUrl;
        this.rating = rating;
        this.status = status;
        this.bookingTime = bookingTime;
    }

    public String getName() {
        return firstName + " "+ lastName;
    }

    public int getRating() {
        return rating;
    }

    public String getLanguage() {
        return language;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getDuration() {
        return duration;
    }

    public String getStatus() {
        return status;
    }

    public String getBookingTime() {
        return bookingTime;
    }
}
