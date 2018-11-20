package secapstone.helper;

import android.content.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import com.saga.communityhelperhandmade.R;


public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set the logout button's "onClick" callback to local onClickLogout function.
        Button logoutButton = (Button) findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickLogout();
            }
        });

    }

    public void onClickLogout()
    {
        //Do anything before logging out.


        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish(); //Since we are logging out, close MainActivity so you can't use back button.
    }
}
