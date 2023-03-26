package com.suuniv.afinal.requests;

import com.google.firebase.database.ServerValue;

public class RequestInfo {

    private String requestId;
   private  String requesterUid;

   private String dogwalkerUserId;
   private String pawId;

   private  Object timestamp;

   private String requestStatus;

   public  RequestInfo(){
       this.timestamp= ServerValue.TIMESTAMP;
   }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequesterUid() {
        return requesterUid;
    }

    public void setRequesterUid(String requesterUid) {
        this.requesterUid = requesterUid;
    }

    public String getDogwalkerUserId() {
        return dogwalkerUserId;
    }

    public void setDogwalkerUserId(String dogwalkerUserId) {
        this.dogwalkerUserId = dogwalkerUserId;
    }

    public String getPawId() {
        return pawId;
    }

    public void setPawId(String pawId) {
        this.pawId = pawId;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }
}
