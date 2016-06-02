package com.nearbypets.data;

/**
 * Created by shrinivas on 02-06-2016.
 */
public class PostedAdImagesDTO {
    private String mImageId;
    private String mImageUrl;
    private String mAdId;
    private boolean mPostedImageActive;

    public String getImageId() {
        return mImageId;
    }

    public void setImageId(String mImageId) {
        this.mImageId = mImageId;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getAdId() {
        return mAdId;
    }

    public void setAdId(String mAdId) {
        this.mAdId = mAdId;
    }

    public boolean isPostedImageActive() {
        return mPostedImageActive;
    }

    public void setPostedImageActive(boolean mPostedImageActive) {
        this.mPostedImageActive = mPostedImageActive;
    }
}
