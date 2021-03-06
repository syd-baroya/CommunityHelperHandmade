package secapstone.helper.pages.add_artisan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import secapstone.helper.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoAddArtisanActivity extends AppCompatActivity
{

    static final int REQUEST_TAKE_PHOTO = 1;
    public String mCurrentPhotoPath;

    Button nextButton, backButton, photoButton;
    ImageView photoPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_add_artisan);

        nextButton = findViewById(R.id.nextButton);
        //backButton = findViewById(R.id.backButton);
        photoButton = findViewById(R.id.uploadPhotoButton);
        photoPreview = findViewById(R.id.imageView);

        setImageViewImage(WelcomeAddArtisanActivity.artisanProfileImage);
    }

    public void onClickSelectPhoto(View view)
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

        setImageViewImage(bmp);
    }

    public void setImageViewImage(Bitmap bmp) {
        if (bmp != null) {
            ImageView image = findViewById(R.id.imageView);
            image.setImageBitmap(bmp);
            WelcomeAddArtisanActivity.artisanProfileImage = bmp;

            nextButton.setEnabled(true);
        }
    }


    public void onClickNext(View view)
    {
        startActivity(new Intent(PhotoAddArtisanActivity.this, PhoneNumberAddArtisanActivity.class));
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
    }

    public void onClickBack(View view)
    {
        startActivity(new Intent(PhotoAddArtisanActivity.this, NameAddArtisanActivity.class));
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        finish();
    }
}
