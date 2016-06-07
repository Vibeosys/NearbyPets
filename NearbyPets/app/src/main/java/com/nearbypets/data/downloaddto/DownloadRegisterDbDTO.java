package com.nearbypets.data.downloaddto;

import com.nearbypets.data.BaseDTO;
import com.nearbypets.data.SettingsDTO;

import java.util.ArrayList;

/**
 * Created by shrinivas on 03-06-2016.
 */
public class DownloadRegisterDbDTO extends BaseDTO {
    private ArrayList<SettingsDTO> settings;
    private ArrayList<NotificationDataDTO> data;

    public DownloadRegisterDbDTO() {
    }

    public ArrayList<SettingsDTO> getSettings() {
        return settings;
    }

    public void setSettings(ArrayList<SettingsDTO> settings) {
        this.settings = settings;
    }

    public ArrayList<NotificationDataDTO> getData() {
        return data;
    }

    public void setData(ArrayList<NotificationDataDTO> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DownloadRegisterDbDTO{" +
                "settings=" + settings +
                ", data=" + data +
                '}';
    }

}
