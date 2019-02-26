package secapstone.helper;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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

public class ViewArtisanActivity extends AppCompatActivity {
    private static final int REQUEST_CALL = 1;

    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    public static String artisanName;
    public static String artisanAddress;
    public static String artisanPhone;
    public static String EXTRA_A_ADDRESS = "com.example.application.example.artisanAddress";
    public static String EXTRA_A_PHONE = "com.example.application.example.artisanPhone";

    Dialog myDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_artisan);

        getIncomingIntent();

        Button logPaymentButton = findViewById(R.id.logPaymentButton);
        logPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                onClickLogPayments(view);
            }
        });

        myDialog = new Dialog(this);

    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("name") && getIntent().hasExtra("url") && getIntent().hasExtra("description")) {
            String name = getIntent().getStringExtra("name");
            artisanName = name;
            String url = getIntent().getStringExtra("url");
            String description = getIntent().getStringExtra("description");
            String phone = getIntent().getStringExtra("phone");
            artisanPhone = phone;
            String address = getIntent().getStringExtra("address");
            artisanAddress = address;

            setImage(url, name, description, phone, address);
        }
    }

    private void setImage(String url, String name, String description, String phone, String
            address) {
        TextView nameTitle = findViewById(R.id.artisan_name);
        nameTitle.setText(name);

//        TextView descriptionTitle = findViewById(R.id.artisan_description);
//        descriptionTitle.setText(description);
//
//        TextView phoneTitle = findViewById(R.id.artisan_phone);
//        phoneTitle.setText(phone);
//
//        TextView addressTitle = findViewById(R.id.artisan_address);
//        addressTitle.setText(address);

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

    public void onClickReportsButton(View view)
    {
        startActivity(new Intent(ViewArtisanActivity.this, ViewReportsActivity.class));
    }

    public void onClickLogPayments(View view)
    {
        startActivity(new Intent(ViewArtisanActivity.this, LogPaymentActivity.class));
    }

    public void onClickBackButton(View view)
    {
        startActivity(new Intent(ViewArtisanActivity.this, MainActivity.class));
    }

    public void onClickContactInfoButton(View view)
    {
        myDialog.setContentView(R.layout.view_contact_info_modal);

        if (artisanPhone != null) {
            TextView phoneNumText = myDialog.findViewById(R.id.artisan_phone2);
            phoneNumText.setText(artisanPhone);
        }

        if (artisanAddress != null) {
            TextView addressText = myDialog.findViewById(R.id.artisan_address);
            addressText.setText(artisanAddress);
        }

        myDialog.findViewById(R.id.callNowButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickCallButton();
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public void onClickCloseContactInfo(View view)
    {
        myDialog.dismiss();
    }

    public void onClickDirectionsButton()
    {
        //startActivity(new Intent(ViewArtisanActivity.this, ViewReportsActivity.class));
    }

    public void onClickCallButton() {
        if (ContextCompat.checkSelfPermission(myDialog.getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(myDialog.getOwnerActivity(), new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        }
        else {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"+artisanPhone));
            startActivity(intent);
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
