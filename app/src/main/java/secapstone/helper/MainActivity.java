package secapstone.helper;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;

import com.google.firebase.FirebaseApp;

import secapstone.helper.R;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener
{

    private BottomNavigationView bottomNavigationView;

    private Artisans artisanFragment = new Artisans();
    private ActionItems actionItemsFragment = new ActionItems();
    private Profile profileFragment = new Profile();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.navigation_artisans );
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.navigation_artisans:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, artisanFragment).commit();
                return true;
            case R.id.navigation_action_items:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, actionItemsFragment).commit();
                return true;
            case R.id.navigation_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
                return true;
        }

        return false;
    }
}
