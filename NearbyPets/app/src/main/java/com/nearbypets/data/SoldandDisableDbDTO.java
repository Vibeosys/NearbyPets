package com.nearbypets.data;

/**
 * Created by shrinivas on 07-06-2016.
 */
public class SoldandDisableDbDTO extends BaseDTO {
    private String adId;

    public SoldandDisableDbDTO(String adId) {
        this.adId = adId;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    @Override
    public String toString() {
        return "SoldandDisableDbDTO{" +
                "adId='" + adId + '\'' +
                '}';
    }
}
