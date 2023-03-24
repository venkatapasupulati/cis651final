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

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
