package com.nearbypets.data;

/**
 * Created by akshay on 04-06-2016.
 */
public class SettingsDBDTO extends BaseDTO {

    private String configKey;
    private String configValue;

    public SettingsDBDTO(String configKey, String configValue) {
        this.configKey = configKey;
        this.configValue = configValue;
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
}
