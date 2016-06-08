package com.nearbypets.data;

/**
 * Created by mahesh on 10/23/2015.
 */
public class UploadUser extends BaseDTO {
    protected String userId;
    protected String email;
    protected String userName;
    protected String accessToken;

    public UploadUser() {

    }

    public UploadUser(String userId, String emailId, String userName, String accessToken) {
        this.userId = userId;
        this.email = emailId;
        this.userName = userName;
        this.accessToken = accessToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmailId() {
        return email;
    }

    public void setEmailId(String emailId) {
        this.email = emailId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
