package secapstone.helper;

public class Artisan {
    private String name;
    private String description;
    private String phoneNumber;
    private String pictureURL;
    private String address;

    public Artisan(String name, String description, String phoneNumber, String pictureURL) {
        this.description = description;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.pictureURL = pictureURL;
    }

    public Artisan() {

    }

    public void setName(String n) {
        this.name = n;
    }

    public void setPhoneNumber(String phoneNum){
        this.phoneNumber = phoneNum;
    }

    public void setAddress(String adr){
        this.address = adr;
    }

    public void setDescription(String des){
        this.description = des;
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

    public String getAddress() {return address;}
}
