package secapstone.helper.addartisan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.saga.communityhelperhandmade.*;

public class NameAddArtisanActivity extends AppCompatActivity
{
    EditText inputFirst, inputLast;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_add_artisan);

        inputFirst = (EditText)findViewById(R.id.firstNameInput);
        inputLast = (EditText)findViewById(R.id.lastNameInput);

        Button nextButton2 = (Button) findViewById(R.id.nextButton2);
        nextButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                onClickNext2();
            }
        });

        Button backButton2 = (Button) findViewById(R.id.backButton2);
        backButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                onClickBack2();
            }
        });
    }

    public void onClickNext2()
    {
        setName(inputFirst.getText().toString(), inputLast.getText().toString());
        startActivity(new Intent(NameAddArtisanActivity.this, PhotoAddArtisanActivity.class));
    }

    public void onClickBack2()
    {
        startActivity(new Intent(NameAddArtisanActivity.this, WelcomeAddArtisanActivity.class));
    }

    public void setName(String first, String last)
    {
        WelcomeAddArtisanActivity.artisanObject.setName(first + " " + last);
    }
}
