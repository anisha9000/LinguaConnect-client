package client.linguaconnect.com.linguaconnectclient.fragments.adapter;

/**
 * Created by anisha on 16/12/15.
 */
public class NavDrawerItem {

    private String title;
    private int imageId;


    public NavDrawerItem() {

    }

    public NavDrawerItem(String title,int imageId) {
        this.title = title;
        this.imageId=imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;

    }
}
