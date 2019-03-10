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
import com.amazon.identity.auth.device.api.workflow.RequestContext;
import com.amazon.identity.auth.device.api.authorization.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import java.io.Serializable;

import secapstone.helper.R;
import secapstone.helper.addartisan.FinalPreviewAddArtisanActivity;


/**
 * A login screen that offers login via email/password.
 *
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    ConstraintLayout activity_login;
    private FirebaseAuth mAuth;

    private RequestContext requestContext;
    private static final String TAG = "LoginActivity";
    private secapstone.helper.User CGA;
    private String email;
    private String password;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        initializeControls();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        activity_login = findViewById(R.id.activity_login);

        authorizeAmazon();
        setupLoginBtn();
        CGA = null;
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
                Log.d(TAG,"app is now authorized");
            }

            /* There was an error during the attempt to authorize the
            application. */
            @Override
            public void onError(AuthError ae) {
                /* Inform the user of the error */
                Log.d(TAG,"cannot authorize application");
            }

            /* Authorization was cancelled before it could be completed. */
            @Override
            public void onCancel(AuthCancellation cancellation) {
                /* Reset the UI to a ready-to-login state */
                Log.d(TAG,"reset ui to ready-to-login");
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
            Log.d(TAG,"user is signed in");
            startActivity(new Intent(getBaseContext(), MainActivity.class));
            finish();
        }
        else {
            if (email != null && password != null){
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    CGA = secapstone.helper.User.getUser();
                                    CGA.setEmail(email);
                                    CGA.setIdToken(user.getUid());
                                    CGA.setName(name);
                                    CGA.setPassword(password);
                                    updateUI(user);
                                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                                    finish();
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

    }

    private void fetchUserProfile() {
        User.fetch(this, new Listener<User, AuthError>() {
            /* fetch completed successfully. */
            @Override
            public void onSuccess(User user) {
                name = user.getUserName();
                email = user.getUserEmail();
                password = user.getUserId();
                Log.i(TAG, "email: " + email + ", password: " + password);
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
                    if(email!=null && password!=null) {
                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(TAG, "signInWithEmail:success");
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            CGA = secapstone.helper.User.getUser();
                                            CGA.setEmail(email);
                                            CGA.setIdToken(user.getUid());
                                            CGA.setName(name);
                                            CGA.setPassword(password);
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
                    Log.d(TAG,"user is not signed in");


                }
            }

            @Override
            public void onError(AuthError ae) {
                /* The user is not signed in */
                Log.d(TAG,"user is not signed in");

            }
        });
    }


    @Override
    public void onClick(View v) {

        AuthorizationManager.signOut(getApplicationContext(), new Listener < Void, AuthError > () {
            @Override
            public void onSuccess(Void response) {
                // Set logged out state in UI
                Log.d(TAG,"set logged out state in ui");

            }
            @Override
            public void onError(AuthError authError) {
                // Log the error
                Log.d(TAG,"log error");

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