package secapstone.helper;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.*;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

import com.google.firebase.auth.FirebaseUser;
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
        /*
        * if(auth.getCurrentUser() != null)
        *   startActivity(new Intent(LoginActivity.this, DashBoard.class));
        * */

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.login_btn_forgot_password){

        }
    }
}

