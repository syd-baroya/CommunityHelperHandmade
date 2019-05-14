package secapstone.helper.pages.log_payment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import secapstone.helper.R;
import secapstone.helper.pages.view_artisan.ViewArtisanActivity;

public class LogPaymentDialog extends Dialog
{
    EditText amount;
    EditText date;

    String artisanID;
    String amountPaid;
    String dateToPay;

    public LogPaymentDialog(@NonNull Context context, String artisanID) {
        super(context);
        this.artisanID = artisanID;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modal_log_payment);

        setName();

        amount = (EditText)findViewById(R.id.amountTextField);
        date = (EditText)findViewById(R.id.dateTextField);

        Button makePayment = (Button) findViewById(R.id.logPaymentButton);
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
        System.out.println("clicked make payment");

        amountPaid = amount.getText().toString();
        dateToPay = date.getText().toString();
        AccountingSystem accountingSystem = new AccountingSystem();
        accountingSystem.logPayment(artisanID, Float.parseFloat(amountPaid) );
        //upload payment info and
    }

    public void setName()
    {
        TextView artisanNameBox = findViewById(R.id.artisanNameText);
        //artisanNameBox.setText(ViewArtisanActivity.artisanName);
    }
}
