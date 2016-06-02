package com.nearbypets.data;

/**
 * Created by shrinivas on 02-06-2016.
 */
public class StatusDTO {
    private int mStatusId;
    private String mStatusDesc;
    private boolean mStatusActive;

    public int getStatusId() {
        return mStatusId;
    }

    public void setStatusId(int mStatusId) {
        this.mStatusId = mStatusId;
    }

    public String getStatusDesc() {
        return mStatusDesc;
    }

    public void setStatusDesc(String mStatusDesc) {
        this.mStatusDesc = mStatusDesc;
    }

    public boolean isStatusActive() {
        return mStatusActive;
    }

    public void setStatusActive(boolean mStatusActive) {
        this.mStatusActive = mStatusActive;
    }
}
