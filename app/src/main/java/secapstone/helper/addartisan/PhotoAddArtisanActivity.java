package secapstone.helper.addartisan;

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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_add_artisan);

        Button nextButton = findViewById(R.id.nextButton);
        Button backButton = findViewById(R.id.backButton);
        Button photoButton = findViewById(R.id.uploadPhotoButton);

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

        ImageView image = findViewById(R.id.imageView);
        image.setImageBitmap(bmp);
        WelcomeAddArtisanActivity.artisanProfileImage = bmp;
    }

    public void onClickNext(View view)
    {
        startActivity(new Intent(PhotoAddArtisanActivity.this, PhoneNumberAddArtisanActivity.class));
    }

    public void onClickBack(View view)
    {
        startActivity(new Intent(PhotoAddArtisanActivity.this, NameAddArtisanActivity.class));
    }
}
