package com.example.finalproject.models;

import java.io.Serializable;

public class Property implements Serializable {
    private String propertyId;
    private String userId;
    private String propertyName;
    private String propertyLocation;
    private double lat;
    private double lng;
    private String province;
    private String postalCode;
    private String district;
    private String ward;
    private String houseNumber;
    private String propertyImage;
    private String propertyType;
    private String propertyFloor;
    private String propertyDescription;
    private String propertyFacilities;
    private int bedroom;
    private int bathroom;
    private double area;
    private long price;
    private long timestamp;

    public Property() {
    }

    public Property(String propertyId, String userId, String propertyName, String propertyLocation, double lat, double lng, String province, String postalCode, String district, String ward, String houseNumber, String propertyImage, String propertyType, String propertyFloor, String propertyDescription, String propertyFacilities, int bedroom, int bathroom, double area, long price, long timestamp) {
        this.propertyId = propertyId;
        this.userId = userId;
        this.propertyName = propertyName;
        this.propertyLocation = propertyLocation;
        this.lat = lat;
        this.lng = lng;
        this.province = province;
        this.postalCode = postalCode;
        this.district = district;
        this.ward = ward;
        this.houseNumber = houseNumber;
        this.propertyImage = propertyImage;
        this.propertyType = propertyType;
        this.propertyFloor = propertyFloor;
        this.propertyDescription = propertyDescription;
        this.propertyFacilities = propertyFacilities;
        this.bedroom = bedroom;
        this.bathroom = bathroom;
        this.area = area;
        this.price = price;
        this.timestamp = timestamp;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyLocation() {
        return propertyLocation;
    }

    public void setPropertyLocation(String propertyLocation) {
        this.propertyLocation = propertyLocation;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
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

    public String getPropertyImage() {
        return propertyImage;
    }

    public void setPropertyImage(String propertyImage) {
        this.propertyImage = propertyImage;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getPropertyFloor() {
        return propertyFloor;
    }

    public void setPropertyFloor(String propertyFloor) {
        this.propertyFloor = propertyFloor;
    }

    public String getPropertyDescription() {
        return propertyDescription;
    }

    public void setPropertyDescription(String propertyDescription) {
        this.propertyDescription = propertyDescription;
    }

    public String getPropertyFacilities() {
        return propertyFacilities;
    }

    public void setPropertyFacilities(String propertyFacilities) {
        this.propertyFacilities = propertyFacilities;
    }

    public int getBedroom() {
        return bedroom;
    }

    public void setBedroom(int bedroom) {
        this.bedroom = bedroom;
    }

    public int getBathroom() {
        return bathroom;
    }

    public void setBathroom(int bathroom) {
        this.bathroom = bathroom;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }
}
