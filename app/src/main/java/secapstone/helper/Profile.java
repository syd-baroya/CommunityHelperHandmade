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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import secapstone.helper.R;

import secapstone.helper.addartisan.WelcomeAddArtisanActivity;



public class Profile extends Fragment {

    //reference to user's artisans in database
    private CollectionReference artisansRef;

    private View view;

    private Button logoutButton;
    private Dialog myDialog;

    public Profile() {
        // Required empty public constructor
    }

    public void setArtisanRef(CollectionReference artisansRef){
        this.artisansRef = artisansRef;
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

        User user_info = User.getUser();
        setImage("",  user_info.getName());


        Button logPaymentButton = view.findViewById(R.id.logPaymentButton);
        logPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                onClickLogPayments(view);
            }
        });

        myDialog = new Dialog(getActivity());

        setStatusBarToDark();

        return view;
    }

    public void onClickLogout()
    {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getContext(), LoginActivity.class));
        getActivity().finish(); //Since we are logging out, close MainActivity so you can't use back button.
    }

    private void setImage(String url, String name) {
        TextView nameTitle = view.findViewById(R.id.cga_name);
        nameTitle.setText(name);

//        final ImageView image = findViewById(R.id.artisan_banner_image);
//
//        final Activity thisAct = this;
//
//        if (url != null) {
//            storageRef.child(url).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                @Override
//                public void onSuccess(Uri uri) {
//                    Glide.with(thisAct)
//                            .asBitmap()
//                            .load(uri)
//                            .into(image);
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                    // Handle any errors
//                }
//            });
//        } else {
//            image.setImageResource(R.drawable.ic_empty_person);
//        }
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


    @TargetApi(23)
    public void setStatusBarToDark() {
        int flags = getActivity().getWindow().getDecorView().getSystemUiVisibility();
        flags = flags ^ View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        getActivity().getWindow().getDecorView().setSystemUiVisibility(flags);
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.almostBlack));
    }

}
