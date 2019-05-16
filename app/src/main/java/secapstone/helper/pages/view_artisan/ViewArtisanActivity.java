package secapstone.helper.pages.view_artisan;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import secapstone.helper.model.Listing;
import secapstone.helper.model.User;
import secapstone.helper.pages.log_payment.AccountingSystem;
import secapstone.helper.pages.MainActivity;
import secapstone.helper.R;

public class ViewArtisanActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener{
    //Used when requesting permissions for Call and Text
    private static final int REQUEST_CALL = 1;
    private static final int REQUEST_SMS = 2;

    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    private static final String TAG = "ViewArtisanActivity";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public String artisanName;
    public String artisanAddress;
    public String artisanPhone;
    public String artisanID;
    public String artisanDescription;
    public String artisanPicURL;
    public float artisanMoneyOwed;
    public float newPurchase = 0.0f;
    public Listing clickedListing = null;
    public EditText amount;
    public EditText date;


    private AccountingSystem accountingSystem;


    private Context context;

    private ListingAdapter adapter;
    private RecyclerView recyclerView;

    //reference to a certain artisan in database
    private DocumentReference artisanRef;
    private float itemClickedPrice=0.0f;

    Dialog contactInfoModal;
    Dialog logPaymentDialog;
    Dialog logShipmentDialog;
    Dialog purchaseDialog;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_artisan);

        context = this;

        recyclerView = findViewById(R.id.recyclerView);

        getIncomingIntent();

        accountingSystem = new AccountingSystem();

        contactInfoModal = new Dialog(this);
        logPaymentDialog = new Dialog(this);
        logShipmentDialog = new Dialog(this);
        purchaseDialog = new Dialog(this);

        setUpContactInfoModal();
        setUpLogPaymentModal();
        setUpLogShipmentModal();
        setUpPurchaseModal();

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
            artisanMoneyOwed = getIntent().getFloatExtra("moneyOwed", 0.0f);
            setImage(this, artisanPicURL, artisanName, artisanMoneyOwed);
        }
    }

    private void setImage(Activity view, String url, String name, float moneyOwed) {
        TextView nameTitle = view.findViewById(R.id.name);
        nameTitle.setText(name);
        TextView moneyOwedText = view.findViewById(R.id.moneyOwed);
        String moneyOwedString = "$" + String.format("%,.2f",moneyOwed);
        moneyOwedText.setText(moneyOwedString);

        final ImageView image = view.findViewById(R.id.banner_image);

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
    private void setImage(Dialog view, String url, String name, String description) {
        TextView nameTitle = view.findViewById(R.id.name);
        nameTitle.setText(name);

        TextView descTitle = view.findViewById(R.id.description);
        descTitle.setText(description);

        TextView pricePer = purchaseDialog.findViewById(R.id.pricePer);
        String pricePerString = "$" + String.format("%,.2f",itemClickedPrice) + " x 0";
        pricePer.setText(pricePerString);

        TextView totalPrice = purchaseDialog.findViewById(R.id.totalPrice);
        String totalPriceString = "$0.00";
        totalPrice.setText(totalPriceString);

        final ImageView image = view.findViewById(R.id.banner_image);

        final Activity thisAct = this;

        if (url != null) {
            storageRef.child(url).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context)
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
            System.out.println("in ViewArtisanActivity, model has null url");
            image.setImageResource(R.drawable.icon_empty_person);
        }


    }

    public void onClickReportsButton(View view)
    {
        startActivity(new Intent(ViewArtisanActivity.this, ViewReportsActivity.class));
    }

    public void onClickLogPayments(View view)
    {
        logPaymentDialog.show();
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

    public void addToArtisanBalance(float recentPurchase)
    {
        Map<String, Object> moneyUpdates = new HashMap<>();
        float newMoneyOwed = recentPurchase + artisanMoneyOwed;
        moneyUpdates.put("moneyOwedFromCommunityLeader", newMoneyOwed);

        artisanRef.update(moneyUpdates);
        TextView moneyOwedText = findViewById(R.id.moneyOwed);
        String moneyOwedString = "$" + String.format("%,.2f", newMoneyOwed);
        moneyOwedText.setText(moneyOwedString);
        artisanMoneyOwed = newMoneyOwed;
    }

    public void subFromArtisanBalance(float recentPayment)
    {
        Map<String, Object> moneyUpdates = new HashMap<>();
        float newMoneyOwed = artisanMoneyOwed - recentPayment;
        moneyUpdates.put("moneyOwedFromCommunityLeader", newMoneyOwed);

        artisanRef.update(moneyUpdates);
        TextView moneyOwedText = findViewById(R.id.moneyOwed);
        String moneyOwedString = "$" + String.format("%,.2f", newMoneyOwed);
        moneyOwedText.setText(moneyOwedString);
        artisanMoneyOwed = newMoneyOwed;

    }

    private void resetPurchaseModal()
    {
        ((NumberPicker) purchaseDialog.findViewById(R.id.numberPicker)).setValue(0);
        purchaseDialog.dismiss();

    }

    public void setUpPurchaseModal()
    {
        purchaseDialog.setContentView(R.layout.modal_purchase);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(purchaseDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;

        final NumberPicker np = purchaseDialog.findViewById(R.id.numberPicker);
        np.setMaxValue(100);
        np.setMinValue(0);
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(this);

        purchaseDialog.findViewById(R.id.confirmPurchaseButton).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                addToArtisanBalance(newPurchase);
                accountingSystem.logPurchase(artisanID, newPurchase, clickedListing);
                resetPurchaseModal();

            }
        });

        purchaseDialog.getWindow().setAttributes(lp);
        purchaseDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void onClickPurchase(Listing model) {
        clickedListing = model;
        itemClickedPrice = model.getPrice();
        setImage(purchaseDialog, model.getPictureURL(), model.getTitle(), model.getDescription());

        purchaseDialog.show();
    }


    public void setUpLogShipmentModal() {

        logShipmentDialog.setContentView(R.layout.modal_log_shipment);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(logShipmentDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;

        logShipmentDialog.getWindow().setAttributes(lp);
        logShipmentDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void onClickLogShipment(Listing model) {

        loadLogShipmentInfo(model);
        setUpLogShipmentListeners(model);

        logShipmentDialog.show();
    }

    public void setUpLogShipmentListeners(Listing model) {
        ConstraintLayout closeButton = logShipmentDialog.findViewById(R.id.closeButtonWrapper);
//        Button minusButton = logShipmentDialog.findViewById(R.id.closeButtonWrapper);
//        Button plusButton = logShipmentDialog.findViewById(R.id.closeButtonWrapper);
//        Button submitButton = logShipmentDialog.findViewById(R.id.closeButtonWrapper);


        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logShipmentDialog.dismiss();
            }
        });

//        minusButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                logShipmentDialog.dismiss();
//            }
//        });
//
//        plusButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                logShipmentDialog.dismiss();
//            }
//        });
//
//        submitButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                logShipmentDialog.dismiss();
//            }
//        });


    }

    public void loadLogShipmentInfo(Listing model) {
        final ImageView image = logShipmentDialog.findViewById(R.id.shipmentImage);
        final TextView title = logShipmentDialog.findViewById(R.id.logShipTitle);

        if(model.getPictureURL()!= null) {
            storageRef.child(model.getPictureURL()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context)
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

        title.setText(model.getTitle());
    }

    public void setUpContactInfoModal() {
        contactInfoModal.setContentView(R.layout.modal_contact_info);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(contactInfoModal.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;

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

        contactInfoModal.getWindow().setAttributes(lp);
        contactInfoModal.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void onClickContactInfoButton(View view)
    {
        contactInfoModal.show();
    }

    private void resetLogPaymentModal()
    {
        ((EditText)logPaymentDialog.findViewById((R.id.amountTextField))).setText(null);
        ((EditText)logPaymentDialog.findViewById((R.id.dateTextField))).setText(null);
        logPaymentDialog.dismiss();
    }

    public void setUpLogPaymentModal() {
        logPaymentDialog.setContentView(R.layout.modal_log_payment);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(logPaymentDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;

        logPaymentDialog.getWindow().setAttributes(lp);

        TextView title = logPaymentDialog.findViewById(R.id.logPaymentTitle);
        title.setText("Log Payment to " + artisanName);

        logPaymentDialog.findViewById(R.id.logPaymentButton).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                EditText amount = (EditText)logPaymentDialog.findViewById((R.id.amountTextField));
                EditText date = (EditText)logPaymentDialog.findViewById((R.id.dateTextField));
                onClickMakePayment(amount, date);
                resetLogPaymentModal();
            }
        });


        logPaymentDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void onClickMakePayment(EditText amount, EditText date)
    {
        System.out.println("clicked make payment");

        String amountPaid = amount.getText().toString();
        String dateToPay = date.getText().toString();
        accountingSystem.logPayment(artisanID, Float.parseFloat(amountPaid) );
        subFromArtisanBalance(Float.parseFloat(amountPaid));
    }


    public void onClickCloseModal(View view)
    {
        contactInfoModal.dismiss();
        resetLogPaymentModal();
        logShipmentDialog.dismiss();
        resetPurchaseModal();
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

        adapter = new ListingAdapter(options, ViewArtisanActivity.this, this);

        RecyclerView.LayoutManager m = new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        recyclerView.setLayoutManager(m);

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

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        newPurchase = itemClickedPrice * newVal;
        TextView pricePer = purchaseDialog.findViewById(R.id.pricePer);
        String pricePerString = "$" + String.format("%,.2f",itemClickedPrice) + " x " + String.valueOf(newVal);
        pricePer.setText(pricePerString);

        TextView totalPrice = purchaseDialog.findViewById(R.id.totalPrice);
        String totalPriceString = "$" + String.format("%,.2f",newPurchase);
        totalPrice.setText(totalPriceString);

    }
}
