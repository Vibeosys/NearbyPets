package com.nearbypets.data;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 03-06-2016.
 */
public class SettingsDTO extends BaseDTO {

    private String key;
    private String value;

    public SettingsDTO() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static List<SettingsDTO> deserialize(List<String> serializedStringList) {
        Gson gson = new Gson();
        ArrayList<SettingsDTO> objectList = new ArrayList<>();

        for (String serializedString : serializedStringList) {
            SettingsDTO deserializeObject = gson.fromJson(serializedString, SettingsDTO.class);
            objectList.add(deserializeObject);
        }
        return objectList;
    }

    @Override
    public String toString() {
        return "SettingsDTO{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
