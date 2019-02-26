package secapstone.helper;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amazon.identity.auth.device.AuthError;
import com.amazon.identity.auth.device.api.Listener;
import com.amazon.identity.auth.device.api.authorization.AuthCancellation;
import com.amazon.identity.auth.device.api.authorization.AuthorizationManager;
import com.amazon.identity.auth.device.api.authorization.AuthorizeListener;
import com.amazon.identity.auth.device.api.authorization.AuthorizeRequest;
import com.amazon.identity.auth.device.api.authorization.AuthorizeResult;
import com.amazon.identity.auth.device.api.authorization.ProfileScope;
import com.amazon.identity.auth.device.api.authorization.Scope;
import com.amazon.identity.auth.device.api.workflow.RequestContext;
import secapstone.helper.R;

/**
 * A login screen that offers login via email/password.
 *
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    TextView btnForgotPass;
    ConstraintLayout activity_login;


    private RequestContext requestContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStatusBarToWhite();

        setContentView(R.layout.activity_login);

        btnForgotPass = (TextView) findViewById(R.id.forgot_password);
        activity_login = (ConstraintLayout)findViewById(R.id.activity_login);

        requestContext = RequestContext.create(this);

        requestContext.registerListener(new AuthorizeListener() {

            /* Authorization was completed successfully. */
            @Override
            public void onSuccess(AuthorizeResult result) {
                /* Your app is now authorized for the requested scopes */
                System.out.println("app is now authorized");

            }

            /* There was an error during the attempt to authorize the
            application. */
            @Override
            public void onError(AuthError ae) {
                /* Inform the user of the error */
                System.out.println("cannot authorize application");
            }

            /* Authorization was cancelled before it could be completed. */
            @Override
            public void onCancel(AuthCancellation cancellation) {
                /* Reset the UI to a ready-to-login state */
                System.out.println("reset ui to ready-to-login");

            }
        });

        View loginButton = findViewById(R.id.login_with_amazon);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthorizationManager.authorize(new AuthorizeRequest
                        .Builder(requestContext)
                        .addScopes(ProfileScope.profile(), ProfileScope.postalCode())
                        .build());
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        requestContext.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Scope[] scopes = {
                ProfileScope.profile(),
                ProfileScope.postalCode()
        };
        AuthorizationManager.getToken(this, scopes, new Listener< AuthorizeResult, AuthError >() {

            @Override
            public void onSuccess(AuthorizeResult result) {
                if (result.getAccessToken() != null) {
                    /* The user is signed in */
                    System.out.println("user is signed in");
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();

                } else {
                    /* The user is not signed in */
                    System.out.println("user is not signed in");

                }
            }

            @Override
            public void onError(AuthError ae) {
                /* The user is not signed in */
                System.out.println("user is not signed in");

            }
        });
    }


    @Override
    public void onClick(View v) {

        AuthorizationManager.signOut(getApplicationContext(), new Listener < Void, AuthError > () {
            @Override
            public void onSuccess(Void response) {
                // Set logged out state in UI
                System.out.println("set logged out state in ui");

            }
            @Override
            public void onError(AuthError authError) {
                // Log the error
                System.out.println("log error");

            }
        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @TargetApi(23)
    public void setStatusBarToWhite() {
        getWindow().setStatusBarColor(Color.WHITE);
    }

}

//package secapstone.helper;
//
//        import android.app.AlertDialog;
//        import android.app.ProgressDialog;
//        import android.content.Intent;
//        import android.support.annotation.NonNull;
//        import android.support.constraint.ConstraintLayout;
//        import android.support.design.widget.Snackbar;
//        import android.support.v7.app.AppCompatActivity;
//
//        import android.os.Bundle;
//        import android.view.View;
//        import android.widget.Button;
//        import android.widget.EditText;
//        import android.widget.RelativeLayout;
//        import android.widget.TextView;
//
//        import com.google.android.gms.tasks.OnCompleteListener;
//        import com.google.android.gms.tasks.Task;
//        import com.google.firebase.auth.AuthResult;
//        import com.google.firebase.auth.FirebaseAuth;
//
//        import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
//        import com.google.firebase.auth.FirebaseAuthInvalidUserException;
//        import com.google.firebase.auth.ProviderQueryResult;
//        import com.saga.communityhelperhandmade.R;
//
///**
// * A login screen that offers login via email/password.
// *
// */
//public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
//
//    Button btnLogin;
//    EditText input_email, input_password;
//    TextView btnForgotPass;
//
//    ConstraintLayout activity_login;
//
//    private FirebaseAuth mAuth;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        //view
//        btnLogin = (Button)findViewById(R.id.login_btn_login);
//        input_email = (EditText)findViewById(R.id.login_email);
//        input_password = (EditText)findViewById(R.id.login_password);
//        btnForgotPass = (TextView)findViewById(R.id.login_btn_forgot_password);
//        activity_login = (ConstraintLayout)findViewById(R.id.activity_login);
//
//        btnForgotPass.setOnClickListener(this);
//        btnLogin.setOnClickListener(this);
//
//        //init firebase auth
//        mAuth = FirebaseAuth.getInstance();
//
//        //check already session, if->dashboard
//        //create dashboard activity
////
////        if(mAuth.getCurrentUser() != null)
////            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//
//
//    }
//
//    @Override
//    public void onClick(View v) {
//        if(v.getId() == R.id.login_btn_forgot_password){
//            startActivity(new Intent(LoginActivity.this,ForgotPassword.class));
//            finish();
//        }
//        else if(v.getId() == R.id.login_btn_login){
//            loginUser(input_email.getText().toString(), input_password.getText().toString());
//        }
//    }
//
//
//    private void loginUser(String email, final String password){
//
//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(!task.isSuccessful()){
//                            if(isValidPasswordLength(password)){
//                                Snackbar snackBar= Snackbar.make(activity_login, "Password length must be over 6", Snackbar.LENGTH_SHORT);
//                                snackBar.show();
//                            }
//
//                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
//                                /**
//                                 * Thrown when one or more of the credentials passed to a method fail to
//                                 * identify and/or authenticate the user subject of that operation.
//                                 * Inspect the error code and message to find out the specific cause.
//                                 * https://firebase.google.com/docs/reference/android/com/google/firebase/auth/FirebaseAuthInvalidCredentialsException
//                                 */
//
//                                Snackbar snackBar= Snackbar.make(activity_login, " Invalid credentials. Check email and password again.", Snackbar.LENGTH_SHORT);
//                                snackBar.show();
//                            } else if (task.getException() instanceof FirebaseAuthInvalidUserException) {
//                                /**
//                                 * Inspect the error code and message to find out the specific cause.
//                                 * https://firebase.google.com/docs/reference/android/com/google/firebase/auth/FirebaseAuthInvalidUserException
//                                 */
//                                Snackbar snackBar= Snackbar.make(activity_login, " Invalid credentials. Check email and password again.", Snackbar.LENGTH_SHORT);
//                                snackBar.show();
//                            }
//
//                        }
//                        else{
//                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                        }
//                    }
//                });
//    }
//
//    public boolean isValidPasswordLength(String password){
//        if(password.length()<6)
//            return false;
//        return true;
//    }
//}



