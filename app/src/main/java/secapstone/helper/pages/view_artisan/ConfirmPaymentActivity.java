package secapstone.helper.pages.view_artisan;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kyanogen.signatureview.SignatureView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import secapstone.helper.R;
import secapstone.helper.model.Listing;
import secapstone.helper.model.User;
import secapstone.helper.pages.custom_ui.CustomTextField;
import secapstone.helper.pages.log_payment.AccountingSystem;
import secapstone.helper.pages.login.LoginActivity;

public class ConfirmPaymentActivity extends AppCompatActivity {

    private static final String TAG = "ConfirmPaymentActivity";

    ConstraintLayout loadingSpinner;
    TextView name;
    TextView date;
    TextView amount;

    String nameString;
    String dateString;
    String amountString;

    Button confirmButton;
    Button cancelButton;

    String artisanID;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_review_log_payment);
        context = this;

        name = findViewById(R.id.artisanNameText);
        date = findViewById(R.id.dateText);
        amount = findViewById(R.id.paymentAmountText);
        confirmButton = findViewById(R.id.confirmButton);
        cancelButton = findViewById(R.id.declineButton);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((amountString != null) && (dateString != null) && (nameString != null)) {
                    AccountingSystem.logPayment(artisanID, Float.parseFloat(amountString), context);
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        SignatureView sigView = (SignatureView) findViewById(R.id.signature_view);
        sigView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Button confirmButton = findViewById(R.id.confirmButton);
                confirmButton.setEnabled(true);
                return false;
            }
        });

        getIncomingIntent();

    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("name")) {
            name.setText(getIntent().getStringExtra("name"));
            nameString = getIntent().getStringExtra("name");
        }

        if (getIntent().hasExtra("date")) {
            date.setText(getIntent().getStringExtra("date"));
            dateString = getIntent().getStringExtra("date");
        }

        if (getIntent().hasExtra("amount")) {
            amount.setText(getIntent().getStringExtra("amount"));
            amountString = getIntent().getStringExtra("amount");
        }

        if (getIntent().hasExtra("artisanID")) {
            artisanID = getIntent().getStringExtra("artisanID");
        }
    }
}
