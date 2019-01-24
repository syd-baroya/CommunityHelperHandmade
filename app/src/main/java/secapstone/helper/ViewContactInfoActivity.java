package secapstone.helper;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ViewContactInfoActivity extends AppCompatActivity {

    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    public static String artisanName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact_info);

        Intent intent = getIntent();
        String name = intent.getStringExtra(ViewArtisanActivity.EXTRA_A_NAME);
        String phone = intent.getStringExtra(ViewArtisanActivity.EXTRA_A_PHONE);
        String address = intent.getStringExtra(ViewArtisanActivity.EXTRA_A_ADDRESS);

        TextView nameTitle = findViewById(R.id.artisan_name2);
        nameTitle.setText(name);

        TextView phoneTitle = findViewById(R.id.artisan_phone2);
        phoneTitle.setText(phone);

        //TextView addressTitle = findViewById(R.id.artisan_address);
        //addressTitle.setText(address);

        Button getDirectionsButton = (Button) findViewById(R.id.getDirectionsButton);
        getDirectionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                onClickDirectionsButton();
            }
        });

        Button callNowButton = (Button) findViewById(R.id.callNowButton);
        callNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                onClickCallButton();
            }
        });
    }

    public void onClickDirectionsButton()
    {
        //startActivity(new Intent(ViewArtisanActivity.this, ViewReportsActivity.class));
    }

    public void onClickCallButton()
    {
        //startActivity(new Intent(ViewArtisanActivity.this, LogPaymentActivity.class));
    }
}
