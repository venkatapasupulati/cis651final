package com.suuniv.afinal;

import com.google.firebase.database.ServerValue;

public class User {
    public String displayname;
    public String email;
    public String phone;
    public Object timestamp;
    public String userType;
    public User(String displayname, String email, String phone,String userType) {
        this.displayname=displayname;
        this.email=email;
        this.phone=phone;
        this.timestamp= ServerValue.TIMESTAMP;
        this.userType = userType;
    }
    public Object getTimestamp(){
        return timestamp;
    }
    public User() {

    }
}
