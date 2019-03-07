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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amazon.identity.auth.device.AuthError;
import com.amazon.identity.auth.device.api.Listener;
import com.amazon.identity.auth.device.api.authorization.AuthCancellation;
import com.amazon.identity.auth.device.api.authorization.AuthorizationManager;
import com.amazon.identity.auth.device.api.authorization.AuthorizeListener;
import com.amazon.identity.auth.device.api.authorization.AuthorizeRequest;
import com.amazon.identity.auth.device.api.authorization.AuthorizeResult;
import com.amazon.identity.auth.device.api.authorization.ProfileScope;
import com.amazon.identity.auth.device.api.authorization.Scope;
import com.amazon.identity.auth.device.api.authorization.User;
import com.amazon.identity.auth.device.api.workflow.RequestContext;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import secapstone.helper.R;



/**
 * A login screen that offers login via email/password.
 *
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    ConstraintLayout activity_login;
    private FirebaseAuth mAuth;

    private RequestContext requestContext;
    private static final String TAG = "LoginActivity";
    private String email;
    private String account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStatusBarToWhite();
        setContentView(R.layout.activity_login);
        initializeControls();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        activity_login = (ConstraintLayout)findViewById(R.id.activity_login);

        authorizeAmazon();
        setupLoginBtn();
        email = null;
        account = null;

    }

    private void setupLoginBtn(){
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

    private void authorizeAmazon(){
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
                startActivity(new Intent(LoginActivity.this, LoginActivity.class));
            }
        });
    }

    private void initializeControls(){
        View loginButton = findViewById(R.id.login_with_amazon);
        loginButton.setOnClickListener(this);
        TextView btnForgotPass = findViewById(R.id.forgot_password);
        btnForgotPass.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        requestContext.onResume();
    }

    private void updateUI(FirebaseUser user){
        if(user!=null) {
            /* The user is signed in */
            System.out.println("user is signed in");
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        else{
            mAuth.createUserWithEmailAndPassword(email, account)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }

    }

    private void fetchUserProfile() {
        User.fetch(this, new Listener<User, AuthError>() {
            /* fetch completed successfully. */
            @Override
            public void onSuccess(User user) {
//                final String name = user.getUserName();
                email = user.getUserEmail();
                account = user.getUserId();
                Log.i(TAG, "email: " + email + ", account: " + account);
            }
            /* There was an error during the attempt to get the profile. */
            @Override
            public void onError(AuthError ae) {
                /* Retry or inform the user of the error */
                Log.i(TAG, "user profile error: " + ae.toString());
            }
        });
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
                    fetchUserProfile();
                    if(email!=null && account!=null) {
                        mAuth.signInWithEmailAndPassword(email, account)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(TAG, "signInWithEmail:success");
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            updateUI(user);
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();
                                            updateUI(null);
                                        }
                                    }
                                });
                    }

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