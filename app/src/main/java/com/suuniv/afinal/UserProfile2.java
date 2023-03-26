package com.suuniv.afinal;

import com.google.firebase.database.ServerValue;

public class UserProfile2 {
    public String name;
    public String dob;
    public String addressline1;
    public String addressline2;
    public String city;

    public String userType;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String state;
    public Object timestamp;
    public String zip;
    public String userId;
    public String profileImage;
    public UserProfile2(String displayname, String dob, String addressline1, String addressline2,
                       String city, String state, String zip) {
        this.name=displayname;
        this.dob=dob;
        this.addressline1=addressline1;
        this.addressline2= addressline2;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.timestamp=ServerValue.TIMESTAMP;
    }
    public Object getTimestamp(){
        return timestamp;
    }
    public UserProfile2() {

    }
}
