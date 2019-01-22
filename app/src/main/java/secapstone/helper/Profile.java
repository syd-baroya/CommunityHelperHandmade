package secapstone.helper;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import secapstone.helper.R;

import secapstone.helper.addartisan.WelcomeAddArtisanActivity;



public class Profile extends Fragment {

    private View view;

    public Profile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile, container, false);

        Button logoutButton = (Button) view.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickLogout();
            }
        });

        Button addArtisanButton = (Button) view.findViewById(R.id.addArtisanButton);
        addArtisanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickAddArtisan();
            }
        });


        return view;
    }

    public void onClickLogout()
    {
        startActivity(new Intent(getContext(), LoginActivity.class));
        getActivity().finish(); //Since we are logging out, close MainActivity so you can't use back button.
    }

    public void onClickAddArtisan()
    {
        startActivity(new Intent(getContext(), WelcomeAddArtisanActivity.class));
    }

}
