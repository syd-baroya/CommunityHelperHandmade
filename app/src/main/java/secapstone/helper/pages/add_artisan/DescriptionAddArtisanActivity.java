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


public class DescriptionAddArtisanActivity extends AppCompatActivity
{
    CustomTextField desInfo;
    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_add_artisan);

        desInfo = findViewById(R.id.artisanDescription);
        nextButton = findViewById(R.id.nextButton);

        setupTextChangedListener(desInfo);

        desInfo.setText(WelcomeAddArtisanActivity.artisanObject.getDescription());
    }

    public void onClickNext(View view)
    {
        setDescription(desInfo.getText().toString());

        startActivity(new Intent(DescriptionAddArtisanActivity.this, SignatureAddArtisanActivity.class));
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    public void onClickBack(View view)
    {
        startActivity(new Intent(DescriptionAddArtisanActivity.this, MapAddArtisanActivity.class));
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    public void setDescription(String des)
    {
        WelcomeAddArtisanActivity.artisanObject.setDescription(des);
    }


    public void setupTextChangedListener(CustomTextField editText)
    {
        editText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (desInfo.getText().length() > 0) {
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
