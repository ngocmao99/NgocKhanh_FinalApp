package com.example.finalproject.models;

import java.io.Serializable;

public class Item implements Serializable {
    String itemAddress;
    String itemName;
    String image;
    String itemId;
    String itemPrice;
    String itemArea;
    String UserId;
    String itemLivingroom;
    String itemType;
    String itemBedrooms;
    String itemBathrooms;
    String itemKitchen;
    String itemBalcony;
    String itemGarage;
    String itemFurnishing;
    String itemFloor;
    String itemGym;
    String itemFuelstation;
    String itemCarparking;
    String itemPark;
    String itemMaintainence;
    String itemDeposit;

    public Item() {
    }

    public Item(String itemAddress, String itemName, String image, String itemId, String itemPrice, String itemArea, String userId, String itemLivingroom, String itemType, String itemBedrooms, String itemBathrooms, String itemKitchen, String itemBalcony, String itemGarage, String itemFurnishing, String itemFloor, String itemGym, String itemFuelstation, String itemCarparking, String itemPark, String itemMaintainence, String itemDeposit) {
        this.itemAddress = itemAddress;
        this.itemName = itemName;
        this.image = image;
        this.itemId = itemId;
        this.itemPrice = itemPrice;
        this.itemArea = itemArea;
        this.UserId = userId;
        this.itemLivingroom = itemLivingroom;
        this.itemType = itemType;
        this.itemBedrooms = itemBedrooms;
        this.itemBathrooms = itemBathrooms;
        this.itemKitchen = itemKitchen;
        this.itemBalcony = itemBalcony;
        this.itemGarage = itemGarage;
        this.itemFurnishing = itemFurnishing;
        this.itemFloor = itemFloor;
        this.itemGym = itemGym;
        this.itemFuelstation = itemFuelstation;
        this.itemCarparking = itemCarparking;
        this.itemPark = itemPark;
        this.itemMaintainence = itemMaintainence;
        this.itemDeposit = itemDeposit;
    }

    public String getItemAddress() {
        return itemAddress;
    }

    public void setItemAddress(String itemAddress) {
        this.itemAddress = itemAddress;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemArea() {
        return itemArea;
    }

    public void setItemArea(String itemArea) {
        this.itemArea = itemArea;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getItemLivingroom() {
        return itemLivingroom;
    }

    public void setItemLivingroom(String itemLivingroom) {
        this.itemLivingroom = itemLivingroom;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemBedrooms() {
        return itemBedrooms;
    }

    public void setItemBedrooms(String itemBedrooms) {
        this.itemBedrooms = itemBedrooms;
    }

    public String getItemBathrooms() {
        return itemBathrooms;
    }

    public void setItemBathrooms(String itemBathrooms) {
        this.itemBathrooms = itemBathrooms;
    }

    public String getItemKitchen() {
        return itemKitchen;
    }

    public void setItemKitchen(String itemKitchen) {
        this.itemKitchen = itemKitchen;
    }

    public String getItemBalcony() {
        return itemBalcony;
    }

    public void setItemBalcony(String itemBalcony) {
        this.itemBalcony = itemBalcony;
    }

    public String getItemGarage() {
        return itemGarage;
    }

    public void setItemGarage(String itemGarage) {
        this.itemGarage = itemGarage;
    }

    public String getItemFurnishing() {
        return itemFurnishing;
    }

    public void setItemFurnishing(String itemFurnishing) {
        this.itemFurnishing = itemFurnishing;
    }

    public String getItemFloor() {
        return itemFloor;
    }

    public void setItemFloor(String itemFloor) {
        this.itemFloor = itemFloor;
    }

    public String getItemGym() {
        return itemGym;
    }

    public void setItemGym(String itemGym) {
        this.itemGym = itemGym;
    }

    public String getItemFuelstation() {
        return itemFuelstation;
    }

    public void setItemFuelstation(String itemFuelstation) {
        this.itemFuelstation = itemFuelstation;
    }

    public String getItemCarparking() {
        return itemCarparking;
    }

    public void setItemCarparking(String itemCarparking) {
        this.itemCarparking = itemCarparking;
    }

    public String getItemPark() {
        return itemPark;
    }

    public void setItemPark(String itemPark) {
        this.itemPark = itemPark;
    }

    public String getItemMaintainence() {
        return itemMaintainence;
    }

    public void setItemMaintainence(String itemMaintainence) {
        this.itemMaintainence = itemMaintainence;
    }

    public String getItemDeposit() {
        return itemDeposit;
    }

    public void setItemDeposit(String itemDeposit) {
        this.itemDeposit = itemDeposit;
    }
}

