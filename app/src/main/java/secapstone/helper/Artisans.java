package secapstone.helper;


import android.annotation.TargetApi;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.*;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import secapstone.helper.addartisan.WelcomeAddArtisanActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class Artisans extends Fragment implements AdapterView.OnItemSelectedListener {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference artisansRef = db.collection("artisans");
    private ArtisanAdapter adapter;

    private View view;

    private TextView artisanSearchField;
    private Button searchButton;
    private RecyclerView recyclerView;
    private Spinner sortBySpinner;
    private Button addArtisanButton;

    public Artisans() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_artisans, container, false);

        artisanSearchField = view.findViewById(R.id.searchArtisanField);
        searchButton = view.findViewById(R.id.searchArtisanButton);
        recyclerView = view.findViewById(R.id.artisan_recycler_view);
        sortBySpinner = view.findViewById(R.id.SortBySpinner);
        addArtisanButton = view.findViewById(R.id.addArtisanButton);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.sortArray, R.layout.spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortBySpinner.setAdapter(adapter);
        sortBySpinner.setOnItemSelectedListener(this);

        setUpRecyclerView("lastName");

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                artisanSearchField.setText("");
                firebaseSearchArtisans();
            }
        });

        artisanSearchField.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    firebaseSearchArtisans();
                    return true;
                }

                return false;
            }
        });


        addArtisanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickAddArtisan();
            }
        });

        firebaseSearchArtisans();

        return view;
    }



    private void setUpRecyclerView(String sortBy) {
        Query query = artisansRef.orderBy(sortBy, Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Artisan> options = new FirestoreRecyclerOptions.Builder<Artisan>()
                .setQuery(query, Artisan.class)
                .build();

        adapter = new ArtisanAdapter(options, getContext());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }


    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){

        Query query = artisansRef.orderBy("firstName", Query.Direction.ASCENDING);
        if (pos == 1){
            //System.out.println("First Name");
            Log.d("info", "First Name");
            //setUpRecyclerView("firstName");
            query = artisansRef.orderBy("firstName", Query.Direction.ASCENDING);
        }
        else if (pos == 2){
            //System.out.println("Last Name");
            //setUpRecyclerView("lastName");
            query = artisansRef.orderBy("lastName", Query.Direction.ASCENDING);
        }

        FirestoreRecyclerOptions<Artisan> options = new FirestoreRecyclerOptions.Builder<Artisan>()
                .setQuery(query, Artisan.class)
                .build();

        adapter = new ArtisanAdapter(options, getContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.startListening();

    }

    public void onNothingSelected(AdapterView<?> parent){
        //don't do anything i think
    }

    private void firebaseSearchArtisans() {
        if (adapter != null) {
            adapter.stopListening();
        }

        Query query;
        String search = artisanSearchField.getText().toString();

        if (search.length() < 1) {
            query = artisansRef.orderBy("name", Query.Direction.ASCENDING);
        } else {
            query = artisansRef.whereEqualTo("name", search).orderBy("name", Query.Direction.ASCENDING);
        }

        FirestoreRecyclerOptions<Artisan> options = new FirestoreRecyclerOptions.Builder<Artisan>()
                .setQuery(query, Artisan.class)
                .build();

        adapter = new ArtisanAdapter(options, getContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.startListening();
    }

    public void onClickAddArtisan()
    {
        startActivity(new Intent(getContext(), WelcomeAddArtisanActivity.class));
    }


    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}