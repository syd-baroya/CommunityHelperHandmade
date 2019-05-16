package secapstone.helper.model;

import com.google.firebase.Timestamp;

public class ProductTransaction
{
    private String productID;
    private String cgaID;
    private String artisanID;
    private float amount;
    private Timestamp time;

    public ProductTransaction(String cgaID, String artisanID, float amount)
    {
        this.cgaID = cgaID;
        this.artisanID = artisanID;
        this.amount = amount;
        this.time = Timestamp.now();
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

}
