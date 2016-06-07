package com.nearbypets.data;

/**
 * Created by akshay on 06-06-2016.
 */
public class GetSavedAd {

    private double latitude;
    private double longitude;
    private String userId;

    public GetSavedAd(double latitude, double longitude, String userId) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.userId = userId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
