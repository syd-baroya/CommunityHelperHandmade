package secapstone.helper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
/**
 * A login screen that offers login via email/password.
 *
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnLogin;
    EditText input_email, input_password;
    TextView btnForgotPass;

    ConstraintLayout activity_login;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //view
        btnLogin = findViewById(R.id.login_btn);
        input_email = findViewById(R.id.login_email);
        input_password = findViewById(R.id.login_password);
        btnForgotPass = findViewById(R.id.forgot_password);
        activity_login = findViewById(R.id.activity_login);

        btnForgotPass.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        //init firebase auth
        mAuth = FirebaseAuth.getInstance();

        //check already session, if->dashboard, then create dashboard activity
        if(mAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.forgot_password){
            startActivity(new Intent(LoginActivity.this,ForgotPassword.class));
        }
        else if(v.getId() == R.id.login_btn){
            loginUser(input_email.getText().toString(), input_password.getText().toString());
        }
    }


    private void loginUser(String email, final String password){

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            if(isValidPasswordLength(password)){
                                Snackbar snackBar= Snackbar.make(activity_login, "Password length must be over 6", Snackbar.LENGTH_SHORT);
                                snackBar.show();
                            }

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                /**
                                 * Thrown when one or more of the credentials passed to a method fail to
                                 * identify and/or authenticate the user subject of that operation.
                                 * Inspect the error code and message to find out the specific cause.
                                 * https://firebase.google.com/docs/reference/android/com/google/firebase/auth/FirebaseAuthInvalidCredentialsException
                                 */

                                Snackbar snackBar= Snackbar.make(activity_login, " Invalid credentials. Check email and password again.", Snackbar.LENGTH_SHORT);
                                snackBar.show();
                            } else if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                                /**
                                 * Inspect the error code and message to find out the specific cause.
                                 * https://firebase.google.com/docs/reference/android/com/google/firebase/auth/FirebaseAuthInvalidUserException
                                 */
                                Snackbar snackBar= Snackbar.make(activity_login, " Invalid credentials. Check email and password again.", Snackbar.LENGTH_SHORT);
                                snackBar.show();
                            }

                        }
                        else{
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                });
    }

    public boolean isValidPasswordLength(String password){
        if(password.length()<6)
            return false;
        return true;
    }
}



