package com.nearbypets.data;

import java.sql.Date;

/**
 * Created by shrinivas on 02-06-2016.
 */
public class PostedAdDTO {
    private String mAdId;
    private String mUserId;
    private String mTitle;
    private int mCategoryId;
    private String mDescription;
    private String mAddress;
    private String mDisplayAddress;
    private double mLatitude;
    private double mLongitude;
    private  double mPrice;
    private int mAdType;
    private  int mStatus;
    private  int mViews;
    private int mPostedActive;
    private Date mPostedDate;

    public String getAdId() {
        return mAdId;
    }

    public void setAdId(String mAdId) {
        this.mAdId = mAdId;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public int getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(int mCategoryId) {
        this.mCategoryId = mCategoryId;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getDisplayAddress() {
        return mDisplayAddress;
    }

    public void setDisplayAddress(String mDisplayAddress) {
        this.mDisplayAddress = mDisplayAddress;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public double getPrice() {
        return mPrice;
    }

    public void setPrice(double mPrice) {
        this.mPrice = mPrice;
    }

    public int getAdType() {
        return mAdType;
    }

    public void setAdType(int mAdType) {
        this.mAdType = mAdType;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int mStatus) {
        this.mStatus = mStatus;
    }

    public int getViews() {
        return mViews;
    }

    public void setViews(int mViews) {
        this.mViews = mViews;
    }

    public int getPostedActive() {
        return mPostedActive;
    }

    public void setPostedActive(int mPostedActive) {
        this.mPostedActive = mPostedActive;
    }

    public Date getPostedDate() {
        return mPostedDate;
    }

    public void setPostedDate(Date mPostedDate) {
        this.mPostedDate = mPostedDate;
    }
}
