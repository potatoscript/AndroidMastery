package com.example.myjapanese;

import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("locationName")
    private String locationName;

    // Getters and setters
    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
