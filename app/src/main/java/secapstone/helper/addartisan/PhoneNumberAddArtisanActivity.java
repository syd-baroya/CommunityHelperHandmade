package secapstone.helper.addartisan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import secapstone.helper.CustomTextField;
import secapstone.helper.R;

public class PhoneNumberAddArtisanActivity extends AppCompatActivity
{
    CustomTextField pNum;
    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_add_artisan);

        pNum = findViewById(R.id.phoneNumText);
        nextButton = findViewById(R.id.nextButton);

        setupTextChangedListener(pNum);

        pNum.setText(WelcomeAddArtisanActivity.artisanObject.getPhoneNumber());
    }

    public void onClickNext(View view)
    {
        setNum(pNum.getText().toString());
        startActivity(new Intent(PhoneNumberAddArtisanActivity.this, MapAddArtisanActivity.class));
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    public void onClickBack(View view)
    {
        startActivity(new Intent(PhoneNumberAddArtisanActivity.this, PhotoAddArtisanActivity.class));
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    public void setNum(String num) {
        WelcomeAddArtisanActivity.artisanObject.setPhoneNumber(num);
    }


    public void setupTextChangedListener(CustomTextField editText)
    {
        editText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (pNum.getText().length() > 6) {
                    nextButton.setEnabled(true);
                } else {
                    nextButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {}

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,int arg2, int arg3) {}
        });
    }
}
