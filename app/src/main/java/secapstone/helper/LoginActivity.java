package secapstone.helper;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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
        mAuth = FirebaseAuth.getInstance();

        //check already session, if->dashboard
        //create dashboard activity

        if(mAuth.getCurrentUser() != null)
            startActivity(new Intent(LoginActivity.this, DashBoard.class));


    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.login_btn_forgot_password){
            startActivity(new Intent(LoginActivity.this,ForgotPassword.class));
            finish();
        }
        else if(v.getId() == R.id.login_btn_login){
            loginUser(input_email.getText().toString(), input_password.getText().toString());
        }
    }

    private void loginUser(String email, final String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            if(password.length()<6){
                                Snackbar snackBar= Snackbar.make(activity_login, "Passowrd length must be over 6", Snackbar.LENGTH_SHORT);
                                snackBar.show();
                            }
                        }
                        else{
                            startActivity(new Intent(LoginActivity.this, DashBoard.class));
                        }
                    }
                });
    }
}

