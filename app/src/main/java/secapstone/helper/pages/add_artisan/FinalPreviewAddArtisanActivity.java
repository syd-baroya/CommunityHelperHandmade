package secapstone.helper.pages.add_artisan;

import android.content.*;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.firestore.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import secapstone.helper.pages.MainActivity;
import secapstone.helper.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.*;
import android.widget.*;

import java.io.ByteArrayOutputStream;
import java.util.Random;

import secapstone.helper.model.Artisan;
import secapstone.helper.model.User;


public class FinalPreviewAddArtisanActivity extends AppCompatActivity
{
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = db.collection("users");
    private static final String TAG = "FinalPreviewActivity";

    ConstraintLayout loadingSpinner;
    private User user_info;
    private CollectionReference artisanRef;

    TextView namePreview, phonePreview, addressPreview, descrPreview;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_preview_add_artisan);

        user_info = User.getUser();
        artisanRef = usersRef.document(user_info.getID()).collection("artisans");

        namePreview = findViewById(R.id.nameText);
        namePreview.setText(WelcomeAddArtisanActivity.artisanObject.getName());

        phonePreview = findViewById(R.id.phoneText);
        phonePreview.setText(WelcomeAddArtisanActivity.artisanObject.getPhoneNumber());

        addressPreview = findViewById(R.id.addressText);
        addressPreview.setText(WelcomeAddArtisanActivity.artisanObject.getAddress());

        descrPreview = findViewById(R.id.descriptionText);
        descrPreview.setText(WelcomeAddArtisanActivity.artisanObject.getDescription());

        loadingSpinner = findViewById(R.id.progress_loader_add);
    }

    public void onClickNext(View view)
    {
        loadingSpinner.setVisibility(View.VISIBLE);
        pushArtisan(WelcomeAddArtisanActivity.artisanObject);
        WelcomeAddArtisanActivity.artisanObject = new Artisan();
        WelcomeAddArtisanActivity.artisanProfileImage = null;
    }

    public void onClickBack(View view)
    {
        startActivity(new Intent(FinalPreviewAddArtisanActivity.this, SignatureAddArtisanActivity.class));
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    public void pushArtisan(Artisan artisanObject) {
        DocumentReference newArtisanRef = artisanRef.document();

        artisanObject.setPictureURL("profiles/" + newArtisanRef.getId() + ".jpg");
        artisanObject.setID(newArtisanRef.getId());

        final Artisan mewBoi2 = artisanObject;

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference artisanPicRef = storageRef.child(artisanObject.getPictureURL());

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
                            documentReference.update("id", documentReference.getId());

                            loadingSpinner.setVisibility(View.GONE);
                            startActivity(new Intent(FinalPreviewAddArtisanActivity.this, MainActivity.class));
                            finish();
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
