package com.nearbypets.data;

/**
 * Created by shrinivas on 04-06-2016.
 */
public class BirdCategoryDataDTO {
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
}
