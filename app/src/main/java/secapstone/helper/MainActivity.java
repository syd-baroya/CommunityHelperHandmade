package secapstone.helper;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;

import secapstone.helper.R;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener
{
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = db.collection("users");
    private DocumentReference CGARef;
    private BottomNavigationView bottomNavigationView;

    private Artisans artisanFragment;
    private ActionItemFragment actionItemsFragment;
    private Profile profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        User user_info = User.getUser();
        CGARef = usersRef.document(user_info.getIdToken());

        profileFragment = new Profile();
        profileFragment.setArtisanRef(CGARef.collection("artisans"));

        artisanFragment = new Artisans();
        artisanFragment.setArtisanRef(CGARef.collection("artisans"));

        actionItemsFragment = new ActionItemFragment();
        actionItemsFragment.setActionItemRef(CGARef.collection("Action Items"));


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.navigation_action_items);
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
