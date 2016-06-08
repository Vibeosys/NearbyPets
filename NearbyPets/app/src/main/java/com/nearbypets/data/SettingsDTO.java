package com.nearbypets.data;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 03-06-2016.
 */
public class SettingsDTO extends BaseDTO {

    private String configKey;
    private String configValue;


    public SettingsDTO() {
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
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
                "configKey='" + configKey + '\'' +
                ", value='" + configValue + '\'' +
                '}';
    }
}
