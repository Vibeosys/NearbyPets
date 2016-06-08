package com.nearbypets.data.downloaddto;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.nearbypets.data.BaseDTO;

/**
 * Created by akshay on 04-06-2016.
 */
public class UserDbDTO extends BaseDTO {

    private String userid;
    private String fname;
    private String lname;
    private String email;
    private String phone;
    private int roleid;
    private String pwd;
    private String token;

    public UserDbDTO() {
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getRoleid() {
        return roleid;
    }

    public void setRoleid(int roleid) {
        this.roleid = roleid;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static UserDbDTO deserializeJson(String serializedString) {
        Gson gson = new Gson();
        UserDbDTO userDbDTO = null;
        try {
            userDbDTO = gson.fromJson(serializedString, UserDbDTO.class);
        } catch (JsonParseException e) {
            Log.d("Download User Db DTO", "Exception in deserialization" + e.toString());
        }
        return userDbDTO;
    }
}
