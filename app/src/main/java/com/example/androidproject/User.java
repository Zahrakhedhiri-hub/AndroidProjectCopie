package com.example.androidproject;

public class User {
    private String mId;
    private  String mFullName;
    private String mPassword;
    private String mEmail;
    private String mPhone;

    public void User(String id,String password,String email,String phone){
     mId=id;
     mEmail=email;
     mPhone=phone;
     mPassword=password;
    }

    public User() {
        //empty constructor needed
    }
    public void setId(String id){mId=id;}
    public void setFullName(String fullName){mFullName=fullName;}
    public void setPassword(String password){mPassword=password;}
    public void setPhone(String phone){mPhone=phone;}
    public void setEmail(String email){mEmail=email;}

    public String getPhone(){return mPhone;}
    public String getFullName(){return mFullName;}
    public String getPassword(){return mPassword;}
    public String getEmail(){return mEmail;}
}
