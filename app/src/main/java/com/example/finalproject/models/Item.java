package com.example.finalproject.models;

public class Item {
    private String itemName;
    private String itemId;
    private String itemType;
    private String itemImg;
    private String userId;
    private String description;
    private String provinces;
    private String district;
    private String address;
    private String zipCode;
    private String itemArea;
    private String itemRentPrice;
    private String itemMaintenance;
    private String itemDeposit;

    public Item(String itemName, String itemId, String itemType, String itemImg, String userId, String description, String provinces, String district, String address, String zipCode, String itemArea, String itemRentPrice, String itemMaintenance, String itemDeposit) {
        this.itemName = itemName;
        this.itemId = itemId;
        this.itemType = itemType;
        this.itemImg = itemImg;
        this.userId = userId;
        this.description = description;
        this.provinces = provinces;
        this.district = district;
        this.address = address;
        this.zipCode = zipCode;
        this.itemArea = itemArea;
        this.itemRentPrice = itemRentPrice;
        this.itemMaintenance = itemMaintenance;
        this.itemDeposit = itemDeposit;

    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemImg() {
        return itemImg;
    }

    public void setItemImg(String itemImg) {
        this.itemImg = itemImg;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProvinces() {
        return provinces;
    }

    public void setProvinces(String provinces) {
        this.provinces = provinces;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getItemArea() {
        return itemArea;
    }

    public void setItemArea(String itemArea) {
        this.itemArea = itemArea;
    }

    public String getItemRentPrice() {
        return itemRentPrice;
    }

    public void setItemRentPrice(String itemRentPrice) {
        this.itemRentPrice = itemRentPrice;
    }

    public String getItemMaintenance() {
        return itemMaintenance;
    }

    public void setItemMaintenance(String itemMaintenance) {
        this.itemMaintenance = itemMaintenance;
    }

    public String getItemDeposit() {
        return itemDeposit;
    }

    public void setItemDeposit(String itemDeposit) {
        this.itemDeposit = itemDeposit;
    }
}
