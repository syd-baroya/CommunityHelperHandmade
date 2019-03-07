package secapstone.helper;

import android.os.Parcelable;

import java.io.Serializable;

public class User implements Serializable {
    private String email;
    private String password;
    private String name;
    private String idToken;

    public User(String email, String password, String name, String idToken){
        this.email = email;
        this.password = password;
        this.name = name;
        this.idToken = idToken;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
    public String getIdToken(){
        return idToken;
    }
}
