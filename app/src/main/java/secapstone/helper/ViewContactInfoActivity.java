package secapstone.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ViewContactInfoActivity extends AppCompatActivity {

    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    private static final int REQUEST_CALL = 1;
    public static String artisanName;
    private String callNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact_info);

        Intent intent = getIntent();
        String name = intent.getStringExtra(ViewArtisanActivity.EXTRA_A_NAME);
        String phone = intent.getStringExtra(ViewArtisanActivity.EXTRA_A_PHONE);
        callNum = "tel:" + phone;
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
            public void onClick(View view) {
                onClickDirectionsButton();
            }
        });

        Button callNowButton = (Button) findViewById(R.id.callNowButton);
        callNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        if (ContextCompat.checkSelfPermission(ViewContactInfoActivity.this, Manifest.permission.CALL_PHONE) !=
        PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ViewContactInfoActivity.this, new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        }
        else {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(callNum)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onClickCallButton();
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
