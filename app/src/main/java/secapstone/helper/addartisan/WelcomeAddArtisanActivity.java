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
    }


    public void onClickBack(View view)
    {
        startActivity(new Intent(WelcomeAddArtisanActivity.this, MainActivity.class));
        artisanObject = new Artisan();
    }

    public void onClickNext(View view)
    {
        startActivity(new Intent(WelcomeAddArtisanActivity.this, NameAddArtisanActivity.class));
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }
}
