package secapstone.helper;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;

import com.saga.communityhelperhandmade.R;

/**
 * A login screen that offers login via email/password.
 *
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnLogin;
    EditText input_email, input_password;
    TextView btnForgotPass;

    RelativeLayout activity_login;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //view
        btnLogin = (Button)findViewById(R.id.login_btn_login);
        input_email = (EditText)findViewById(R.id.login_email);
        input_password = (EditText)findViewById(R.id.login_password);
        btnForgotPass = (TextView)findViewById(R.id.login_btn_forgot_password);
        activity_login = (RelativeLayout)findViewById(R.id.activity_login);

        btnForgotPass.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        //init firebase auth
//        mAuth = FirebaseAuth.getInstance();

        //check already session, if->dashboard
        //create dashboard activity
        /*
        * if(auth.getCurrentUser() != null)
        *   startActivity(new Intent(LoginActivity.this, DashBoard.class));
        * */

    }

    @Override
    public void onClick(View v) {
//        if(v.getId() == R.id.login_btn_forgot_password){
//
//        }
    }
}

