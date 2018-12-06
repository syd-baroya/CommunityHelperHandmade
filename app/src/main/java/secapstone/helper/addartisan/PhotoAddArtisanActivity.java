package secapstone.helper.addartisan;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.saga.communityhelperhandmade.*;

public class PhotoAddArtisanActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_add_artisan);

        Button nextButton3 = (Button) findViewById(R.id.nextButton3);
        nextButton3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onClickNext3();
            }
        });

        Button backButton3 = (Button) findViewById(R.id.backButton3);
        backButton3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onClickBack3();
            }
        });

        Button photoButton = (Button) findViewById(R.id.uploadPhotoButton);
        photoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onClickSelectPhoto();
            }
        });

    }

    public void onClickSelectPhoto()
    {
        
    }

    public void onClickNext3()
    {
        startActivity(new Intent(PhotoAddArtisanActivity.this, PhoneNumberAddArtisanActivity.class));
    }

    public void onClickBack3()
    {
        startActivity(new Intent(PhotoAddArtisanActivity.this, NameAddArtisanActivity.class));
    }
}
