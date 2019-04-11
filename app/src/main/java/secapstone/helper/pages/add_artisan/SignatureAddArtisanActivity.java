package secapstone.helper.pages.add_artisan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import secapstone.helper.R;


public class SignatureAddArtisanActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature_add_artisan);
    }

    public void onClickNext(View view)
    {
        startActivity(new Intent(SignatureAddArtisanActivity.this, FinalPreviewAddArtisanActivity.class));
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    public void onClickBack(View view)
    {
        startActivity(new Intent(SignatureAddArtisanActivity.this, DescriptionAddArtisanActivity.class));
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }


}
