package secapstone.helper.pages.view_artisan;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import secapstone.helper.R;
import secapstone.helper.model.PayoutTransaction;
import secapstone.helper.pages.custom_ui.CustomTextField;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ViewReportsActivity extends AppCompatActivity
{
    String artisanID="";
    String cgaID="";

    static final String TAG = "ViewReportsActivity";
    List<String> transactionData;
    Map<String, Object> map;
    ListView listView;
    ArrayAdapter<String> adapter;
    Dialog downloadFileModal;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reports);
        listView = findViewById(R.id.listView);
        downloadFileModal = new Dialog(this);

        setUpDownloadFileModal();


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

    public void setUpDownloadFileModal() {
        downloadFileModal.setContentView(R.layout.modal_download_report);
        final EditText fileName = downloadFileModal.findViewById(R.id.fileNameField);
        final Button downloadModalButton = downloadFileModal.findViewById(R.id.downloadModalButton);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(downloadFileModal.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;

        downloadFileModal.getWindow().setAttributes(lp);
        downloadFileModal.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        fileName.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (fileName.getText().length() > 0) {
                    downloadModalButton.setEnabled(true);
                } else {
                    downloadModalButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {}

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,int arg2, int arg3) {}
        });

        downloadModalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickDownloadButtonModal(fileName.getText().toString());
            }
        });

    }

    public void onClickDownloadButton(View view)
    {
        downloadFileModal.show();
    }
    public void onClickDownloadButtonModal(final String fileNameField)
    {
        List<String[]> listTransData = transformTransactionDataForCSV();
        writeToCSV(listTransData, fileNameField);
        downloadFileModal.dismiss();
    }

    private List<String[]> transformTransactionDataForCSV()
    {
        List<String[]> newTransData = new ArrayList<>();
        for(String data : transactionData)
        {
            String[] dataArray = data.split("\\s+");
            newTransData.add(dataArray);
        }
        return newTransData;
    }
    public void onClickCloseModal(View view)
    {
        ((EditText)downloadFileModal.findViewById((R.id.fileNameField))).setText(null);
        downloadFileModal.dismiss();

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
                alertBuilder.setTitle("CSV Download Status");
                alertBuilder.setMessage("CSV file successfully downloaded!");
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
        FirebaseFirestore db;
        transactionData = new ArrayList<>();
        map = new HashMap<>();
        adapter = new ArrayAdapter<>(this, R.layout.report_item, R.id.reportInfo, transactionData);

        getIncomingIntent();
        db = FirebaseFirestore.getInstance();

        if(artisanID.length() > 0)
            getTransactions("artisanID", artisanID, db);
        else if(cgaID.length()>0)
            getTransactions("cgaID", cgaID, db);
    }

    private void getTransactions(String fieldName, String id, FirebaseFirestore db)
    {
        CollectionReference ref;
        ref = db.collection("PayoutTransactions");
        ref.whereEqualTo(fieldName, id)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        transactionData.add("CGA to Artisan Payments");
                        transactionData.add("Date" + "                    " + "Amount" + "       " + "Artisan ID");

                        for (QueryDocumentSnapshot doc : value) {
                            map = doc.getData();
                            transactionData.add(map.get("date") + "      " + map.get("amount") + "               " + map.get("artisanID"));
                        }
                        listView.setAdapter(adapter);

                    }
                });
        ref = db.collection("ProductTransactions");
        ref.whereEqualTo(fieldName, id)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        transactionData.add("Product Purchases");
                        transactionData.add("Date" + "                  " + "Amount" + "   " + "Product ID" + "                  " + "Artisan ID");

                        for (QueryDocumentSnapshot doc : value) {
                            map = doc.getData();
                            transactionData.add(map.get("date") + "     " + map.get("amount") + "        " + map.get("productID") + " " + map.get("artisanID"));

                        }
                        listView.setAdapter(adapter);
                    }
                });
        ref = db.collection("Shipments");
        ref.whereEqualTo(fieldName, id)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        transactionData.add("Shipment of Products");
                        transactionData.add("Date" + "                  " + "Amount" + "   " + "Product ID" + "                  " + "Artisan ID");

                        for (QueryDocumentSnapshot doc : value) {
                            map = doc.getData();
                            transactionData.add(map.get("date") + "     " + map.get("amount") + "          " + map.get("productID") + " " + map.get("artisanID"));
                        }
                        listView.setAdapter(adapter);
                    }
                });
    }

    private void writeToCSV(List<String[]> listTransData, String fileName)
    {
        String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        String filePath = baseDir + File.separator + fileName + ".csv";
        File f = new File(filePath);
        if(f.exists())
            f.delete();
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


        writer.writeAll(listTransData);

        try {
            writer.close();
            Log.d(TAG, "csv file was made and written to");
            Log.d(TAG, "file location: " + filePath);


        } catch (IOException e) {
            e.printStackTrace();
        }
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
