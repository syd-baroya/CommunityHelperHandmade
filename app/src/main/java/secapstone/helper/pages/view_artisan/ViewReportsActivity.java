package secapstone.helper.pages.view_artisan;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import secapstone.helper.R;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ViewReportsActivity extends AppCompatActivity
{
    private String artisanID="";
    private String cgaID="";
    private FirebaseFirestore db;
    private static final String TAG = "ViewReportsActivity";
    private List<Object[]> payoutData, productData, shipmentData;
    private View view;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reports);


        if (!checkPermission()) {
            openActivity();
        } else {
            if (checkPermission()) {
                requestPermissionAndContinue();
            } else {
                openActivity();
            }
        }

    }

    private static final int PERMISSION_REQUEST_CODE = 200;
    private boolean checkPermission() {

        return ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ;
    }

    private void requestPermissionAndContinue() {
        if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, WRITE_EXTERNAL_STORAGE)
                    && ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE)) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setCancelable(true);
//                alertBuilder.setTitle(getString(R.string.permission_necessary));
//                alertBuilder.setMessage(R.string.storage_permission_is_necessary_to_wrote_event);
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(ViewReportsActivity.this, new String[]{WRITE_EXTERNAL_STORAGE
                                , READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                    }
                });
                AlertDialog alert = alertBuilder.create();
                alert.show();
                Log.e("", "permission denied, show dialog");
            } else {
                ActivityCompat.requestPermissions(ViewReportsActivity.this, new String[]{WRITE_EXTERNAL_STORAGE,
                        READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        } else {
            openActivity();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (permissions.length > 0 && grantResults.length > 0) {

                boolean flag = true;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        flag = false;
                    }
                }
                if (flag) {
                    openActivity();
                } else {
                    finish();
                }

            } else {
                finish();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void openActivity() {
        //add your further process after giving permission or to download images from remote server.
        payoutData = new ArrayList<>();
        productData = new ArrayList<>();
        shipmentData = new ArrayList<>();


        getIncomingIntent();
        db = FirebaseFirestore.getInstance();
        if(artisanID.length() > 0)
            getArtisanTransactions();
        else if(cgaID.length()>0)
            getCGATransactions();
    }

    private void writeToCSV(List<Object[]> dataList)
    {
        String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        String fileName = "AnalysisData.csv";
        String filePath = baseDir + File.separator + fileName;
        File f = new File(filePath);
        CSVWriter writer=null;
        FileWriter mFileWriter;

        // File exist
        if(f.exists()&&!f.isDirectory())
        {
            try {
                mFileWriter = new FileWriter(filePath, true);
                writer = new CSVWriter(mFileWriter);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            try {
                writer = new CSVWriter(new FileWriter(filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(writer==null)
        {
            Log.d(TAG, "csv file was not made");
            return;
        }

        String[] data = {"Address", "Amount", "artisanID", "cgaID"};

        writer.writeNext(data);
        //writer.writeAll(dataList);

        try {
            writer.close();
            Log.d(TAG, "csv file was made and written to");
            Log.d(TAG, "file location: " + filePath);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        View view = findViewById(R.id.view_report_container);
        TableLayout ll = view.findViewById(R.id.table);


        for (int i = 0; i < payoutData.size(); i++) {

            TableRow row = new TableRow(this);
            TableLayout.LayoutParams tableRowParams=
                    new TableLayout.LayoutParams
                            (TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);

            int leftMargin=2;
            int topMargin=2;
            int rightMargin=2;
            int bottomMargin=2;

            tableRowParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

            //row.setLayoutParams(tableRowParams);
            TextView addressText = new TextView(this);
            TextView amountText = new TextView(this);
            TextView dateText = new TextView(this);
            addressText.setLayoutParams(tableRowParams);
            amountText.setLayoutParams(tableRowParams);
//            String[] map = payoutData.get(i);
//            String address = map[0];
//            String amount = map[1];
//            addressText.setText(address);
//            amountText.setText(amount);
            row.addView(addressText);
            row.addView(amountText);
            ll.addView(row, i);
        }
//        for (int i = 0; i < productData.size(); i++) {
//
//            TableRow row = new TableRow(this);
//            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
//            row.setLayoutParams(lp);
//            TextView productText = new TextView(this);
//            TextView amountText = new TextView(this);
//            TextView dateText = new TextView(this);
//            Map<String, Object> map = productData.get(i);
//            String product = map.get("productID").toString();
//            String amount = map.get("amount").toString();
//            productText.setText(product);
//            amountText.setText(amount);
//            row.addView(productText);
//            row.addView(amountText);
//            ll.addView(row, i);
//        }
//        for (int i = 0; i < shipmentData.size(); i++) {
//
//            TableRow row = new TableRow(this);
//            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
//            row.setLayoutParams(lp);
//            TextView productText = new TextView(this);
//            TextView amountText = new TextView(this);
//            TextView dateText = new TextView(this);
//            Map<String, Object> map = shipmentData.get(i);
//            String product = map.get("productID").toString();
//            String amount = map.get("amount").toString();
//            productText.setText(product);
//            amountText.setText(amount);
//            row.addView(productText);
//            row.addView(amountText);
//            ll.addView(row, i);
//        }
    }

    private void getArtisanTransactions()
    {
        db.collection("PayoutTransactions")
                .whereEqualTo("artisanID", artisanID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Object[] values = document.getData().values().toArray(new Object[0]);
                                payoutData.add(values);
//                                Log.d(TAG, "Payout Transactions: " + document.getId() + " => " + document.getData());
                            }
                            writeToCSV(payoutData);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
//        db.collection("ProductTransactions")
//                .whereEqualTo("artisanID", artisanID)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                productData.add(document.getData());
////                                Log.d(TAG, "Product Transactions: " + document.getId() + " => " + document.getData());
//                            }
//                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
//        db.collection("Shipments")
//                .whereEqualTo("artisanID", artisanID)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                shipmentData.add(document.getData());
////                                Log.d(TAG, "Shipments: " + document.getId() + " => " + document.getData());
//                            }
//                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
    }

    private void getCGATransactions()
    {
//        db.collection("PayoutTransactions")
//                .whereEqualTo("cgaID", cgaID)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                String[] values = document.getData().values().toArray(new String[0]);
//                                payoutData.add(values);
////                                Log.d(TAG, "Payout Transactions: " + document.getId() + " => " + document.getData());
//                            }
//                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
//        db.collection("ProductTransactions")
//                .whereEqualTo("cgaID", cgaID)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                String[] values = document.getData().values().toArray(new String[0]);
//                                productData.add(values);
////                                Log.d(TAG, "Product Transactions: " + document.getId() + " => " + document.getData());
//                            }
//                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
//        db.collection("Shipments")
//                .whereEqualTo("cgaID", cgaID)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                String[] values = document.getData().values().toArray(new String[0]);
//                                shipmentData.add(values);
////                                Log.d(TAG, "Shipments: " + document.getId() + " => " + document.getData());
//                            }
//                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("artisanID")) {
            artisanID = getIntent().getStringExtra("artisanID");
        }
        else if (getIntent().hasExtra("cgaID")) {
            cgaID = getIntent().getStringExtra("cgaID");
        }
    }



}
