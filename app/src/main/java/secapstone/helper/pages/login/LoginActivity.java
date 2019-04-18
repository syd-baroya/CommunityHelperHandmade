package secapstone.helper.pages.login;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

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

import secapstone.helper.pages.MainActivity;
import secapstone.helper.R;


public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private RequestContext amazonAuthListener;
    private secapstone.helper.model.User CGA;
    private String email;
    private String password;
    private String name;

    private Button loginButton;

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setStatusBarToWhite();

        initializeControls();
        setupAmazonAuthListener();
    }

    private void onClickLoginButton(){
        //Calls amazonAuthListener's listeners (look at authorizeAmazon())
        Log.d(TAG,"Click Login");
        AuthorizationManager.authorize(new AuthorizeRequest
            .Builder(amazonAuthListener)
            .addScopes(ProfileScope.profile(), ProfileScope.postalCode())
            .build());
    }

    private void setupAmazonAuthListener(){
        amazonAuthListener = RequestContext.create((Context) this);

        amazonAuthListener.registerListener(new AuthorizeListener() {
            @Override
            public void onSuccess(AuthorizeResult result) {
                /* Your app is now authorized for the requested scopes */
                Log.d(TAG,"app is now authorized by Amazon");
                fetchAmazonUserProfile();
            }

            @Override
            public void onError(AuthError ae) {
                /* Inform the user of the error */
                Log.d(TAG,"Amazon cannot authorize application");
            }

            @Override
            public void onCancel(AuthCancellation cancellation) {
                /* Reset the UI to a ready-to-login state */
                Log.d(TAG,"Amazon auth canceled before it could complete");
                startActivity(new Intent(LoginActivity.this, LoginActivity.class));
            }
        });
    }

    private void goToMainActivity(FirebaseUser user){
        if(user!=null) {
            startActivity(new Intent(getBaseContext(), MainActivity.class));
            finish();
        }
        else {
            Log.d(TAG,"Tried to proceed but firebase user is still null");
        }
    }

    private void fetchAmazonUserProfile() {
        User.fetch(this, new Listener<User, AuthError>() {
            /* fetch completed successfully. */
            @Override
            public void onSuccess(User user) {
                name = user.getUserName();
                email = user.getUserEmail();
                password = user.getUserId();
                Log.i(TAG, "email: " + email + ", password: " + password);

                signInFirebaseUser();
            }
            @Override
            public void onError(AuthError ae) {
                /* Retry or inform the user of the error */
                Log.i(TAG, "Couldn't get Amazon account details: " + ae.toString());
            }
        });
    }

    public void signInFirebaseUser() {
        if (email!=null && password!=null) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "Successfully signed into firebase");
                                FirebaseUser user = mAuth.getCurrentUser();
                                CGA = secapstone.helper.model.User.getUser();
                                CGA.setEmail(email);
                                CGA.setID(user.getUid());
                                CGA.setName(name);
                                goToMainActivity(user);
                            } else {
                                Log.d(TAG, "Failed to sign user into firebase, attempting to create new user", task.getException());
                                createNewFirebaseUser();
                            }
                        }
                    });
        } else {
            Log.d(TAG, "email or pass is null");
        }
    }

    public void createNewFirebaseUser() {
        if (email != null && password != null){
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "Created firebase user successfully");
                            FirebaseUser user = mAuth.getCurrentUser();
                            CGA = secapstone.helper.model.User.getUser();
                            CGA.setEmail(email);
                            CGA.setID(user.getUid());
                            CGA.setName(name);
                            goToMainActivity(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "Failed to create firebase user", task.getException());
                        }
                    }
                });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        amazonAuthListener.onResume();
    }

    private void initializeControls(){
        //Grab any needed views
        loginButton = findViewById(R.id.login_with_amazon);

        //Set OnClick Listeners
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLoginButton();
            }
        });
    }

    public static boolean isValidPassword(String s) {
        if (s.length() > 0) {
            return true;
        }
        return false;
    }

    @TargetApi(23)
    public void setStatusBarToWhite() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);

        int flags = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        getWindow().getDecorView().setSystemUiVisibility(flags);
        getWindow().setStatusBarColor(Color.WHITE);
    }

}