package client.linguaconnect.com.linguaconnectclient.ui.adapter;

/**
 * Created by anisha on 27/12/15.
 */
public class InterpreterItem {
    int rating, age;
    String gender, pictureUrl, firstName, lastName, nationality, email;

    public InterpreterItem(String firstName, String lastName, String email, String nationality,
                           String pictureUrl, int age, int rating) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.nationality = nationality;
        this.pictureUrl = pictureUrl;
        this.age = age;
        this.rating = rating;
    }

    public String getName() {
        return firstName + " "+ lastName;
    }

    public int getAge() {
        return age;
    }

    public String getNationality() {
        return nationality;
    }

    public int getRating() {
        return rating;
    }

    public String getEmail() {
        return email;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }
}
