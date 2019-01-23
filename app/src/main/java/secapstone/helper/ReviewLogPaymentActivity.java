package secapstone.helper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ReviewLogPaymentActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_log_payment);

        Button finish = (Button) findViewById(R.id.finishButton);
        finish.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onClickFinish();
            }
        });

        Button back = (Button) findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onClickBack();
            }
        });

        setName();
        setAmount();
        setDate();

    }

    public void setName()
    {
        TextView logPaymentTo = findViewById(R.id.logPaymentToText);
        logPaymentTo.setText(ViewArtisanActivity.artisanName);
    }

    public void setAmount()
    {
        TextView logAmount = findViewById(R.id.logAmountText);
        logAmount.setText(LogPaymentActivity.amountPaid);
    }

    public void setDate()
    {
        TextView logDate = findViewById(R.id.logDateText);
        logDate.setText(LogPaymentActivity.dateToPay);
    }

    public void onClickFinish()
    {
        startActivity(new Intent(ReviewLogPaymentActivity.this, MainActivity.class));
    }

    public void onClickBack()
    {
        startActivity(new Intent(ReviewLogPaymentActivity.this, LogPaymentActivity.class));
    }
}
