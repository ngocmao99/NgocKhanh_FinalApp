package com.example.finalproject.models;

import java.io.Serializable;

public class Item implements Serializable {
     String location;
     String itemName;
     String image;

    public Item() {
    }

    public Item(String location, String itemName, String image) {
        this.location = location;
        this.itemName = itemName;
        this.image = image;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
}
