package secapstone.helper.addartisan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.google.android.gms.common.*;
import com.google.android.gms.location.places.*;
import com.google.android.gms.location.places.ui.*;
import com.saga.communityhelperhandmade.*;
import com.saga.communityhelperhandmade.R;

public class MapAddArtisanActivity extends AppCompatActivity
{
    EditText adr;
    final int PLACE_PICKER_REQUEST = 1;

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

        Button selectOnMapButton = (Button) findViewById(R.id.selectOnMapButton);
        selectOnMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            { onClickLaunchGoogleMaps();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                adr.setText(place.getLatLng().toString());
            }
        }
    }

    public void onClickLaunchGoogleMaps()
    {
        try
        {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
            //TODO Alert the user that Google maps isn't availible right now.
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        }
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
