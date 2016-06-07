package com.nearbypets.data.downloaddto;

import com.nearbypets.data.CategoryDbDTO;
import com.nearbypets.data.ProductDbDTO;
import com.nearbypets.data.SettingsDTO;

import java.util.ArrayList;

/**
 * Created by shrinivas on 04-06-2016.
 */
public class CategoryListDBDTO {
    private ArrayList<SettingsDTO> settings;
    private ArrayList<CategoryDataDbDTO> data;

    public CategoryListDBDTO() {
    }

    public ArrayList<SettingsDTO> getSettings() {
        return settings;
    }

    public void setSettings(ArrayList<SettingsDTO> settings) {
        this.settings = settings;
    }

    public ArrayList<CategoryDataDbDTO> getData() {
        return data;
    }

    public void setData(ArrayList<CategoryDataDbDTO> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CategoryListDBDTO{" +
                "settings=" + settings +
                ", data=" + data +
                '}';
    }
}
