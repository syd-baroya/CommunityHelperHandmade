package secapstone.helper;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class SellerToArtisanPayout
{

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
        List<Artisan> artisans = getArtisans();
        List<Double> transaction = getTransactions();


    }

    public static List<Double> getTransactions()
    {
        List<Double> transactions = new ArrayList<Double>();

        return transactions;
    }

    public static List<Artisan> getArtisans()
    {
        List<Artisan> artisans = new ArrayList<Artisan>();


        return artisans;
    }
}
