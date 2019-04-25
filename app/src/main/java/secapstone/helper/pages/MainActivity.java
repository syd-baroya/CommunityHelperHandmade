package secapstone.helper.pages;

import android.content.DialogInterface;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import secapstone.helper.pages.action_items.ActionItemFragment;
import secapstone.helper.pages.artisans_page.ArtisansFragment;
import secapstone.helper.model.User;
import secapstone.helper.pages.profile_page.ProfileFragment;
import secapstone.helper.R;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener
{
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = db.collection("users");
    private DocumentReference CGARef;
    private BottomNavigationView bottomNavigationView;

    private ArtisansFragment artisanFragment;
    private ActionItemFragment actionItemsFragment;
    private ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        User user_info = User.getUser();
        CGARef = usersRef.document(user_info.getID());

        profileFragment = new ProfileFragment();
        profileFragment.setArtisanRef(CGARef.collection("artisans"));

        artisanFragment = new ArtisansFragment();
        artisanFragment.setArtisanRef(CGARef.collection("artisans"));

        actionItemsFragment = new ActionItemFragment();
        actionItemsFragment.setActionItemRef(CGARef.collection("Action Items"));


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.navigation_artisans);
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

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
