package secapstone.helper.addartisan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.saga.communityhelperhandmade.*;

import secapstone.helper.Artisan;
import secapstone.helper.MainActivity;

public class SignatureAddArtisanActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature_add_artisan);

        Button nextButton7 = (Button) findViewById(R.id.nextButton7);
        nextButton7.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onNextClick7();
            }
        });

        Button backButton7 = (Button) findViewById(R.id.backButton7);
        backButton7.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onClickBack7();
            }
        });
    }

    public void onNextClick7()
    {
        pushArtisan(WelcomeAddArtisanActivity.mew);
        startActivity(new Intent(SignatureAddArtisanActivity.this, MainActivity.class));
    }

    public void onClickBack7()
    {
        startActivity(new Intent(SignatureAddArtisanActivity.this, DescriptionAddArtisanActivity.class));
    }

    public void pushArtisan(Artisan mewBoi){
        //so here is where i would, ideally, push the info to the firebase
        //but i dont know anything about that
    }
}
