package com.nearbypets.data.downloaddto;

import com.nearbypets.data.BaseDTO;
import com.nearbypets.data.SettingsDTO;

import java.util.ArrayList;

/**
 * Created by akshay on 07-06-2016.
 */
public class DownloadDataDbDTO extends BaseDTO {

    private ErrorDbDTO error;
    private ArrayList<SettingsDTO> settings;

    public DownloadDataDbDTO() {
    }

    public ErrorDbDTO getError() {
        return error;
    }

    public void setError(ErrorDbDTO error) {
        this.error = error;
    }

    public ArrayList<SettingsDTO> getSettings() {
        return settings;
    }

    public void setSettings(ArrayList<SettingsDTO> settings) {
        this.settings = settings;
    }
}
