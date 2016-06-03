package com.nearbypets.data;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shrinivas on 03-06-2016.
 */
public class RegisterDBDTO  extends  BaseDTO{

    private String fname;
    private String lname;
    private String email;
    private String phone;
    private String password;

    public RegisterDBDTO(String fname, String lname, String email, String phone, String password) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.phone = phone;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static List<RegisterDBDTO> deserializeOrders(List<String> serializedStringList) {
        Gson gson = new Gson();
        ArrayList<RegisterDBDTO> objectList = new ArrayList<>();

        for (String serializedString : serializedStringList) {
            RegisterDBDTO deserializeObject = gson.fromJson(serializedString, RegisterDBDTO.class);
            objectList.add(deserializeObject);
        }
        return objectList;
    }
}
