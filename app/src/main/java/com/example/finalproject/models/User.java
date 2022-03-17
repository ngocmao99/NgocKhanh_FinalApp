package com.example.finalproject.models;

public class User {
    private String userId;
    private String fullName;
    private String email;
    private String userImgId;
    private String dob;
    private String phoneNumber;
    private String gender;

    //Initial constructor
    public User() {
    }

    public User(String userId, String fullName, String email, String userImgId, String dob, String phoneNumber, String gender) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.userImgId = userImgId;
        this.dob = dob;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserImgId() {
        return userImgId;
    }

    public void setUserImgId(String userImgId) {
        this.userImgId = userImgId;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
