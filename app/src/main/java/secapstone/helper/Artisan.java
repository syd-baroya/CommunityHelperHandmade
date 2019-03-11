package secapstone.helper;


//Must match artisan in Firebase!

public class Artisan {
    private String name;
    private String description;
    private String phoneNumber;
    private String pictureURL;
    private String address;
    private String countryRegion;
    private String zipPostalCode;
    private String firstName;
    private String lastName;
    private String craft;
    private String howItsMade;
    private String inspiration;
    private String id;
    /*private String facebook;
    private String twitter;
    private String pinterest;
    private String instagram;
    private boolean customOrders;*/


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

    public void setFirstName(String fn){this.firstName = fn;}

    public void setLastName(String ln){this.lastName = ln;}

    public void setPhoneNumber(String phoneNum){
        this.phoneNumber = phoneNum;
    }

    public void setAddress(String adr){
        this.address = adr;
    }

    public void setCountryRegion(String couReg){this.countryRegion = couReg;}

    public void setZipPostalCode(String postCode){this.zipPostalCode = postCode;}

    public void setCraft(String cra){this.craft = cra;}

    public void setHowItsMade(String him){this.howItsMade = him;}

    public void setInspiration(String ins){this.inspiration = ins;}

    public void setID(String id) {
        this.id = id;
    }

    /*public void setFacebook(String fb){this.facebook = fb;}

    public void setTwitter(String twit){this.twitter = twit;}

    public void setPinterest(String pin){this.pinterest = pin;}

    public void setInstagram (String inst){this.instagram = inst;}

    public void setCustomOrders(boolean co){this.customOrders = co;}*/

    public void setDescription(String des){
        this.description = des;
    }

    public void setPictureURL(String url) {
        this.pictureURL = url;
    }

    public String getName() {
        return name;
    }

    public String getFirstName() { return firstName;}

    public String getLastName() { return lastName;}

    public String getDescription() { return description;}

    public String getCountryRegion() { return countryRegion;}

    public String getZipPostalCode() { return zipPostalCode;}

    public String getCraft() { return craft;}

    public String getHowItsMade(){ return howItsMade;}

    public String getInspiration(){ return inspiration;}

    /*public String getFacebook() { return facebook;}

    public String getTwitter() {return twitter;}

    public String getPinterest() { return pinterest;}

    public String getInstagram() { return instagram;}

    public boolean getCustomOrders() {return customOrders;}*/

    public String getPhoneNumber() { return phoneNumber;}

    public String getPictureURL() { return pictureURL;}

    public String getAddress() {return address;}

    public String getID() {return id;}
}
