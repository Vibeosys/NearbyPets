package com.nearbypets.data;

/**
 * Created by shrinivas on 14-06-2016.
 */
public class UpdateRegisterDBDTO extends BaseDTO{
    private String userId;
    private String fname;
    private String lname;
    private String phone;
    private String email;
    private String pwd;

    public UpdateRegisterDBDTO(String userId, String fname, String lname, String phone, String email, String pwd) {
        this.userId = userId;
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
        this.email = email;
        this.pwd = pwd;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
