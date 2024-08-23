// Item.java
package com.example.myjapanese;

public class Item {

    private int id; // Add this field to store the item's ID
    private String itemName;
    private String date_added;
    private int itemLocation;

    public Item(int id, String itemName, String date_added, int itemLocation) {
        this.id = id;
        this.itemName = itemName;
        this.date_added = date_added;
        this.itemLocation = itemLocation;
    }

    public int getId() {
        return id;
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

    public void setId(int id) {
        this.id = id;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setDateAdded(String date_added) {
        this.date_added = date_added;
    }

    public void setItemLocationId(int itemLocation) {
        this.itemLocation = itemLocation;
    }
}
