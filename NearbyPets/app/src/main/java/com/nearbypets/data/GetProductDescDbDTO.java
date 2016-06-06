package com.nearbypets.data;

/**
 * Created by akshay on 06-06-2016.
 */
public class GetProductDescDbDTO {
    private String adId;

    public GetProductDescDbDTO(String adId) {
        this.adId = adId;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }
}
