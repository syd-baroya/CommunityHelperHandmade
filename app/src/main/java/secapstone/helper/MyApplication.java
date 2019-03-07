package secapstone.helper;

import android.app.Application;

//import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.FileInputStream;
import java.io.IOException;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        try {
//            FileInputStream serviceAccount = new FileInputStream("./handmade-17b08-firebase-adminsdk-sxsop-f3b4136f67.json");
//            FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();
//            FirebaseApp.initializeApp(options);
//
//        }catch(IOException e){
//
//        }
        FirebaseApp.initializeApp(this);

    }
}
