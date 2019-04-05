package secapstone.helper;

import com.google.firebase.Timestamp;

public class AmazonTransaction
{
    private int productID;
    private float amount;
    private Timestamp time;
    private boolean hasBeenAccounted; // Used to see if this transaction has gone through the accounting system.

    public AmazonTransaction(int productID, float amount)
    {
        this.productID = productID;
        this.amount = amount;
        this.time = Timestamp.now();
        this.hasBeenAccounted = false;
    }

    public void setProductID(int newId)
    {
        productID = newId;
    }

    public int getProductID()
    {
        return productID;
    }

    public void setAmount(float newAmount)
    {
        amount = newAmount;
    }

    public float getAmount()
    {
        return amount;
    }

    public void setTime(Timestamp newTime)
    {
        time = newTime;
    }

    public Timestamp getTime()
    {
        return time;
    }

    public boolean getHasBeenAccounted() { return hasBeenAccounted; }

    public void setHasBeenAccounted(boolean newHasBeenAcounted) { hasBeenAccounted = newHasBeenAcounted; }
}
