package com.nearbypets.data.downloaddto;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by shrinivas on 04-06-2016.
 */
public class CategoryListDBDTO {
    //private ArrayList<SettingsDTO> settings;
    private ArrayList<CategoryDataDbDTO> data;

    public CategoryListDBDTO() {
    }

    public ArrayList<CategoryDataDbDTO> getData() {
        return data;
    }

    public void setData(ArrayList<CategoryDataDbDTO> data) {
        this.data = data;
    }

    public static ArrayList<CategoryListDBDTO> deserializeToArray(String serializedString) {
        Gson gson = new Gson();
        CategoryListDBDTO[] deserializeObject = gson.fromJson(serializedString, CategoryListDBDTO[].class);
        ArrayList<CategoryListDBDTO> objectList = (ArrayList<CategoryListDBDTO>) Arrays.asList(deserializeObject);
        return objectList;
    }

    @Override
    public String toString() {
        return "CategoryListDBDTO{" +
                " data=" + data +
                '}';
    }
}
