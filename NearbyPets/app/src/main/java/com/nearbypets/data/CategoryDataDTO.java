package com.nearbypets.data;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by shrinivas on 04-06-2016.
 */
public class CategoryDataDTO extends BaseDTO {
    private String categoryTitle;
    private int categoryId;

    public CategoryDataDTO() {
    }

    public CategoryDataDTO(int categoryId, String categoryTitle) {
        this.categoryId = categoryId;
        this.categoryTitle = categoryTitle;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public static ArrayList<CategoryDataDTO> deserializeToArray(String serializedString) {
        Gson gson = new Gson();
        ArrayList<CategoryDataDTO> categoryDataDTOs = null;
        try {
            CategoryDataDTO[] deserializeObject = gson.fromJson(serializedString, CategoryDataDTO[].class);
            categoryDataDTOs = new ArrayList<>();
            for (CategoryDataDTO categoryDataRow : deserializeObject) {
                categoryDataDTOs.add(categoryDataRow);
            }

        } catch (JsonSyntaxException e) {
            Log.e("Deserilization", "Category  data DTO error" + e.toString());
        }

        return categoryDataDTOs;
    }

}
