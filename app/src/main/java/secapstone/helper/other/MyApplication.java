package secapstone.helper.other;

import android.app.Application;

//import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
