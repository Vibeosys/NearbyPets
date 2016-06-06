package com.nearbypets.data.downloaddto;

import com.nearbypets.data.BaseDTO;

/**
 * Created by akshay on 06-06-2016.
 */
public class SaveAnAdDbDTO extends BaseDTO {

    private String adId;
    private String userId;

    public SaveAnAdDbDTO(String adId, String userId) {
        this.adId = adId;
        this.userId = userId;
    }

    public SaveAnAdDbDTO() {
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
