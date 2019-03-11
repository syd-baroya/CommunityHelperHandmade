package secapstone.helper;

import android.Manifest;
import android.annotation.TargetApi;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ViewArtisanActivity extends AppCompatActivity {
    //Used when requesting permissions for Call and Text
    private static final int REQUEST_CALL = 1;
    private static final int REQUEST_SMS = 2;

    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    public static String artisanName;
    public static String artisanAddress;
    public static String artisanPhone;

    //reference to a certain artisan in database
    private static DocumentReference artisanRef;
    //reference to the current artisan's products
    private static CollectionReference productsRef;

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

        productsRef = artisanRef.collection("Products");

        setStatusBarToDark();

    }

    public static void setArtisanRef( DocumentReference ref){
        artisanRef = ref;
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

    private void setImage(String url, String name, String description, String phone, String address) {
        TextView nameTitle = findViewById(R.id.artisan_name);
        nameTitle.setText(name);

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

        myDialog.findViewById(R.id.textNowButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickTextButton();
            }
        });

        myDialog.findViewById(R.id.getDirectionsButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickMapButton();
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public void onClickCloseContactInfo(View view)
    {
        myDialog.dismiss();
    }

    public void onClickCallButton() {
        if (ContextCompat.checkSelfPermission(ViewArtisanActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ViewArtisanActivity.this, new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        }
        else {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"+artisanPhone));
            startActivity(intent);
        }
    }

    public void onClickTextButton() {
        //Using built-in Android messaging app
        if (ContextCompat.checkSelfPermission(ViewArtisanActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ViewArtisanActivity.this, new String[] {Manifest.permission.SEND_SMS}, REQUEST_SMS);
        }
        else {
            Intent textIntent = new Intent(Intent.ACTION_VIEW);
            textIntent.setData(Uri.parse("sms:"));
            //textIntent.setType("vnd.android-dir/mms-sms");
            textIntent.putExtra("address", artisanPhone);
            startActivity(textIntent);
        }
        //Using Whatsapp
        /*
        Uri uri = Uri.parse("smsto:" + artisanPhone);
        Intent whatsappIntent = new Intent(Intent.ACTION_SENDTO, uri);
        whatsappIntent.setPackage("com.whatsapp");
        startActivity(whatsappIntent);
         */
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
        if (requestCode == REQUEST_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onClickTextButton();
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onClickMapButton() {
        Uri uri = Uri.parse("geo:0,0?q=" + artisanAddress);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    @TargetApi(23)
    public void setStatusBarToDark() {
        View view = findViewById(R.id.view_artisan_container);
        int flags = this.getWindow().getDecorView().getSystemUiVisibility();
        flags = flags ^ View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        flags = flags ^ View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        view.setSystemUiVisibility(flags);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }
}
