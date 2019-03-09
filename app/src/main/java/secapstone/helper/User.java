package secapstone.helper;

import android.os.Parcelable;

import java.io.Serializable;

public class User implements Serializable {
    private String email;
    private String password;
    private String name;
    private String idToken;

    private static User obj=new User();//Early, instance will be created at load time
    private User(){}

    public void setEmail(String email) {this.email = email;}

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static User getUser(){
        return obj;
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
