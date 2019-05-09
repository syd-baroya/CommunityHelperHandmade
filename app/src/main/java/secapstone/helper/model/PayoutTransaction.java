package secapstone.helper.model;

import com.google.firebase.Timestamp;

public class PayoutTransaction
{
    private String cgaID;
    private String artisanID;
    private Double amount;
    private Timestamp time;
    private String address;
    private boolean hasBeenAccounted; // Used to see if this transaction has gone through the accounting system.

    public PayoutTransaction(String cgaID, String artisanID, Double amount)
    {
        this.cgaID = cgaID;
        this.artisanID = artisanID;
        this.amount = amount;
        this.time = Timestamp.now();
        this.hasBeenAccounted = false;
    }

    public String getCgaID() {return cgaID;}
    public String getArtisanID() {return artisanID;}
    public void setAmount(Double newAmount)
    {
        amount = newAmount;
    }

    public Double getAmount()
    {
        return amount;
    }

    public Timestamp getTime()
    {
        return time;
    }

    public void setAddress(String address) { this.address = address; }
    public boolean getHasBeenAccounted() { return hasBeenAccounted; }

    public void setHasBeenAccounted(boolean newHasBeenAccounted) { hasBeenAccounted = newHasBeenAccounted; }
}

