package secapstone.helper;

import android.os.Parcelable;

import java.io.Serializable;

public class User implements Serializable {
    private String email;
    private String name;
    private String id;

    private static User obj=new User();//Early, instance will be created at load time
    private User(){}

    public void setEmail(String email) {this.email = email;}

    public void setID(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getID(){
        return id;
    }
}
