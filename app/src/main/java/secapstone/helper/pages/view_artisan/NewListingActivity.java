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
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import secapstone.helper.model.Listing;
import secapstone.helper.model.User;
import secapstone.helper.pages.custom_ui.CustomTextField;
import secapstone.helper.R;

public class NewListingActivity extends AppCompatActivity {

    static final int REQUEST_TAKE_PHOTO = 1;
    private static final String TAG = "NewListingActivity";
    public String mCurrentPhotoPath;
    ConstraintLayout loadingSpinner;

    CustomTextField name;
    CustomTextField price;
    CustomTextField description;
    Button uploadButton;
    String artisanID;

    String paymentAmount = "";

    //private static CollectionReference productsRef;
    private Bitmap productImage = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_listing);

        name = findViewById(R.id.nlNameText);
        price = findViewById(R.id.nlPriceText);
        description = findViewById(R.id.nlDescriptionText);
        uploadButton = findViewById(R.id.nlAddListing);
        uploadButton.setEnabled(false);

        setupEnableButtonListener(name);
        setupEnableButtonListener(price);
        setupEnableButtonListener(description);

        setupFormatPriceListener(price);

        loadingSpinner = findViewById(R.id.progress_loader_add);
        getIncomingIntent();

    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("id")) {
            artisanID = getIntent().getStringExtra("id");
        }
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

        if ((name.getText().length() != 0) && (price.getText().length() != 0)
                && (description.getText().length() != 0) && (productImage != null))  {
            uploadButton.setEnabled(true);
        } else {
            uploadButton.setEnabled(false);
        }
    }

    public void onClickBackButton(View view) {
        finish();
    }

    public void onClickAddListingButton(View view) {
        loadingSpinner.setVisibility(View.VISIBLE);
        Listing newListing = new Listing();
        newListing.setTitle(name.getText().toString());
        newListing.setDescription(description.getText().toString());
        newListing.setPrice(Float.parseFloat(price.getText().toString()));
        pushListing(newListing);
    }

    public void pushListing(Listing listingObject) {
        DocumentReference artisanRef = FirebaseFirestore.getInstance().collection("users").document(User.getUser().getID()).collection("artisans").document(artisanID);
        CollectionReference newProductRef = artisanRef.collection("products");
        DocumentReference newProductRefTempDoc = newProductRef.document();

        listingObject.setPictureURL("products/" + newProductRefTempDoc.getId() + ".jpg");
        listingObject.setID(newProductRefTempDoc.getId());

        final Listing mewBoi2 = listingObject;
        final CollectionReference finalNewProductRef = newProductRef;

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference productPicRef = storageRef.child(listingObject.getPictureURL());

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
                finalNewProductRef
                        .add(mewBoi2)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>(){
                            @Override
                            public void onSuccess(DocumentReference documentReference){
                                Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                documentReference.update("id", documentReference.getId());
                                loadingSpinner.setVisibility(View.GONE);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener(){
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                                loadingSpinner.setVisibility(View.GONE);
                            }
                        });
            }
        });
    }

    public void setupEnableButtonListener(CustomTextField editText)
    {
        editText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if ((name.getText().length() != 0) && (price.getText().length() != 0)
                        && (description.getText().length() != 0) && (productImage != null))  {
                        uploadButton.setEnabled(true);
                } else {
                    uploadButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {}

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,int arg2, int arg3) {}
        });
    }

    public void setupFormatPriceListener(final CustomTextField amountField)
    {
        amountField.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(!s.toString().equals(paymentAmount)){
                    String cleanString = s.toString().replaceAll("[$,.]", "");

                    if (cleanString.length() > 0) {
                        double parsed = Double.parseDouble(cleanString);
                        String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));

                        paymentAmount = formatted;
                        amountField.setText(formatted);
                        amountField.setSelection(formatted.length());
                    } else {
                        String formatted = "";

                        paymentAmount = formatted;
                        amountField.setText(formatted);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {}

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,int arg2, int arg3) {}
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
