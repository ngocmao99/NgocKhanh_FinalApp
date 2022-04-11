package com.example.finalproject.models;

public class User {
    private String userId;
    private String fullName;
    private String email;
    private String userImgId;
    private String dob;
    private String phoneNumber;
    private String gender;
    private double latitude;
    private double longitude;
    private String province;
    private String postalCode;
    private String district;
    private String ward;
    private String houseNumber;

    //Initial constructor
    public User() {
    }

    public User(String userId, String fullName, String email, String userImgId, String dob,
                String phoneNumber, String gender, double latitude, double longitude, String province,
                String postalCode, String district, String ward, String houseNumber) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.userImgId = userImgId;
        this.dob = dob;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.latitude = latitude;
        this.longitude = longitude;
        this.province = province;
        this.postalCode = postalCode;
        this.district = district;
        this.ward = ward;
        this.houseNumber = houseNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }
}
