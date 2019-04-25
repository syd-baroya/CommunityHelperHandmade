package secapstone.helper.pages.log_payment;

import androidx.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import secapstone.helper.model.AmazonTransaction;
import secapstone.helper.model.Artisan;
import secapstone.helper.model.User;

public class AccountingSystem
{

    private static long timeLastRun = -1; //TODO This needs to be in firebase, not kept in RAM or it will reset each time APP restarts.
    private static final long TIME_BETWEEN_EACH_RUN = 86400000;

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static CollectionReference usersRef = db.collection("users");
    private static User user_info = User.getUser();
    private static CollectionReference artisanRef = usersRef.document(user_info.getID()).collection("artisans");
    private static final String TAG = "AccountingSystem";

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
        if (System.currentTimeMillis() >= timeLastRun + TIME_BETWEEN_EACH_RUN)
        {
            List<Artisan> artisans = getArtisans();
            List<AmazonTransaction> transactions = getTransactions(timeLastRun);

            float totalFromTransactionPeriod = 0;

            for (AmazonTransaction t: transactions)
            {
                for (Artisan a: artisans)
                {
//                    SparseArray<Listing> artisanListings = a.getListings();
//
//                    if (artisanListings.get(t.getProductID()) != null && !t.getHasBeenAccounted())
//                    {
//                        a.setMoneyOwedFromCommunityLeader(a.getMoneyOwedFromCommunityLeader() + t.getAmount());
//                        totalFromTransactionPeriod += t.getAmount();
//                        t.setHasBeenAccounted(true);
//                        break;
//                    }

                }
            }

            timeLastRun = System.currentTimeMillis();

        }

    }

    public static void logPayment(DocumentReference a, Double amount, Date userEnteredDate, Date dateUploaded)
    {
        AccountingSystem.artisanRef.add(a)
            .addOnSuccessListener(new OnSuccessListener<DocumentReference>(){
                @Override
                public void onSuccess(DocumentReference documentReference){
                    Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                }}).addOnFailureListener(new OnFailureListener(){
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }});
    }

    public static List<AmazonTransaction> getTransactions(long startTime)
    {
        List<AmazonTransaction> transactions = new ArrayList<AmazonTransaction>();
        //TODO This is where Amazon MWS API call is.
        //TODO Only get transactions after startTime.
        return transactions;
    }

    public static List<Artisan> getArtisans()
    {
        List<Artisan> artisans = new ArrayList<Artisan>();
        //TODO grab artisans from firebase
        return artisans;
    }
}