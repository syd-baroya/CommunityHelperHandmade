package secapstone.helper.model;

import com.google.firebase.Timestamp;

public class PayoutTransaction
{
    private String cgaID;
    private String artisanID;
    private float amount;
    private String address;

    public PayoutTransaction(String cgaID, String artisanID, float amount)
    {
        this.cgaID = cgaID;
        this.artisanID = artisanID;
        this.amount = amount;
    }

    public String getCgaID() {return cgaID;}
    public String getArtisanID() {return artisanID;}
    public void setAmount(float newAmount)
    {
        amount = newAmount;
    }

    public float getAmount()
    {
        return amount;
    }

    public void setAddress(String address) { this.address = address; }
}

