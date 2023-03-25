package com.suuniv.afinal;

import com.google.firebase.database.ServerValue;

public class paymentProfile {    public String bankNumber;
    public String bankType;
    public String expDate;
    public Object timestamp;
    public String secCode;
    public paymentProfile(String displayname, String email, String phone,String userType) {
        this.bankNumber =displayname;
        this.bankType =email;
        this.expDate =phone;
        this.timestamp= ServerValue.TIMESTAMP;
        this.secCode = userType;
    }
    public Object getTimestamp(){
        return timestamp;
    }
    public paymentProfile() {

    }

    public String getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getexpDate() {
        return expDate;
    }

    public void setexpDate(String phone) {
        this.expDate = phone;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

    public String getSecCode() {
        return secCode;
    }

    public void setSecCode(String secCode) {
        this.secCode = secCode;
    }
}