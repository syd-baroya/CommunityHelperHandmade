package secapstone.helper.addartisan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import secapstone.helper.CustomTextField;
import secapstone.helper.R;

public class PhoneNumberAddArtisanActivity extends AppCompatActivity
{
    CustomTextField pNum;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_add_artisan);

        pNum = findViewById(R.id.phoneNumText);
    }

    public void onClickNext(View view)
    {
        setNum(pNum.getText().toString());
        startActivity(new Intent(PhoneNumberAddArtisanActivity.this, MapAddArtisanActivity.class));
    }

    public void onClickBack(View view)
    {
        startActivity(new Intent(PhoneNumberAddArtisanActivity.this, PhotoAddArtisanActivity.class));
    }

    public void setNum(String num) {
        WelcomeAddArtisanActivity.artisanObject.setPhoneNumber(num);
    }
}
