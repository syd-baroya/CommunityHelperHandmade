package secapstone.helper.addartisan;

import android.content.*;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.firestore.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.saga.communityhelperhandmade.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.*;
import android.widget.*;

import java.io.ByteArrayOutputStream;

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

        TextView namePreview = (TextView) findViewById(R.id.namePreview);
        namePreview.setText("Name: " + WelcomeAddArtisanActivity.artisanObject.getName());

        TextView phonePreview = (TextView) findViewById(R.id.phoneNumberPreview);
        phonePreview.setText("Phone Number: " + WelcomeAddArtisanActivity.artisanObject.getPhoneNumber());

        TextView addressPreview = (TextView) findViewById(R.id.addressPreview);
        addressPreview.setText("Address: " + WelcomeAddArtisanActivity.artisanObject.getAddress());

        TextView descrPreview = (TextView) findViewById(R.id.descriptionPreview);
        descrPreview.setText("Store Description: " + WelcomeAddArtisanActivity.artisanObject.getDescription());
    }

    public void onClickNext8()
    {
        Button nextButton8 = (Button) findViewById(R.id.nextButton8);

        nextButton8.setText("Uploading...");
        pushArtisan(WelcomeAddArtisanActivity.artisanObject);
    }

    public void onClickBack8()
    {
        startActivity(new Intent(FinalPreviewAddArtisanActivity.this, SignatureAddArtisanActivity.class));
    }

    public void pushArtisan(Artisan mewBoi){
        DocumentReference newArtisanRef = db.collection("artisans").document();
        mewBoi.setPictureURL("profiles/" + newArtisanRef.getId() + ".jpg");

        final Artisan mewBoi2 = mewBoi;

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference artisanPicRef = storageRef.child(mewBoi.getPictureURL());

        Bitmap bitmap = WelcomeAddArtisanActivity.artisanProfileImage;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = artisanPicRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                db.collection("artisans")
                    .add(mewBoi2)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>(){
                        @Override
                        public void onSuccess(DocumentReference documentReference){
                            Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                            startActivity(new Intent(FinalPreviewAddArtisanActivity.this, MainActivity.class));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener(){
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
            }
        });
    }
}
