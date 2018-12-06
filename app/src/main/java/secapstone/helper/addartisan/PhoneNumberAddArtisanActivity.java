package secapstone.helper.addartisan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.saga.communityhelperhandmade.*;

import java.time.Instant;

public class PhoneNumberAddArtisanActivity extends AppCompatActivity
{
    EditText pNum;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_add_artisan);

        Button nextButton4 = (Button) findViewById(R.id.nextButton4);
        nextButton4.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onClickNext4();
            }
        });

        Button backButton4 = (Button) findViewById(R.id.backButton4);
        backButton4.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onClickBack4();
            }
        });

        pNum = (EditText)findViewById(R.id.phoneNumText);
    }

    public void onClickNext4()
    {
        setNum(pNum.getText().toString());
        startActivity(new Intent(PhoneNumberAddArtisanActivity.this, MapAddArtisanActivity.class));
    }

    public void onClickBack4()
    {
        startActivity(new Intent(PhoneNumberAddArtisanActivity.this, PhotoAddArtisanActivity.class));
    }

    public void setNum(String num)
    {
        WelcomeAddArtisanActivity.mew.setPhoneNumber(num);
    }
}
