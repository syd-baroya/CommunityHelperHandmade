package secapstone.helper.pages.make_purchase;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import secapstone.helper.R;

public class PurchaseDialog extends Dialog {

    public PurchaseDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modal_purchase);


    }
}
