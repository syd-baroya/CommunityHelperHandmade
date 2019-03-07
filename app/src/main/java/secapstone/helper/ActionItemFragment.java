package secapstone.helper;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_action_items, container, false);

        // Grab all needed views
        recyclerView = view.findViewById(R.id.action_item_recycler_view);

        // Do everything else
        runActionItemsQuery();


        return view;
    }

    // Update artisan list based on filter and search term variables
    private void runActionItemsQuery() {
        Query query = artisansRef.orderBy("date", Query.Direction.ASCENDING);

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
