package secapstone.helper.addartisan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.saga.communityhelperhandmade.*;


public class DescriptionAddArtisanActivity extends AppCompatActivity
{
    EditText desInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_add_artisan);

        Button nextButton6 = (Button) findViewById(R.id.nextButton6);
        nextButton6.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onClickNext6();
            }
        });

        Button backButton6 = (Button) findViewById(R.id.backButton6);
        backButton6.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onClickBack6();
            }
        });

        desInfo = (EditText)findViewById(R.id.artisanDescription);
    }

    public void onClickNext6()
    {
        setDescription(desInfo.getText().toString());
        startActivity(new Intent(DescriptionAddArtisanActivity.this, SignatureAddArtisanActivity.class));
    }

    public void onClickBack6()
    {
        startActivity(new Intent(DescriptionAddArtisanActivity.this, MapAddArtisanActivity.class));
    }

    public void setDescription(String des)
    {
        WelcomeAddArtisanActivity.mew.setDescription(des);
    }
}
