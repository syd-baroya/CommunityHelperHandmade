package secapstone.helper;


import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazon.identity.auth.device.AuthError;
import com.amazon.identity.auth.device.api.Listener;
import com.amazon.identity.auth.device.api.authorization.AuthorizationManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import secapstone.helper.R;
import android.support.v7.widget.RecyclerView;
import secapstone.helper.addartisan.WelcomeAddArtisanActivity;

import static java.security.AccessController.getContext;


public class Profile extends Fragment {

    //reference to user's artisans in database
    private CollectionReference artisansRef;

    private View view;

    private Button logoutButton;
    private Dialog myDialog;
    private RecyclerView recyclerView;
    private ArtisanAdapter adapter;
    public void setArtisanRef(CollectionReference artisansRef){
        this.artisansRef = artisansRef;
    }

    public Profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile, container, false);

        logoutButton = view.findViewById(R.id.logoutButton);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickLogout();
            }
        });
        recyclerView=  view.findViewById(R.id.ArtisanRecyclerView);

        User user_info = User.getUser();
        setImage("",  user_info.getName());


        /*Button logPaymentButton = view.findViewById(R.id.logPaymentButton);
        //logPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                onClickLogPayments(view);
            }
        });*/

        myDialog = new Dialog(getActivity());

        setStatusBarToDark();
        makeRecyclerView();

        return view;
    }

    public void onClickLogout()
    {
        FirebaseAuth.getInstance().signOut();

        AuthorizationManager.signOut(getContext(), new Listener< Void, AuthError >() {
            @Override
            public void onSuccess(Void response) {
                // Set logged out state in UI

                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
            }
            @Override
            public void onError(AuthError authError) {
                // Log the error
            }
        });
    }

    private void setImage(String url, String name) {
        TextView nameTitle = view.findViewById(R.id.cga_name);
        nameTitle.setText(name);
    }


    public void onClickReportsButton(View view)
    {
        startActivity(new Intent(getActivity(), ViewReportsActivity.class));
    }

    public void onClickLogPayments(View view)
    {
        startActivity(new Intent(getActivity(), LogPaymentActivity.class));
    }

    public void onClickBackButton(View view)
    {
        startActivity(new Intent(getActivity(), MainActivity.class));
    }

    public void onClickContactInfoButton(View view)
    {

    }

    public void makeRecyclerView(){
        Query query = artisansRef.orderBy("lastName", Query.Direction.ASCENDING);
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


//        adapter = new ArtisanAdapter(options, getContext(), artisansRef);
//
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
//        adapter.startListening();
    }


    @TargetApi(23)
    public void setStatusBarToDark() {
        int flags = getActivity().getWindow().getDecorView().getSystemUiVisibility();
        flags = flags ^ View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        getActivity().getWindow().getDecorView().setSystemUiVisibility(flags);
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.almostBlack));
    }

}
