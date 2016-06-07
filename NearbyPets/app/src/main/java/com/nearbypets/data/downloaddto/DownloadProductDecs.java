package com.nearbypets.data.downloaddto;

import com.nearbypets.data.BaseDTO;
import com.nearbypets.data.ProductDbDTO;
import com.nearbypets.data.SettingsDTO;

import java.util.ArrayList;

/**
 * Created by akshay on 04-06-2016.
 */
public class DownloadProductDecs extends BaseDTO {

    private ArrayList<SettingsDTO> settings;
    private ArrayList<ProductDescDataDTO> data;

    public DownloadProductDecs() {
    }

    public ArrayList<SettingsDTO> getSettings() {
        return settings;
    }

    public void setSettings(ArrayList<SettingsDTO> settings) {
        this.settings = settings;
    }

    public ArrayList<ProductDescDataDTO> getData() {
        return data;
    }

    public void setData(ArrayList<ProductDescDataDTO> data) {
        this.data = data;
    }
}
