package secapstone.helper.pages.artisans_page;


import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import secapstone.helper.pages.custom_ui.EditTextSearch;
import secapstone.helper.pages.add_artisan.WelcomeAddArtisanActivity;
import secapstone.helper.model.Artisan;
import secapstone.helper.R;

/**
 * A simple {@link Fragment} subclass.
 */

public class ArtisansFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "ArtisansFragment";

    private CollectionReference artisansRef;
    private ArtisanAdapter adapter;

    private View view;

    private EditTextSearch artisanSearchField;
    private Button searchButton;
    private RecyclerView recyclerView;
    private Spinner sortBySpinner;
    private Button addArtisanButton;
    Dialog deleteArtisanDialog;

    private String filter = "lastName";
    private String searchTerm = "";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

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
        setUpDeleteArtisanModal();
        runArtisanQuery();
        setStatusBarToWhite();

        return view;
    }

    public void showDeleteArtisanModel(Artisan model) {
        deleteArtisanDialog.show();
        final Artisan modelCopy = model;
        Button deleteButton = deleteArtisanDialog.findViewById(R.id.deleteArtisanButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference toPath = db.collection("deleted-artisans").document();
                DocumentReference artisanRef = artisansRef.document(modelCopy.getID());
                deleteListingsThenMove(artisanRef, toPath, deleteArtisanDialog);
            }
        });

        Button cancelButton = deleteArtisanDialog.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteArtisanDialog.dismiss();
            }
        });
    }

    public void deleteListingsThenMove(final DocumentReference artisanRef, final DocumentReference toPath, Dialog dialog) {
        artisanRef.collection("products")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            document.getReference().delete();
                        }
                        moveFirestoreDocument(artisanRef, toPath, deleteArtisanDialog);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
    }

    public void setUpDeleteArtisanModal() {

        deleteArtisanDialog = new Dialog(this.getContext());

        deleteArtisanDialog.setContentView(R.layout.modal_delete_artisan);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(deleteArtisanDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;

        deleteArtisanDialog.getWindow().setAttributes(lp);
        deleteArtisanDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void moveFirestoreDocument(final DocumentReference fromPath, final DocumentReference toPath, Dialog dialog) {
        fromPath.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        toPath.set(document.getData())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully written!");
                                        fromPath.delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                                        deleteArtisanDialog.dismiss();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error deleting document", e);
                                                    }
                                                });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error writing document", e);
                                    }
                                });
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
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

        adapter = new ArtisanAdapter(options, this.getContext(), artisansRef, this);


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
    public ArtisansFragment() {}

}