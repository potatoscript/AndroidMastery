// Item.java
package com.example.myjapanese;

public class Item {

    private String itemName;
    private String date_added;
    private int itemLocation;

    public Item(String itemName, String date_added, int itemLocation) {
        this.itemName = itemName;
        this.date_added = date_added;
        this.itemLocation = itemLocation;
    }

    public String getItemName() {
        return itemName;
    }

    public String getDateAdded() {
        return date_added;
    }

    public int getItemLocationId() {
        return itemLocation;
    }
}
