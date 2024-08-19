package com.example.myjapanese;

import com.google.gson.annotations.SerializedName;

public class Item {
    @SerializedName("itemName")
    private String itemName;

    @SerializedName("itemLocation")
    private int itemLocation;  // Send the ID of the location

    // Constructor
    public Item(String itemName, int itemLocation) {
        this.itemName = itemName;
        this.itemLocation = itemLocation;
    }

    public String getItemName() {
        return itemName;
    }

    // Getters and setters...
}
