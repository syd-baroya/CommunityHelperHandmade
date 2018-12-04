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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.firestore.Query;
import com.saga.communityhelperhandmade.R;

import java.util.ArrayList;

import secapstone.helper.addartisan.*;


public class MainActivity extends AppCompatActivity
{

    private static final String TAG = "MainActivity";

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

        //initImageBitmaps();

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


//    private void initImageBitmaps() {
//        mImageUrls.add("https://c1.staticflickr.com/5/4636/25316407448_de5fbf183d_o.jpg");
//        mNames.add("Havasu Falls");
//
//        mImageUrls.add("https://i.redd.it/tpsnoz5bzo501.jpg");
//        mNames.add("Trondheim");
//
//        mImageUrls.add("https://i.redd.it/qn7f9oqu7o501.jpg");
//        mNames.add("Portugal");
//
//        mImageUrls.add("https://i.redd.it/j6myfqglup501.jpg");
//        mNames.add("Rocky Mountain National Park");
//
//
//        mImageUrls.add("https://i.redd.it/0h2gm1ix6p501.jpg");
//        mNames.add("Mahahual");
//
//        mImageUrls.add("https://i.redd.it/k98uzl68eh501.jpg");
//        mNames.add("Frozen Lake");
//
//
//        mImageUrls.add("https://i.redd.it/glin0nwndo501.jpg");
//        mNames.add("White Sands Desert");
//
//        mImageUrls.add("https://i.redd.it/obx4zydshg601.jpg");
//        mNames.add("Austrailia");
//
//        mImageUrls.add("https://i.imgur.com/ZcLLrkY.jpg");
//        mNames.add("Washington");
//
//        initRecyclerView();
//    }
//
//    private void initRecyclerView() {
//        RecyclerView recyclerView = findViewById(R.id.recycler_view);
//        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mNames, mImageUrls, this);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//    }
}
