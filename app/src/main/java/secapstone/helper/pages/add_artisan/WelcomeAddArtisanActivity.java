package secapstone.helper.pages.add_artisan;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import secapstone.helper.R;

import secapstone.helper.model.Artisan;
import secapstone.helper.pages.MainActivity;

public class WelcomeAddArtisanActivity extends AppCompatActivity
{
    public static Artisan artisanObject = new Artisan();
    public static Bitmap artisanProfileImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_add_artisan);
        setNavbarColorToBlue();
    }


    public void onClickBack(View view)
    {
        startActivity(new Intent(WelcomeAddArtisanActivity.this, MainActivity.class));
        artisanObject = new Artisan();
        artisanProfileImage = null;
    }

    public void onClickNext(View view)
    {
        startActivity(new Intent(WelcomeAddArtisanActivity.this, NameAddArtisanActivity.class));
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    @TargetApi(23)
    public void setNavbarColorToBlue() {
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }
}
