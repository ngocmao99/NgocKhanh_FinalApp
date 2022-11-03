package com.example.finalproject.models;

import java.io.Serializable;

public class Item implements Serializable {
    String itemAddress;
    String itemName;
    String image;
    String itemPrice;
    String itemArea;
    int itemId;

    public Item() {
    }

    public Item(String itemAddress, String itemName, String image, String itemPrice, String itemArea, int itemId) {
        this.itemAddress = itemAddress;
        this.itemName = itemName;
        this.image = image;
        this.itemPrice = itemPrice;
        this.itemArea = itemArea;
        this.itemId = itemId;
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

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
}

