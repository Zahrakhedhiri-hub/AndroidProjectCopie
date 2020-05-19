package com.example.androidproject;

import com.google.firebase.firestore.Exclude;

public class Upload {
    private String mName;
    private String mImageUrl;
    private String mKey;
    private String mUser;
    public String mAdresse;
    public Upload() {
        //empty constructor needed
    }
    public Upload(String name, String imageUrl,String user,String adresse) {
        if (name.trim().equals("")) {
            name = "No Name";
        }
        mName = name;
        mImageUrl = imageUrl;
        mUser=user;
        mAdresse=adresse;

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
    public void setAdresse(String adresse){mAdresse=adresse;}
    public String getAdresse(){return mAdresse;}

}
