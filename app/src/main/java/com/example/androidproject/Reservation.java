package com.example.androidproject;

import com.google.firebase.firestore.Exclude;

public class Reservation {
    private String mName;
    private String mImageUrl;
    private String mKey;
    private String mUser;
    private String mPreneur;
    public Reservation() {
        //empty constructor needed
    }
    public Reservation(String name, String imageUrl,String user,String preneur) {
        if (name.trim().equals("")) {
            name = "No Name";
        }
        mName = name;
        mImageUrl = imageUrl;
        mUser=user;
        mPreneur=preneur;

    }
    public String getName() {
        return mName;
    }
    @Exclude
    public void setKey(String key) {
        mKey = key;
    }

    @Exclude
    public String getKey() {
        return mKey;
    }
    public void setName(String name) {
        mName = name;
    }
    public String getImageUrl() {
        return mImageUrl;
    }
    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }
    public void setUser(String user){mUser=user;}
    public String getUser(){return mUser;}
    public void setPreneur(String preneur){mPreneur=preneur;}
    public String getPreneur(){return mPreneur;}

}

