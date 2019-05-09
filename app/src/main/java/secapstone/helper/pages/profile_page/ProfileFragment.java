package secapstone.helper.pages.profile_page;


import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amazon.identity.auth.device.AuthError;
import com.amazon.identity.auth.device.api.Listener;
import com.amazon.identity.auth.device.api.authorization.AuthorizationManager;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import secapstone.helper.R;
import secapstone.helper.model.Artisan;
import secapstone.helper.model.User;
import secapstone.helper.pages.MainActivity;
import secapstone.helper.pages.artisans_page.ArtisanAdapter;
import secapstone.helper.pages.custom_ui.CustomTextField;
import secapstone.helper.pages.log_payment.LogPaymentActivity;
import secapstone.helper.pages.login.LoginActivity;
import secapstone.helper.pages.view_artisan.ViewReportsActivity;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class ProfileFragment extends Fragment {

    //reference to user's artisans in database
    private CollectionReference artisansRef;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private View view;

    private Button logoutButton;
    private RecyclerView recyclerView;
    private ArtisanAdapter adapter;
    private Dialog reportIssueModal;
    private Button reportIssueButton;
    private CustomTextField reportField;
    private ConstraintLayout closeButton;
    private Button submitReportButton;
    public void setArtisanRef(CollectionReference artisansRef){
        this.artisansRef = artisansRef;
    }

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile, container, false);

        reportIssueModal = new Dialog(this.getContext());
        reportIssueModal.setContentView(R.layout.modal_report_issue);
        reportIssueModal.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        logoutButton = view.findViewById(R.id.logoutButton);
        reportIssueButton = view.findViewById(R.id.reportIssueButton);
        submitReportButton = reportIssueModal.findViewById(R.id.submitReportButton);
        reportField = reportIssueModal.findViewById(R.id.reportText);
        closeButton = reportIssueModal.findViewById(R.id.closeButton);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickLogout();
            }
        });
        reportIssueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickReportIssue();
            }
        });
        submitReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSubmitReport();
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickCloseModal();
            }
        });


        User user_info = User.getUser();
        setImage("",  user_info.getName());

        setStatusBarToDark();


        return view;
    }

    public void onClickLogout()
    {
        FirebaseAuth.getInstance().signOut();

        AuthorizationManager.signOut(getContext(), new Listener< Void, AuthError >() {
            @Override
            public void onSuccess(Void response) {
                // Set logged out state in UI

                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
            }
            @Override
            public void onError(AuthError authError) {
                // Log the error
            }
        });
    }

    private void setImage(String url, String name) {
        TextView nameTitle = view.findViewById(R.id.cga_name);
        nameTitle.setText(name);
    }


    public void onClickReportsButton(View view)
    {
        startActivity(new Intent(getActivity(), ViewReportsActivity.class));
    }

    public void onClickReportIssue()
    {
        reportIssueModal.show();
    }

    public void onClickSubmitReport() {
        if (reportField.getText().toString().length() > 0) {
            Map<String, Object> report = new HashMap<>();
            report.put("report", reportField.getText().toString());
            report.put("UserID", User.getUser().getID());
            report.put("time", Calendar.getInstance().getTime().toString());

            db.collection("reports").document()
                    .set(report)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            reportIssueModal.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                        }
                    });
        }
    }

    public void onClickCloseModal() {
        reportIssueModal.dismiss();
    }

    public void onClickLogPayments(View view)
    {
        startActivity(new Intent(getActivity(), LogPaymentActivity.class));
    }

    public void onClickBackButton(View view)
    {
        startActivity(new Intent(getActivity(), MainActivity.class));
    }

    public void onClickContactInfoButton(View view)
    {

    }


    @TargetApi(23)
    public void setStatusBarToDark() {
        int flags = getActivity().getWindow().getDecorView().getSystemUiVisibility();
        flags = flags ^ View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        getActivity().getWindow().getDecorView().setSystemUiVisibility(flags);
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.almostBlack));
    }

}
