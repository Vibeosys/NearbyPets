package com.nearbypets.data;

/**
 * Created by akshay on 28-05-2016.
 */
public class CategoryDTO {

    private String mCategoryName;
    private int mProductCount;
    private String mCategoryImage;

    public CategoryDTO(String categoryName, int productCount, String categoryImage) {
        this.mCategoryName = categoryName;
        this.mProductCount = productCount;
        this.mCategoryImage = categoryImage;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public void setCategoryName(String mCategoryName) {
        this.mCategoryName = mCategoryName;
    }

    public int getProductCount() {
        return mProductCount;
    }

    public void setProductCount(int mProductCount) {
        this.mProductCount = mProductCount;
    }

    public String getCategoryImage() {
        return mCategoryImage;
    }

    public void setCategoryImage(String mCategoryImage) {
        this.mCategoryImage = mCategoryImage;
    }
}
