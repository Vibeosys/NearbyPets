package com.nearbypets.data;

/**
 * Created by shrinivas on 09-06-2016.
 */
public class HiddenAdDbDTO {
    private String adId;
    private int status;

    public HiddenAdDbDTO(String adId, int status) {
        this.adId = adId;
        this.status = status;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
