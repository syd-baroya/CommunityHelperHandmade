package secapstone.helper;


import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import secapstone.helper.R;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


/**
 * A simple {@link Fragment} subclass.
 */
public class Artisans extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference artisansRef = db.collection("artisans");
    private FirestoreRecyclerAdapter adapter;

    private View view;

    TextView artisanSearchField;
    Button searchButton;
    RecyclerView recyclerView;


    public Artisans() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_artisans, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        artisanSearchField = (TextView) view.findViewById(R.id.searchArtisanField);
        searchButton = (Button) view.findViewById(R.id.searchArtisanButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseSearchArtisans();
            }
        });
        recyclerView = view.findViewById(R.id.artisan_recycler_view);

        firebaseSearchArtisans();

        TextView.OnEditorActionListener exampleListener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView exampleView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    firebaseSearchArtisans();
                }

                return true;
            }
        };

        artisanSearchField.setOnEditorActionListener(exampleListener);
        artisanSearchField.setImeActionLabel("Search", KeyEvent.KEYCODE_ENTER);
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
