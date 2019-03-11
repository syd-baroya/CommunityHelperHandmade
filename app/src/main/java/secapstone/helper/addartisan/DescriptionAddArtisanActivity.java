package secapstone.helper.addartisan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import secapstone.helper.R;


public class DescriptionAddArtisanActivity extends AppCompatActivity
{
    EditText desInfo;
    EditText craftInfo;
    EditText howItsDoneInfo;
    EditText inspirationInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_add_artisan);

        desInfo = findViewById(R.id.artisanDescription);
        craftInfo = findViewById(R.id.craftEditText);
        howItsDoneInfo = findViewById(R.id.howItsMadeEditText);
        inspirationInfo = findViewById(R.id.inspirationEditText);
    }

    public void onClickNext(View view)
    {
        setDescription(desInfo.getText().toString());
//        setCraft(craftInfo.getText().toString());
//        setHowItsMade(howItsDoneInfo.getText().toString());
//        setInspiration(inspirationInfo.getText().toString());

        startActivity(new Intent(DescriptionAddArtisanActivity.this, SignatureAddArtisanActivity.class));
    }

    public void onClickBack(View view)
    {
        startActivity(new Intent(DescriptionAddArtisanActivity.this, MapAddArtisanActivity.class));
    }

    public void setDescription(String des)
    {
        WelcomeAddArtisanActivity.artisanObject.setDescription(des);
    }

    public void setCraft(String c){
        WelcomeAddArtisanActivity.artisanObject.setCraft(c);
    }

    public void setHowItsMade(String him){
        WelcomeAddArtisanActivity.artisanObject.setHowItsMade(him);
    }

    public void setInspiration(String i){
        WelcomeAddArtisanActivity.artisanObject.setInspiration(i);
    }
}
