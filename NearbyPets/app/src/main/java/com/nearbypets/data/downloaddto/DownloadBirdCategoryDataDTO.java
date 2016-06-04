package com.nearbypets.data.downloaddto;

import com.nearbypets.data.BirdCategoryDataDTO;
import com.nearbypets.data.SettingsDTO;

import java.util.ArrayList;

/**
 * Created by shrinivas on 04-06-2016.
 */
public class DownloadBirdCategoryDataDTO {
    private ArrayList<SettingsDTO> settings;
    private ArrayList<BirdCategoryDataDTO> data;

    public DownloadBirdCategoryDataDTO() {
    }

    public ArrayList<SettingsDTO> getSettings() {
        return settings;
    }

    public void setSettings(ArrayList<SettingsDTO> settings) {
        this.settings = settings;
    }

    public ArrayList<BirdCategoryDataDTO> getData() {
        return data;
    }

    public void setData(ArrayList<BirdCategoryDataDTO> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DownloadBirdCategoryDataDTO{" +
                "settings=" + settings +
                ", data=" + data +
                '}';
    }
}
