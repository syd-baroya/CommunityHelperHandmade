package secapstone.helper.Pages.LogPayment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import secapstone.helper.R;
import secapstone.helper.Pages.ViewArtisan.ViewArtisanActivity;

public class LogPaymentActivity extends AppCompatActivity
{
    EditText amount;
    EditText date;

    static String amountPaid;
    static String dateToPay;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_payment);

        setName();

        amount = (EditText)findViewById(R.id.amountToPayText);
        date = (EditText)findViewById(R.id.DateTextBox);

        Button makePayment = (Button) findViewById(R.id.makePaymentButton);
        makePayment.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onClickMakePayment();
            }
        });
    }

    public void onClickMakePayment()
    {
        amountPaid = amount.getText().toString();
        dateToPay = date.getText().toString();
        startActivity(new Intent(LogPaymentActivity.this, ReviewLogPaymentActivity.class));
    }

    public void setName()
    {
        TextView artisanNameBox = findViewById(R.id.artisanNameText);
        artisanNameBox.setText(ViewArtisanActivity.artisanName);
    }
}
