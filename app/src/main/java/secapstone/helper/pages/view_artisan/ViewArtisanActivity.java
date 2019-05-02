package secapstone.helper.pages.view_artisan;

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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.Date;

import secapstone.helper.model.ActionItem;
import secapstone.helper.model.Listing;
import secapstone.helper.model.User;
import secapstone.helper.pages.action_items.ActionItemAdapter;
import secapstone.helper.pages.log_payment.AccountingSystem;
import secapstone.helper.pages.log_payment.LogPaymentActivity;
import secapstone.helper.pages.MainActivity;
import secapstone.helper.R;

public class ViewArtisanActivity extends AppCompatActivity {
    //Used when requesting permissions for Call and Text
    private static final int REQUEST_CALL = 1;
    private static final int REQUEST_SMS = 2;

    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = db.collection("users");

    public String artisanName;
    public String artisanAddress;
    public String artisanPhone;
    public String artisanID;
    public String artisanDescription;
    public String artisanPicURL;

    private ListingAdapter adapter;
    private RecyclerView recyclerView;

    //reference to a certain artisan in database
    private DocumentReference artisanRef;

    Dialog contactInfoModal;
    Dialog logPaymentDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_artisan);

        recyclerView = findViewById(R.id.recyclerView);

        getIncomingIntent();

        contactInfoModal = new Dialog(this);
        logPaymentDialog = new Dialog(this);

        setUpContactInfoModal();
        setUpLogPaymentModal();

        artisanRef = FirebaseFirestore.getInstance().collection("users").document(User.getUser().getID()).collection("artisans").document(artisanID);

        setStatusBarToDark();

        runListingsQuery();

    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("name") && getIntent().hasExtra("url") && getIntent().hasExtra("description")&& getIntent().hasExtra("id")) {

            artisanPhone = getIntent().getStringExtra("phone");
            artisanAddress = getIntent().getStringExtra("address");
            artisanName = getIntent().getStringExtra("name");
            artisanID = getIntent().getStringExtra("id");
            artisanDescription = getIntent().getStringExtra("description");
            artisanPicURL = getIntent().getStringExtra("url");

            setImage(artisanPicURL, artisanName, artisanDescription, artisanPhone, artisanAddress);
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
            image.setImageResource(R.drawable.icon_empty_person);
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

    public void onClickNewListingButton(View view)
    {
        Intent intent = new Intent(ViewArtisanActivity.this, NewListingActivity.class);
        intent.putExtra("id", artisanID);
        startActivity(intent);
    }

    public void setUpContactInfoModal() {
        contactInfoModal.setContentView(R.layout.modal_contact_info);

        if (artisanPhone != null) {
            TextView phoneNumText = contactInfoModal.findViewById(R.id.logPaymentTitle);
            phoneNumText.setText(artisanPhone);
        }

        if (artisanAddress != null) {
            TextView addressText = contactInfoModal.findViewById(R.id.artisan_address);
            addressText.setText(artisanAddress);
        }

        contactInfoModal.findViewById(R.id.callNowButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickCallButton();
            }
        });

        contactInfoModal.findViewById(R.id.textNowButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickTextButton();
            }
        });

        contactInfoModal.findViewById(R.id.getDirectionsButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickMapButton();
            }
        });

        contactInfoModal.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void onClickContactInfoButton(View view)
    {
        contactInfoModal.show();
    }


    public void setUpLogPaymentModal() {
        logPaymentDialog.setContentView(R.layout.modal_log_payment);

        TextView title = logPaymentDialog.findViewById(R.id.logPaymentTitle);
        title.setText("Log Payment to " + artisanName);
        logPaymentDialog.findViewById(R.id.logPaymentButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //double amount = Double.parseDouble((String)((TextView)logPaymentDialog.findViewById((R.id.amountTextField))).getText());
                //Date userEnteredDate = new Date((String)((TextView)logPaymentDialog.findViewById((R.id.amountTextField))).getText());
                double amount = 10.11;
                Date userEnteredDate = new Date(1999, 12, 31);
                logPayment(amount, userEnteredDate);

                logPaymentDialog.hide();
            }
        });

        logPaymentDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void onClickLogPaymentButton(View view)
    {
        logPaymentDialog.show();
    }

    public void logPayment(double amount, Date userEnteredDate)
    {
        AccountingSystem.logPayment(artisanRef,amount, userEnteredDate, Calendar.getInstance().getTime());
    }

    public void onClickCloseModal(View view)
    {
        contactInfoModal.dismiss();
        logPaymentDialog.dismiss();
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

    private void runListingsQuery() {
        Query query = artisanRef.collection("products").orderBy("title", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Listing> options = new FirestoreRecyclerOptions.Builder<Listing>()
                .setQuery(query, Listing.class)
                .build();

        adapter = new ListingAdapter(options, ViewArtisanActivity.this);

        recyclerView.setLayoutManager(new LinearLayoutManager(ViewArtisanActivity.this));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.startListening();
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
