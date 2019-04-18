package secapstone.helper.pages.view_artisan;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import secapstone.helper.model.Artisan;
import secapstone.helper.model.Listing;
import secapstone.helper.pages.MainActivity;
import secapstone.helper.pages.add_artisan.FinalPreviewAddArtisanActivity;
import secapstone.helper.pages.add_artisan.WelcomeAddArtisanActivity;
import secapstone.helper.pages.custom_ui.CustomTextField;
import secapstone.helper.R;

public class NewListingActivity extends AppCompatActivity {

    static final int REQUEST_TAKE_PHOTO = 1;
    private static final String TAG = "NewListingActivity";
    public String mCurrentPhotoPath;

    CustomTextField name;
    CustomTextField price;
    CustomTextField description;
    private static CollectionReference productsRef;
    private static Bitmap productImage = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_listing);

        name = findViewById(R.id.nlNameText);
        price = findViewById(R.id.nlPriceText);
        description = findViewById(R.id.nlDescriptionText);

    }

    public static void setArtisanRef( CollectionReference ref){
        productsRef = ref;
    }

    public void onClickNewListingPhoto(View view)
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    private File createImageFile() throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,   /* prefix */
                ".jpg",   /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bmp = null;
        try {
            bmp = BitmapFactory.decodeFile(mCurrentPhotoPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setImageNewListingImage(bmp);
    }

    public void setImageNewListingImage(Bitmap bmp) {
        if (bmp != null) {
            ImageView image = findViewById(R.id.newListingImage);
            image.setImageBitmap(bmp);
            productImage = bmp;
        }
    }

    public void onClickBackButton(View view) {
        finish();
    }

    public void onClickAddListingButton(View view) {
        Listing newListing = new Listing();
        newListing.setTitle(name.getText().toString());
        newListing.setDescription(description.getText().toString());
        newListing.setPrice(Float.parseFloat(price.getText().toString()));
        pushListing(newListing);
        finish();
    }

    public void pushListing(Listing mewBoi) {
        DocumentReference newProductRef = productsRef.document();
        mewBoi.setPictureURL("products/" + newProductRef.getId() + ".jpg");
        mewBoi.setProductID(newProductRef.getId());

        final Listing mewBoi2 = mewBoi;

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference productPicRef = storageRef.child(mewBoi.getPictureURL());

        Bitmap bitmap = getResizedBitmap(productImage, 1920);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = productPicRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                productsRef
                        .add(mewBoi2)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>(){
                            @Override
                            public void onSuccess(DocumentReference documentReference){
                                Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                //loadingSpinner.setVisibility(View.GONE);
                                startActivity(new Intent(NewListingActivity.this, ViewArtisanActivity.class));
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
