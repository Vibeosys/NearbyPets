package com.nearbypets.data.downloaddto;

import com.nearbypets.data.BaseDTO;
import com.nearbypets.data.SettingsDTO;

import java.util.ArrayList;

/**
 * Created by shrinivas on 03-06-2016.
 */
public class DownloadRegisterDbDTO extends DownloadDataDbDTO {
    private UserDbDTO data;

    public DownloadRegisterDbDTO() {
    }

    public UserDbDTO getData() {
        return data;
    }

    public void setData(UserDbDTO data) {
        this.data = data;
    }
}
