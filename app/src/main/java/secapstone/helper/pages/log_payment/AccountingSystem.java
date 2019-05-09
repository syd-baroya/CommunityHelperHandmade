package secapstone.helper.pages.log_payment;

import androidx.annotation.NonNull;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import secapstone.helper.model.Artisan;
import secapstone.helper.model.Listing;
import secapstone.helper.model.PayoutTransaction;
import secapstone.helper.model.ProductTransaction;
import secapstone.helper.model.Report;
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
            List<ProductTransaction> transactions = getTransactions(timeLastRun);

            float totalFromTransactionPeriod = 0;

            for (ProductTransaction t: transactions)
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

    public static void logPayment(String artisanID, Double amount, Date userEnteredDate, Date dateUploaded)
    {
        String userID = User.getUser().getID();
        DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(userID);
        DocumentReference artisanRef = userRef.collection("artisans").document(artisanID);
        CollectionReference reportsRef = FirebaseFirestore.getInstance().collection("reports");


        final PayoutTransaction mewBoi2 = new PayoutTransaction(userID, artisanID, amount);

        artisanRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot docSnapshot = task.getResult();
                    if(docSnapshot!=null){
                        mewBoi2.setAddress((String)docSnapshot.get("address"));
                    }
                }
            }
        });
        Report report = new Report(mewBoi2);
        final CollectionReference finalReportsRef = reportsRef;


        finalReportsRef
                .add(report)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>(){
                    @Override
                    public void onSuccess(DocumentReference documentReference){
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener(){
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }

    public static List<ProductTransaction> getTransactions(long startTime)
    {
        List<ProductTransaction> transactions = new ArrayList<ProductTransaction>();
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