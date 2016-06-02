package com.nearbypets.data;

/**
 * Created by shrinivas on 02-06-2016.
 */
public class AdTypesDTO {
    private int mAdTypeId;
    private String mAdTypeDesc;
    private boolean mAdTypesActive;

    public int getAdTypeId() {
        return mAdTypeId;
    }

    public void setAdTypeId(int mAdTypeId) {
        this.mAdTypeId = mAdTypeId;
    }

    public String getAdTypeDesc() {
        return mAdTypeDesc;
    }

    public void setAdTypeDesc(String mAdTypeDesc) {
        this.mAdTypeDesc = mAdTypeDesc;
    }

    public boolean isAdTypesActive() {
        return mAdTypesActive;
    }

    public void setAdTypesActive(boolean mAdTypesActive) {
        this.mAdTypesActive = mAdTypesActive;
    }
}
