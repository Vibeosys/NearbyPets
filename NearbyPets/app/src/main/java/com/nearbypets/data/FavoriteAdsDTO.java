package com.nearbypets.data;

import java.sql.Date;

/**
 * Created by shrinivas on 02-06-2016.
 */
public class FavoriteAdsDTO {
    private String mUserId;
    private String mAdId;
    private Date mFavouriteOnDate;
    private boolean mFavoriteActive;

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getAdId() {
        return mAdId;
    }

    public void setAdId(String mAdId) {
        this.mAdId = mAdId;
    }

    public Date getFavouriteOnDate() {
        return mFavouriteOnDate;
    }

    public void setFavouriteOnDate(Date mFavouriteOnDate) {
        this.mFavouriteOnDate = mFavouriteOnDate;
    }

    public boolean isFavoriteActive() {
        return mFavoriteActive;
    }

    public void setFavoriteActive(boolean mFavoriteActive) {
        this.mFavoriteActive = mFavoriteActive;
    }
}
