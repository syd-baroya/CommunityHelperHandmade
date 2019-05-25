package secapstone.helper.model;

import java.io.Serializable;

public class User implements Serializable {
    private String email;
    private String name;
    private String id;
    private float balance = 0.0f;

    private static User obj;//Early, instance will be created at load time
    private User(){}

    public void setEmail(String email) {this.email = email;}

    public void setID(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static synchronized User getUser(){
        if(obj==null)
            obj=new User();
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

    public float getBalance() { return balance; }
    public void setBalance(float balance) { this.balance = balance; }
    public void updateBalance(float balance) { this.balance += balance; }
}

