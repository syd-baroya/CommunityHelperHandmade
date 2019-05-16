package secapstone.helper.model;
import java.util.ArrayList;
import java.util.List;

public class Listing
{

    private String title;
    private String pictureURL;
    private String description;
    private String id;
    private float price;

    public Listing()
    {

    }

    public Listing(String title, String description, String pictureURL, float price, String id)
    {
        this.title = title;
        this.description = description;
        this.pictureURL = pictureURL;
        this.price = price;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
    public void setID(String id) {
        this.id = id;
    }
    public String getID() { return id; }
}
