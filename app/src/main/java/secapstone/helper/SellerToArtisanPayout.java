package secapstone.helper;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class SellerToArtisanPayout
{

    private static long timeLastRun = -1;
    private static long timeBetweenEachRun = 86400000;

    /*
      Gets all the artisans for the Community Leader.
      Calculates how long since the last run to avoid overusing APIs.
      Grabs all the transactions for the 1 seller account since last run.
      Goes through each transaction, gets its product ID, determines
      which Artisan made that product, and adds that transaction amount
      to the amount the Community Leader owes that Artisan.
     */
    public static void CalculatePayouts()
    {
        if (System.currentTimeMillis() >= timeLastRun + timeBetweenEachRun)
        {
            List<Artisan> artisans = getArtisans();
            List<AmazonTransaction> transactions = getTransactions();

            boolean foundTransaction = false;
            for (AmazonTransaction t : transactions) {

                if (!foundTransaction) {

                    for (Artisan a : artisans) {

                        if (!foundTransaction) {

                            for (Listing l : a.getListings()) {

                                if (t.productID == l.productID) {
                                    a.setMoneyOwedFromCommunityLeader(a.getMoneyOwedFromCommunityLeader() + t.amount);
                                    foundTransaction = true;
                                    break;
                                }
                            }
                        }
                        else
                        {
                            break;
                        }
                    }
                }
                else
                {
                    break;
                }
            }

            timeLastRun = System.currentTimeMillis();
        }
    }

    public static List<AmazonTransaction> getTransactions()
    {
        List<AmazonTransaction> transactions = new ArrayList<AmazonTransaction>();
        //TODO This is where Amazon MWS API call is.
        return transactions;
    }

    public static List<Artisan> getArtisans()
    {
        List<Artisan> artisans = new ArrayList<Artisan>();
        //TODO grab artisans from firebase
        return artisans;
    }
}