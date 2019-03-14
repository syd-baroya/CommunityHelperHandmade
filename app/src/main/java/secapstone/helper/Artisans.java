package secapstone.helper;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.io.Serializable;

import secapstone.helper.addartisan.FinalPreviewAddArtisanActivity;
import secapstone.helper.addartisan.WelcomeAddArtisanActivity;

import static java.security.AccessController.getContext;

/**
 * A simple {@link Fragment} subclass.
 */

public class Artisans extends Fragment implements AdapterView.OnItemSelectedListener {
    private CollectionReference artisansRef;
    private ArtisanAdapter adapter;

    private View view;

    private EditTextSearch artisanSearchField;
    private Button searchButton;
    private RecyclerView recyclerView;
    private Spinner sortBySpinner;
    private Button addArtisanButton;

    private String filter = "lastName";
    private String searchTerm = "";

    public void setArtisanRef(CollectionReference artisansRef){
        this.artisansRef = artisansRef;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_artisans, container, false);

        // Grab all needed views
        artisanSearchField  =  view.findViewById(R.id.searchActionItemsField);
        searchButton        =  view.findViewById(R.id.searchButton);
        recyclerView        =  view.findViewById(R.id.artisan_recycler_view);
        sortBySpinner       =  view.findViewById(R.id.SortBySpinner);
        addArtisanButton    =  view.findViewById(R.id.addArtisanButton);

        // Setup listeners
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSearchButton();
            }
        });
        artisanSearchField.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return onKeySearchField(keyCode, event);
            }
        });
        addArtisanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickAddArtisan();
            }
        });

        // Do everything else
        setUpFilterSpinner();
        runArtisanQuery();
        setStatusBarToWhite();

        return view;
    }

    // Filter Spinner Clicked
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if (pos == 0) {
            filter = "firstName";
        }
        else if (pos == 1) {
            filter = "lastName";
        }

        runArtisanQuery();
    }

    public void onClickAddArtisan() {
        startActivity(new Intent(getContext(), WelcomeAddArtisanActivity.class));
    }

    public void onClickSearchButton() {
        if (artisanSearchField.hasFocus()) {
            artisanSearchField.setText("");
            searchTerm = artisanSearchField.getText().toString();
            runArtisanQuery();
        } else {
            searchTerm = artisanSearchField.getText().toString();
            runArtisanQuery();

            artisanSearchField.requestFocus();

            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(artisanSearchField, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public boolean onKeySearchField(int keyCode, KeyEvent event) {
        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
            searchTerm = artisanSearchField.getText().toString();
            runArtisanQuery();
            return true;
        }
        return false;
    }

    // Update artisan list based on filter and search term variables
    private void runArtisanQuery() {
        Query query = artisansRef.orderBy("firstName", Query.Direction.ASCENDING);

        if (filter.equals("firstName")) {
            query = artisansRef.orderBy("firstName", Query.Direction.ASCENDING);
        }
        else if (filter.equals("lastName")) {
            query = artisansRef.orderBy("lastName", Query.Direction.ASCENDING);
        }

        if (searchTerm.length() > 0) {
            query = query.whereEqualTo("name", searchTerm);
        }

        FirestoreRecyclerOptions<Artisan> options = new FirestoreRecyclerOptions.Builder<Artisan>()
                .setQuery(query, Artisan.class)
                .build();

        adapter = new ArtisanAdapter(options, this.getContext(), artisansRef);


        RecyclerView.LayoutManager m = new LinearLayoutManager(getActivity()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        recyclerView.setLayoutManager(m);

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
        //setStatusBarToWhite();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @TargetApi(23)
    public void setStatusBarToWhite() {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);

        int flags = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        getActivity().getWindow().getDecorView().setSystemUiVisibility(flags);
        getActivity().getWindow().setStatusBarColor(Color.WHITE);
    }

    public void setUpFilterSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.sortArray, R.layout.spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortBySpinner.setAdapter(adapter);
        sortBySpinner.setOnItemSelectedListener(this);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        //don't do anything i think
    }

    // Required empty public constructor
    public Artisans() {}

}