package com.example.androidproject;

import com.google.firebase.firestore.Exclude;

public class UploadDemande {
    private String mName;
    private String mImageUrl;
    private String mKey;
    private String mUser;

    public UploadDemande() {
        //empty constructor needed
    }

    public UploadDemande(String name, String imageUrl,String user) {
        if (name.trim().equals("")) {
            name = "No Name";
        }
        mName = name;
        mImageUrl = imageUrl;
        mUser=user;


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
    public void setmUser(String user){mUser=user;}
    public String getUser(){return mUser;}




}
