package secapstone.helper.addartisan;

import android.content.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.firestore.*;
import com.saga.communityhelperhandmade.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.*;
import android.widget.*;

import secapstone.helper.*;
import secapstone.helper.Artisan;


public class FinalPreviewAddArtisanActivity extends AppCompatActivity
{
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference artisansRef = db.collection("artisans");
    private static final String TAG = "FinalPreviewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_preview_add_artisan);

        Button nextButton8 = (Button) findViewById(R.id.nextButton8);
        nextButton8.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onClickNext8();
            }
        });

        Button backButton8 = (Button) findViewById(R.id.backButton8);
        backButton8.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onClickBack8();
            }
        });
    }

    public void onClickNext8()
    {
        pushArtisan(WelcomeAddArtisanActivity.mew);
        startActivity(new Intent(FinalPreviewAddArtisanActivity.this, MainActivity.class));
    }

    public void onClickBack8()
    {
        startActivity(new Intent(FinalPreviewAddArtisanActivity.this, SignatureAddArtisanActivity.class));
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
