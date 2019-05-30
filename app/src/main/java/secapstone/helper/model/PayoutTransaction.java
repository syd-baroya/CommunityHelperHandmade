package secapstone.helper.model;

public class PayoutTransaction
{
    private String cgaID;
    private String artisanID;
    private float amount;
    private String address;
    private String date;

    public PayoutTransaction(String cgaID, String artisanID, float amount, String date)
    {
        this.cgaID = cgaID;
        this.artisanID = artisanID;
        this.amount = amount;
        this.date=date;
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
    public String getAddress() {return address;}
    public void setDate(String date) { this.date = date; }
    public String getDate() {return date;}
}

