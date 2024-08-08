package com.example.myjapanese;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class Item {

    @SerializedName("itemName")
    private String itemName;

    @SerializedName("date_added")
    private Date dateAdded;

    @SerializedName("itemLocation")
    private Location itemLocation;

    // Getters and setters
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Location getItemLocation() {
        return itemLocation;
    }

    public void setItemLocation(Location itemLocation) {
        this.itemLocation = itemLocation;
    }
}

