package secapstone.helper.addartisan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.google.android.gms.common.*;
import com.google.android.gms.location.places.*;
import com.google.android.gms.location.places.ui.*;
import secapstone.helper.R;

public class MapAddArtisanActivity extends AppCompatActivity
{
    EditText adr;
    EditText zip;
    EditText country;
    final int PLACE_PICKER_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_add_artisan);

        adr = findViewById(R.id.artisanAddress);
        zip = findViewById(R.id.zipPostalEditText);
        country = findViewById(R.id.countryRegionEditText);

        Button selectOnMapButton = findViewById(R.id.selectOnMapButton);
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
                Place place = PlacePicker.getPlace(this, data);
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
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Amazon", e.getStackTrace().toString());
        }
    }

    public void onClickNext(View view)
    {
        setAddress(adr.getText().toString(), zip.getText().toString(), country.getText().toString());
        setZipPostalCode(zip.getText().toString());
        setCountryRegion(country.getText().toString());
        startActivity(new Intent(MapAddArtisanActivity.this, DescriptionAddArtisanActivity.class));
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    public void onClickBack(View view)
    {
        startActivity(new Intent(MapAddArtisanActivity.this, PhoneNumberAddArtisanActivity.class));
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    public void setAddress(String addr, String zip, String country){

        WelcomeAddArtisanActivity.artisanObject.setAddress(addr + " " + country + ", " + zip);
    }

    public void setZipPostalCode(String zpc){
        WelcomeAddArtisanActivity.artisanObject.setZipPostalCode(zpc);
    }

    public void setCountryRegion(String cr){
        WelcomeAddArtisanActivity.artisanObject.setCountryRegion(cr);
    }
}
