package com.example.myjapanese;

import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("id")
    private int id;

    @SerializedName("locationName")
    private String locationName;

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    @Override
    public String toString() {
        return locationName;  // This will be displayed in the Spinner
    }
}
