package secapstone.helper.addartisan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.saga.communityhelperhandmade.*;

public class MapAddArtisanActivity extends AppCompatActivity
{
    EditText adr;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_add_artisan);

        Button nextButton5 = (Button) findViewById(R.id.nextButton5);
        nextButton5.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onClickNext5();
            }
        });

        Button backButton5 = (Button) findViewById(R.id.backButton5);
        backButton5.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onClickBack5();
            }
        });
        adr = (EditText)findViewById(R.id.artisanAddress);
    }

    public void onClickNext5()
    {
        setAddress(adr.getText().toString());
        startActivity(new Intent(MapAddArtisanActivity.this, DescriptionAddArtisanActivity.class));
    }

    public void onClickBack5()
    {
        startActivity(new Intent(MapAddArtisanActivity.this, PhoneNumberAddArtisanActivity.class));
    }

    public void setAddress(String addr){
        WelcomeAddArtisanActivity.mew.setAddress(addr);
    }
}
