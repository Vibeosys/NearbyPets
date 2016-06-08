package com.nearbypets.data.downloaddto;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.nearbypets.data.BaseDTO;
import com.nearbypets.data.SettingsDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 07-06-2016.
 */
public class DownloadDataDbDTO extends BaseDTO {

    private ErrorDbDTO error;
    private List<SettingsDTO> settings;
    private String data;

    public DownloadDataDbDTO() {
    }

    public ErrorDbDTO getError() {
        return error;
    }

    public void setError(ErrorDbDTO error) {
        this.error = error;
    }

    public List<SettingsDTO> getSettings() {
        return settings;
    }

    public void setSettings(List<SettingsDTO> settings) {
        this.settings = settings;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public static DownloadDataDbDTO deserializeJson(String serializedString) {
        Gson gson = new Gson();
        DownloadDataDbDTO downloadDataDbDTO = null;
        try {
            downloadDataDbDTO = gson.fromJson(serializedString, DownloadDataDbDTO.class);
        } catch (JsonParseException e) {
            Log.d("Download Data Db DTO", "Exception in deserialization" + e.toString());
        }
        return downloadDataDbDTO;
    }
}
