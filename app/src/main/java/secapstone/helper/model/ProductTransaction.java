package secapstone.helper.model;

import com.google.firebase.Timestamp;

public class ProductTransaction
{
    private String productID;
    private String cgaID;
    private String artisanID;
    private float amount;
    private String date;

    public ProductTransaction(String cgaID, String artisanID, float amount, String date)
    {
        this.cgaID = cgaID;
        this.artisanID = artisanID;
        this.amount = amount;
        this.date = date;
    }


    public String getCgaID() {return cgaID;}
    public String getArtisanID() {return artisanID;}
    public String getProductID() {return productID; }
    public void setProductID(String productID) {this.productID=productID; }

    public void setAmount(float newAmount)
    {
        amount = newAmount;
    }

    public float getAmount()
    {
        return amount;
    }
    public void setDate(String date) {this.date = date; }
    public String getDate(){return date;}

}
