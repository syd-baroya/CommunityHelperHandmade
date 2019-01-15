package secapstone.helper;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.saga.communityhelperhandmade.R;

public class ViewArtisanActivity extends AppCompatActivity {

    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_artisan);

        getIncomingIntent();

        Button reportsButton = (Button) findViewById(R.id.reportsButton);
        reportsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                onClickReportsButton();
            }
        });
    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("name") && getIntent().hasExtra("url") && getIntent().hasExtra("description")) {
            String name = getIntent().getStringExtra("name");
            String url = getIntent().getStringExtra("url");
            String description = getIntent().getStringExtra("description");
            String phone = getIntent().getStringExtra("phone");
            String address = getIntent().getStringExtra("address");

            setImage(url, name, description, phone, address);
        }
    }

    private void setImage(String url, String name, String description, String phone, String
            address) {
        TextView nameTitle = findViewById(R.id.artisan_name);
        nameTitle.setText(name);

        TextView descriptionTitle = findViewById(R.id.artisan_description);
        descriptionTitle.setText(description);

        TextView phoneTitle = findViewById(R.id.artisan_phone);
        phoneTitle.setText(phone);

        TextView addressTitle = findViewById(R.id.artisan_address);
        addressTitle.setText(address);

        final ImageView image = findViewById(R.id.artisan_banner_image);

        final Activity thisAct = this;

        if (url != null) {
            storageRef.child(url).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(thisAct)
                            .asBitmap()
                            .load(uri)
                            .into(image);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        } else {
            image.setImageResource(R.drawable.ic_empty_person);
        }
    }

    public void onClickReportsButton()
    {
        startActivity(new Intent(ViewArtisanActivity.this, ViewReportsActivity.class));
    }



}
