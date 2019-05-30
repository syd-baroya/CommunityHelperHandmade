package secapstone.helper.pages.add_artisan;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import com.kyanogen.signatureview.SignatureView;
import android.view.WindowManager;

import secapstone.helper.R;


public class SignatureAddArtisanActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signature_add_artisan);

        SignatureView sigView = (SignatureView) findViewById(R.id.signature_view);
        sigView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Button nextButton = findViewById(R.id.nextButton);
                nextButton.setEnabled(true);
                return false;
            }
        });
    }

    public void onClickNext(View view)
    {
        startActivity(new Intent(SignatureAddArtisanActivity.this, FinalPreviewAddArtisanActivity.class));
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    public void onClickBack(View view)
    {
        SignatureView sigView = (SignatureView) findViewById(R.id.signature_view);
        if (!sigView.isBitmapEmpty()) {
            startActivity(new Intent(SignatureAddArtisanActivity.this, DescriptionAddArtisanActivity.class));
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        }
    }


}
