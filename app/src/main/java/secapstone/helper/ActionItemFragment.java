package secapstone.helper;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


/**
 * A simple {@link Fragment} subclass.
 */
public class ActionItemFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference artisansRef = db.collection("Action Items");
    private ActionItemAdapter adapter;

    private View view;

    private RecyclerView recyclerView;
    private EditTextSearch artisanSearchField;
    private Button searchButton;

    private String searchTerm = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_action_items, container, false);

        // Grab all needed views
        recyclerView = view.findViewById(R.id.action_item_recycler_view);
        artisanSearchField  =  view.findViewById(R.id.searchActionItemsField);
        searchButton        =  view.findViewById(R.id.searchButton);

        // Set Listeners
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

        // Do everything else
        runActionItemsQuery();


        return view;
    }

    public void onClickSearchButton() {
        if (artisanSearchField.hasFocus()) {
            artisanSearchField.setText("");
            searchTerm = artisanSearchField.getText().toString();
            runActionItemsQuery();
        } else {
            searchTerm = artisanSearchField.getText().toString();
            runActionItemsQuery();

            artisanSearchField.requestFocus();

            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(artisanSearchField, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public boolean onKeySearchField(int keyCode, KeyEvent event) {
        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
            searchTerm = artisanSearchField.getText().toString();
            runActionItemsQuery();
            return true;
        }
        return false;
    }

    // Update artisan list based on filter and search term variables
    private void runActionItemsQuery() {
        Query query = artisansRef.orderBy("date", Query.Direction.ASCENDING);

        if (searchTerm.length() > 0) {
            query = query.whereEqualTo("action", searchTerm);
        }

        FirestoreRecyclerOptions<ActionItem> options = new FirestoreRecyclerOptions.Builder<ActionItem>()
                .setQuery(query, ActionItem.class)
                .build();

        adapter = new ActionItemAdapter(options, getContext());

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

    public ActionItemFragment() {
        // Required empty public constructor
    }
}
