package com.nearbypets.data;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by shrinivas on 04-06-2016.
 */
public class BirdCategoryDataDTO extends BaseDTO {
    private String categoryTitle;
    private int categoryId;

    public BirdCategoryDataDTO() {
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

    public static ArrayList<BirdCategoryDataDTO> deserializeToArray(String serializedString) {
        Gson gson = new Gson();
        BirdCategoryDataDTO[] deserializeObject = gson.fromJson(serializedString, BirdCategoryDataDTO[].class);
        ArrayList<BirdCategoryDataDTO> objectList = (ArrayList<BirdCategoryDataDTO>) Arrays.asList(deserializeObject);
        return objectList;
    }
}
