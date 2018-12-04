package secapstone.helper;

public class Artisan {
    private String name;
    private String description;
    private String phoneNumber;
    private String pictureURL;

    public Artisan(String name, String description, String phoneNumber, String pictureURL) {
        this.description = description;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.pictureURL = pictureURL;
    }

    public Artisan() {

    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPictureURL() {
        return pictureURL;
    }
}
