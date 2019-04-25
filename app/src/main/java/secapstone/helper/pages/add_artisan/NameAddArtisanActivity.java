package secapstone.helper.pages.add_artisan;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import secapstone.helper.pages.custom_ui.CustomTextField;
import secapstone.helper.R;

public class NameAddArtisanActivity extends AppCompatActivity
{
    CustomTextField inputFirst, inputLast;
    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_add_artisan);

        inputFirst = findViewById(R.id.artisanAddress);
        inputLast = findViewById(R.id.zipPostalEditText);
        nextButton = findViewById(R.id.nextButton);

        setupTextChangedListener(inputFirst);
        setupTextChangedListener(inputLast);

        inputFirst.setText(WelcomeAddArtisanActivity.artisanObject.getFirstName());
        inputLast.setText(WelcomeAddArtisanActivity.artisanObject.getLastName());
    }

    public void onClickNext(View view)
    {
        setName(inputFirst.getText().toString(), inputLast.getText().toString());
        setFirstName(inputFirst.getText().toString());
        setLastName(inputLast.getText().toString());
        startActivity(new Intent(NameAddArtisanActivity.this, PhotoAddArtisanActivity.class));
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    public void onClickBack(View view)
    {
        startActivity(new Intent(NameAddArtisanActivity.this, WelcomeAddArtisanActivity.class));
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
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

    public void setupTextChangedListener(CustomTextField editText)
    {
        editText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if ((inputFirst.getText().length() != 0) && (inputLast.getText().length() != 0)) {
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
