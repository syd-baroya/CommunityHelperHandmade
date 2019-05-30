package secapstone.helper.pages.view_artisan;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import secapstone.helper.R;
import secapstone.helper.pages.log_payment.AccountingSystem;

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
                    AccountingSystem.logPayment(artisanID, Float.parseFloat(amountString), context, dateString);
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
