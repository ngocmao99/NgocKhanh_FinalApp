package com.example.finalproject.models;

public class Property {
    private String propertyId;
    private String propertyName;
    private String propertyType;
    private String propertyAddress;
    private int propertyImage;

    public Property() {
    }

    public Property(String propertyId, String propertyName, String propertyType, String propertyAddress, int propertyImage) {
        this.propertyId = propertyId;
        this.propertyName = propertyName;
        this.propertyType = propertyType;
        this.propertyAddress = propertyAddress;
        this.propertyImage = propertyImage;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getPropertyAddress() {
        return propertyAddress;
    }

    public void setPropertyAddress(String propertyAddress) {
        this.propertyAddress = propertyAddress;
    }

    public int getPropertyImage() {
        return propertyImage;
    }

    public void setPropertyImage(int propertyImage) {
        this.propertyImage = propertyImage;
    }
}
