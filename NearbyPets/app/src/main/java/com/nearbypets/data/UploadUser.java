package com.nearbypets.data;

/**
 * Created by mahesh on 10/23/2015.
 */
public class UploadUser extends BaseDTO {
    protected int userId;
    protected String emailId;
    protected String userName;
    protected int rollId;

    public UploadUser() {

    }

    public UploadUser(int userId, String emailId, String userName, int rollId) {
        this.userId = userId;
        this.emailId = emailId;
        this.userName = userName;
        this.rollId = rollId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getRollId() {
        return rollId;
    }

    public void setRollId(int rollId) {
        this.rollId = rollId;
    }
}
