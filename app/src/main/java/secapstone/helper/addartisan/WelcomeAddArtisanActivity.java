package secapstone.helper.addartisan;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import secapstone.helper.R;

import secapstone.helper.Artisan;
import secapstone.helper.MainActivity;

public class WelcomeAddArtisanActivity extends AppCompatActivity
{
    public static Artisan artisanObject = new Artisan();
    public static Bitmap artisanProfileImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_add_artisan);

        Button nextButton1 = (Button) findViewById(R.id.nextButton1);
        nextButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickNext1();
            }
        });

        Button backButton1 = (Button) findViewById(R.id.backButton1);
        backButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickBack1();
            }
        });


    }


    public void onClickBack1()
    {
        startActivity(new Intent(WelcomeAddArtisanActivity.this, MainActivity.class));
    }

    public void onClickNext1()
    {
        startActivity(new Intent(WelcomeAddArtisanActivity.this, NameAddArtisanActivity.class));
    }
}
