package com.nearbypets.data;

/**
 * Created by shrinivas on 07-06-2016.
 */
public class SoldandDisableDbDTO extends BaseDTO {
        private String adId;
        private String  status;

    public SoldandDisableDbDTO(String adId, String status) {
        this.adId = adId;
        this.status = status;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "SoldandDisableDbDTO{" +
                "adId='" + adId + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
