package secapstone.helper;

import android.content.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.*;


import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.saga.communityhelperhandmade.R;

import java.util.ArrayList;

import secapstone.helper.addartisan.*;


public class MainActivity extends AppCompatActivity
{

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference artisansRef = db.collection("artisans");
    private ArtisanAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set the logout button's "onClick" callback to local onClickLogout function.
        Button logoutButton = (Button) findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickLogout();
            }
        });

        Button addArtisanButton = (Button) findViewById(R.id.addArtisanButton);
        addArtisanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickAddArtisan();
            }
        });

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        Query query = artisansRef.orderBy("name", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Artisan> options = new FirestoreRecyclerOptions.Builder<Artisan>()
                .setQuery(query, Artisan.class)
                .build();

        adapter = new ArtisanAdapter(options, this);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public void onClickLogout()
    {
        //Do anything before logging out.

        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish(); //Since we are logging out, close MainActivity so you can't use back button.
    }

    public void onClickAddArtisan()
    {
        startActivity(new Intent(MainActivity.this, WelcomeAddArtisanActivity.class));
    }
}
