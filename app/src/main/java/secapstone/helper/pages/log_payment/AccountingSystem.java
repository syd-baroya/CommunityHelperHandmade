package secapstone.helper.pages.log_payment;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import secapstone.helper.model.Artisan;
import secapstone.helper.model.Listing;
import secapstone.helper.model.PayoutTransaction;
import secapstone.helper.model.ProductTransaction;
import secapstone.helper.model.Report;
import secapstone.helper.model.User;
import secapstone.helper.pages.login.LoginActivity;

public class AccountingSystem
{

    private static long timeLastRun = -1; //TODO This needs to be in firebase, not kept in RAM or it will reset each time APP restarts.
    private static final long TIME_BETWEEN_EACH_RUN = 86400000;

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static CollectionReference usersRef = db.collection("users");
    private static User user_info = User.getUser();
    private static CollectionReference artisanRef = usersRef.document(user_info.getID()).collection("artisans");
    public static final String TAG = "AccountingSystem";

    /*
      Gets all the artisans for the Community Leader.
      Calculates how long since the last run to avoid overusing APIs.
      Grabs all the transactions for the 1 seller account since last run.
      Goes through each transaction, gets its product ID, determines
      which Artisan made that product, and adds that transaction amount
      to the amount the Community Leader owes that Artisan.
     */

    public AccountingSystem(){ System.out.println("made new Accounting System"); }

    public static void logPayment(String artisanID, float amount, final Context context)
    {
        final double doubleAmount = amount;
        String userID = User.getUser().getID();
        final DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(userID);
        final DocumentReference artisanRef = userRef.collection("artisans").document(artisanID);
        final CollectionReference payoutsRef = FirebaseFirestore.getInstance().collection("PayoutTransactions");


        final PayoutTransaction mewBoi2 = new PayoutTransaction(userID, artisanID, amount);

        artisanRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot docSnapshot = task.getResult();
                    if (docSnapshot!=null){
                        String address = (String) docSnapshot.get("address");
                        if (address!=null) {
                            mewBoi2.setAddress(address);
                            payoutsRef
                                .add(mewBoi2)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                        userRef.update("balance", FieldValue.increment((-1)*doubleAmount));
                                        artisanRef.update("moneyOwedFromCommunityLeader", FieldValue.increment((-1)*doubleAmount));
                                        ((Activity)context).finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error adding document", e);
                                    }
                                });
                    }
                    }
                }else{
                    Log.d(TAG, "GET FAILED WITH: ", task.getException());
                }
            }
        });

    }

    public static void logPurchase(String artisanID, float amount, Listing listing)
    {
        if(listing==null)
            return;

        final double doubleAmount = amount;
        System.out.println("in Accounting System logPayment");
        String userID = User.getUser().getID();
        final DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(userID);
        final DocumentReference artisanRef = userRef.collection("artisans").document(artisanID);
        DocumentReference productRef = artisanRef.collection("products").document(listing.getID());
        final CollectionReference productTransRef = FirebaseFirestore.getInstance().collection("ProductTransactions");


        final ProductTransaction mewBoi2 = new ProductTransaction(userID, artisanID, amount);

        Map<String, Object> purchaseUpdate = new HashMap<>();
        int amountPurchased = (int) ((int) amount/listing.getPrice());
        int newShippedCount = listing.getShippedCount() - amountPurchased;
        purchaseUpdate.put("shippedCount", newShippedCount);
        productRef.update(purchaseUpdate);


        productRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot docSnapshot = task.getResult();
                    if (docSnapshot!=null){
                        String id = (String) docSnapshot.get("id");
                        if (id!=null) {
                            mewBoi2.setProductID(id);
                            productTransRef
                                    .add(mewBoi2)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>(){
                                        @Override
                                        public void onSuccess(DocumentReference documentReference){
                                            Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                            userRef.update("balance", FieldValue.increment(doubleAmount));
                                            artisanRef.update("moneyOwedFromCommunityLeader", FieldValue.increment(doubleAmount));
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener(){
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error adding document", e);
                                        }
                                    });
                        }
                    }
                } else{
                    Log.d(TAG, "GET FAILED WITH: ", task.getException());
                }
            }
        });
    }

    public static void logShipment(String artisanID, int amount, Listing listing, final Dialog dialog) {
        if(listing==null)
            return;
        String userID = User.getUser().getID();
        DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(userID);
        DocumentReference artisanRef = userRef.collection("artisans").document(artisanID);
        DocumentReference productRef = artisanRef.collection("products").document(listing.getID());
        final CollectionReference productTransRef = FirebaseFirestore.getInstance().collection("Shipments");

        final ProductTransaction mewBoi2 = new ProductTransaction(userID, artisanID, amount);

        Map<String, Object> shipmentUpdates = new HashMap<>();
        int shippedCount = listing.getShippedCount() + amount;
        shipmentUpdates.put("shippedCount", shippedCount);
        productRef.update(shipmentUpdates);

        productRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot docSnapshot = task.getResult();
                    if(docSnapshot!=null){
                        String id = (String) docSnapshot.get("id");
                        if(id!=null) {
                            mewBoi2.setProductID(id);
                            productTransRef
                                    .add(mewBoi2)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>(){
                                        @Override
                                        public void onSuccess(DocumentReference documentReference){
                                            Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                            dialog.dismiss();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener(){
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error adding document", e);
                                        }
                                    });
                        }
                    }
                }else{
                    Log.d(TAG, "GET FAILED WITH: ", task.getException());
                }
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
}