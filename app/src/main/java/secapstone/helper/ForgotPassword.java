package secapstone.helper;

import android.content.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import secapstone.helper.R;

import secapstone.helper.addartisan.*;

public class ForgotPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Button backButton = (Button) findViewById(R.id.forgot_btn_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                onClickReturnToLogin();
            }
        });

    }

    public void onClickReturnToLogin()
    {
        startActivity(new Intent(ForgotPassword.this, LoginActivity.class));
    }
}
