package com.nearbypets.data;

/**
 * Created by akshay on 13-06-2016.
 */
public class UserSettingDbDTO extends BaseDTO {

    private String userId;
    private int radiusInKm;

    public UserSettingDbDTO(String userId, int radiusInKm) {
        this.userId = userId;
        this.radiusInKm = radiusInKm;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getRadiusInKm() {
        return radiusInKm;
    }

    public void setRadiusInKm(int radiusInKm) {
        this.radiusInKm = radiusInKm;
    }
}
