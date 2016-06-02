package com.nearbypets.data;

/**
 * Created by shrinivas on 02-06-2016.
 */
public class ConfigSettingsDTO {
    private String mConfigKey;
    private String mConfigDesc;
    private String mConfigValue;
    private boolean mConfigActive;

    public String getConfigKey() {
        return mConfigKey;
    }

    public void setConfigKey(String mConfigKey) {
        this.mConfigKey = mConfigKey;
    }

    public String getConfigDesc() {
        return mConfigDesc;
    }

    public void setConfigDesc(String mConfigDesc) {
        this.mConfigDesc = mConfigDesc;
    }

    public String getConfigValue() {
        return mConfigValue;
    }

    public void setConfigValue(String mConfigValue) {
        this.mConfigValue = mConfigValue;
    }

    public boolean isConfigActive() {
        return mConfigActive;
    }

    public void setConfigActive(boolean mConfigActive) {
        this.mConfigActive = mConfigActive;
    }
}
