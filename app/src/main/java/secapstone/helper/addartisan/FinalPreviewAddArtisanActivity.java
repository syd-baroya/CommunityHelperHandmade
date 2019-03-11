package secapstone.helper.addartisan;

import android.content.*;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.firestore.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import secapstone.helper.R;
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
    private CollectionReference usersRef = db.collection("users");
    private static final String TAG = "FinalPreviewActivity";

    ConstraintLayout loadingSpinner;
    private User user_info;
    private CollectionReference artisanRef;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_preview_add_artisan);

        user_info = User.getUser();
        artisanRef = usersRef.document(user_info.getIdToken()).collection("artisans");

        Button nextButton8 = (Button) findViewById(R.id.finishButton);
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

        loadingSpinner = findViewById(R.id.progress_loader_add);
    }

    public void onClickNext8()
    {
        Button nextButton8 = (Button) findViewById(R.id.finishButton);

        loadingSpinner.setVisibility(View.VISIBLE);
        pushArtisan(WelcomeAddArtisanActivity.artisanObject);
    }

    public void onClickBack8()
    {
        startActivity(new Intent(FinalPreviewAddArtisanActivity.this, SignatureAddArtisanActivity.class));
    }

    public void pushArtisan(Artisan mewBoi){
        DocumentReference newArtisanRef = artisanRef.document();
        mewBoi.setPictureURL("profiles/" + newArtisanRef.getId() + ".jpg");
        mewBoi.setID(newArtisanRef.getId());

        final Artisan mewBoi2 = mewBoi;

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference artisanPicRef = storageRef.child(mewBoi.getPictureURL());

        Bitmap bitmap = getResizedBitmap(WelcomeAddArtisanActivity.artisanProfileImage, 1920);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
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
                artisanRef
                    .add(mewBoi2)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>(){
                        @Override
                        public void onSuccess(DocumentReference documentReference){
                            Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                            loadingSpinner.setVisibility(View.GONE);
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


    public Bitmap getResizedBitmap(Bitmap bm, int maxWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleFactor = ((float) maxWidth) / width;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleFactor, scaleFactor);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }
}
