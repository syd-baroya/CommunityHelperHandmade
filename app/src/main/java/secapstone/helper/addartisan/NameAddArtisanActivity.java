package secapstone.helper.addartisan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import secapstone.helper.CustomTextField;
import secapstone.helper.R;

public class NameAddArtisanActivity extends AppCompatActivity
{
    CustomTextField inputFirst, inputLast;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_add_artisan);

        inputFirst = findViewById(R.id.artisanAddress);
        inputLast = findViewById(R.id.zipPostalEditText);

    }

    public void onClickNext(View view)
    {
        setName(inputFirst.getText().toString(), inputLast.getText().toString());
        setFirstName(inputFirst.getText().toString());
        setLastName(inputLast.getText().toString());
        startActivity(new Intent(NameAddArtisanActivity.this, PhotoAddArtisanActivity.class));
    }

    public void onClickBack(View view)
    {
        startActivity(new Intent(NameAddArtisanActivity.this, WelcomeAddArtisanActivity.class));
    }

    public void setName(String first, String last)
    {
        WelcomeAddArtisanActivity.artisanObject.setName(first + " " + last);
    }

    public void setFirstName(String f){
        WelcomeAddArtisanActivity.artisanObject.setFirstName(f);
    }

    public void setLastName(String l){
        WelcomeAddArtisanActivity.artisanObject.setLastName(l);
    }
}
