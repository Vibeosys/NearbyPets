package com.nearbypets.data;

import java.sql.Date;

/**
 * Created by shrinivas on 02-06-2016.
 */
public class UserRegisterDTO {
    private String mUserId;
    private String mUserEmailId;
    private String mPassword;
    private String mFirstName;
    private String mLastName;
    private String mPhone;
    private int mLoginSource;
    private String mFbApiToken;
    private String mPhotoUrl;
    private Date mRegisterDate;
    private int mRollId;
    private boolean mActiveStatus;

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getUserEmailId() {
        return mUserEmailId;
    }

    public void setUserEmailId(String mUserEmailId) {
        this.mUserEmailId = mUserEmailId;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public int getLoginSource() {
        return mLoginSource;
    }

    public void setLoginSource(int mLoginSource) {
        this.mLoginSource = mLoginSource;
    }

    public String getFbApiToken() {
        return mFbApiToken;
    }

    public void setFbApiToken(String mFbApiToken) {
        this.mFbApiToken = mFbApiToken;
    }

    public String getPhotoUrl() {
        return mPhotoUrl;
    }

    public void setPhotoUrl(String mPhotoUrl) {
        this.mPhotoUrl = mPhotoUrl;
    }

    public Date getmRegisterDate() {
        return mRegisterDate;
    }

    public void setmRegisterDate(Date mRegisterDate) {
        this.mRegisterDate = mRegisterDate;
    }

    public int getmRollId() {
        return mRollId;
    }

    public void setmRollId(int mRollId) {
        this.mRollId = mRollId;
    }

    public boolean ismActiveStatus() {
        return mActiveStatus;
    }

    public void setmActiveStatus(boolean mActiveStatus) {
        this.mActiveStatus = mActiveStatus;
    }
}
