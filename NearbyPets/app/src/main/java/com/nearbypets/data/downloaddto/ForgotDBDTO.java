package com.nearbypets.data.downloaddto;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shrinivas on 03-06-2016.
 */
public class ForgotDBDTO {
    private String email;

    public ForgotDBDTO(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static List<ForgotDBDTO> deserialize(List<String> serializedStringList) {
        Gson gson = new Gson();
        ArrayList<ForgotDBDTO> objectList = new ArrayList<>();

        for (String serializedString : serializedStringList) {
            ForgotDBDTO deserializeObject = gson.fromJson(serializedString, ForgotDBDTO.class);
            objectList.add(deserializeObject);
        }
        return objectList;
    }
}
