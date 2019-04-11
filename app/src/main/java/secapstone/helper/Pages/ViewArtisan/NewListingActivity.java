package secapstone.helper.Pages.ViewArtisan;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import secapstone.helper.Pages.CustomUI.CustomTextField;
import secapstone.helper.R;

public class NewListingActivity extends AppCompatActivity {

    static final int REQUEST_TAKE_PHOTO = 1;
    public String mCurrentPhotoPath;

    CustomTextField name;
    CustomTextField price;
    CustomTextField description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_listing);

        name = findViewById(R.id.nlNameText);
        price = findViewById(R.id.nlPriceText);
        description = findViewById(R.id.nlDescriptionText);

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

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
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
        }
    }

    public void onClickBackButton(View view) {
        finish();
    }

    public void onClickAddListingButton(View view) {
        finish();
    }
}