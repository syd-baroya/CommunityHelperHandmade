package secapstone.helper;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import secapstone.helper.R;

import secapstone.helper.addartisan.WelcomeAddArtisanActivity;



public class Profile extends Fragment {

    private View view;

    Button logoutButton;
    ImageView artisanProfileImage;

    public Profile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile, container, false);

        logoutButton = view.findViewById(R.id.logoutButton);
        artisanProfileImage = view.findViewById(R.id.artisan_banner_image);
        //artisanProfileImage.setImageDrawable(getResources().getDrawable(R.drawable.example_artisan));

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickLogout();
            }
        });

        return view;
    }

    public void onClickLogout()
    {
        startActivity(new Intent(getContext(), LoginActivity.class));
        getActivity().finish(); //Since we are logging out, close MainActivity so you can't use back button.
    }

}
