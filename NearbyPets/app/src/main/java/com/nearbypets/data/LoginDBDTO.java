package com.nearbypets.data;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 03-06-2016.
 */
public class LoginDBDTO extends BaseDTO {

    private String email;
    private String password;

    public LoginDBDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static List<LoginDBDTO> deserializeOrders(List<String> serializedStringList) {
        Gson gson = new Gson();
        ArrayList<LoginDBDTO> objectList = new ArrayList<>();

        for (String serializedString : serializedStringList) {
            LoginDBDTO deserializeObject = gson.fromJson(serializedString, LoginDBDTO.class);
            objectList.add(deserializeObject);
        }
        return objectList;
    }
}
