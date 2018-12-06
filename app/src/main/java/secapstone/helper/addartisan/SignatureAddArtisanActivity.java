package secapstone.helper.addartisan;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.saga.communityhelperhandmade.*;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import secapstone.helper.Artisan;
import secapstone.helper.MainActivity;

public class SignatureAddArtisanActivity extends AppCompatActivity
{
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference artisansRef = db.collection("artisans");

    private static final String TAG = "MyActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature_add_artisan);

        Button nextButton7 = (Button) findViewById(R.id.nextButton7);
        nextButton7.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onNextClick7();
            }
        });

        Button backButton7 = (Button) findViewById(R.id.backButton7);
        backButton7.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onClickBack7();
            }
        });
    }

    public void onNextClick7()
    {
        pushArtisan(WelcomeAddArtisanActivity.mew);
        startActivity(new Intent(SignatureAddArtisanActivity.this, MainActivity.class));
    }

    public void onClickBack7()
    {
        startActivity(new Intent(SignatureAddArtisanActivity.this, DescriptionAddArtisanActivity.class));
    }

    public void pushArtisan(Artisan mewBoi){
        db.collection("artisans")
                .add(mewBoi)
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
}
