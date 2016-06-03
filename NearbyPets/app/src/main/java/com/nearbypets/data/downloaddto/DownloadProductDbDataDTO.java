package com.nearbypets.data.downloaddto;

import com.google.gson.Gson;
import com.nearbypets.data.BaseDTO;
import com.nearbypets.data.ProductDbDTO;
import com.nearbypets.data.SettingsDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 03-06-2016.
 */
public class DownloadProductDbDataDTO extends BaseDTO {
    private ArrayList<SettingsDTO> settings;
    private ArrayList<ProductDbDTO> data;

    public DownloadProductDbDataDTO() {
    }

    public ArrayList<SettingsDTO> getSettings() {
        return settings;
    }

    public void setSettings(ArrayList<SettingsDTO> settings) {
        this.settings = settings;
    }

    public ArrayList<ProductDbDTO> getData() {
        return data;
    }

    public void setData(ArrayList<ProductDbDTO> data) {
        this.data = data;
    }

    public static List<DownloadProductDbDataDTO> deserialize(List<String> serializedStringList) {
        Gson gson = new Gson();
        ArrayList<DownloadProductDbDataDTO> objectList = new ArrayList<>();

        for (String serializedString : serializedStringList) {
            DownloadProductDbDataDTO deserializeObject = gson.fromJson(serializedString, DownloadProductDbDataDTO.class);
            objectList.add(deserializeObject);
        }
        return objectList;
    }
}
