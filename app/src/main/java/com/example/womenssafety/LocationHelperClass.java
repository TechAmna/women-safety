package com.example.womenssafety;

public class LocationHelperClass {
    private double latitude;
    private double longitude;
    private String timestamp;  // Optional: You can include a timestamp or any other metadata

    // Default constructor (no-argument constructor)
    public LocationHelperClass() {
        // Initialize fields with default values if necessary
    }

    // Parameterized constructor
    public LocationHelperClass(double latitude, double longitude, String timestamp) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }


    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    public LocationHelperClass(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;

    }

}

