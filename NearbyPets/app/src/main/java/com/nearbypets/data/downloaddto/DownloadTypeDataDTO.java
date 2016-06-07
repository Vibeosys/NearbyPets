package com.nearbypets.data.downloaddto;

import com.nearbypets.data.SettingsDTO;
import com.nearbypets.data.TypeDataDTO;

import java.util.ArrayList;

/**
 * Created by shrinivas on 04-06-2016.
 */
public class DownloadTypeDataDTO {
    private ArrayList<SettingsDTO> settings;
    private ArrayList<TypeDataDbDTO> data;

    public DownloadTypeDataDTO() {
    }

    public ArrayList<SettingsDTO> getSettings() {
        return settings;
    }

    public void setSettings(ArrayList<SettingsDTO> settings) {
        this.settings = settings;
    }

    public ArrayList<TypeDataDbDTO> getData() {
        return data;
    }

    public void setData(ArrayList<TypeDataDbDTO> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DownloadTypeDataDTO{" +
                "settings=" + settings +
                ", data=" + data +
                '}';
    }
}
