package com.nearbypets.data.downloaddto;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

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
    public static ForgotDBDTO deserializeJson(String serializedString) {
        Gson gson = new Gson();
        ForgotDBDTO forgotDBDTO = null;
        try {
            forgotDBDTO = gson.fromJson(serializedString, ForgotDBDTO.class);
        } catch (JsonParseException e) {
            Log.d("Forgot password Db DTO", "Exception in deserialization" + e.toString());
        }
        return forgotDBDTO;
    }
}
