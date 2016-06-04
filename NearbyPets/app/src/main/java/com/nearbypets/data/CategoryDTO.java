package com.nearbypets.data;

import android.graphics.drawable.Drawable;

/**
 * Created by akshay on 28-05-2016.
 */
public class CategoryDTO {

    private String mCategoryName;
    private int mProductCount;
    private String mCategoryImage;
    private Drawable mDrawable;
    private boolean mActive;
    private int mCategoryId;

    public CategoryDTO(String categoryImage,int productCount,String categoryName) {
        this.mCategoryImage = categoryImage;
        this.mProductCount = productCount;
        this.mCategoryName = categoryName;
    }

    public CategoryDTO(String categoryName, int productCount, String categoryImage, Drawable drawable) {
        this.mCategoryName = categoryName;
        this.mProductCount = productCount;
        this.mCategoryImage = categoryImage;
        this.mDrawable = drawable;
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

    public Drawable getDrawable() {
        return mDrawable;
    }

    public void setDrawable(Drawable mDrawable) {
        this.mDrawable = mDrawable;
    }

    public boolean isActive() {
        return mActive;
    }

    public void setActive(boolean mActive) {
        this.mActive = mActive;
    }

    public int getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(int mCategoryId) {
        this.mCategoryId = mCategoryId;
    }
}
