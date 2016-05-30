package com.nearbypets.data;

/**
 * Created by akshay on 30-05-2016.
 */
public class ProductDataDTO {

    private String mProductName;
    private String mProductImage;
    private String mProductDesc;
    private String mDistance;
    private double mPrice;
    private boolean mFavouriteFlag;
    private String mDate;

    public ProductDataDTO(String productName, String productImage, String productDesc,
                          String distance, double price, boolean favouriteFlag, String date) {
        this.mProductName = productName;
        this.mProductImage = productImage;
        this.mProductDesc = productDesc;
        this.mDistance = distance;
        this.mPrice = price;
        this.mFavouriteFlag = favouriteFlag;
        this.mDate = date;
    }

    public String getProductName() {
        return mProductName;
    }

    public void setProductName(String mProductName) {
        this.mProductName = mProductName;
    }

    public String getProductImage() {
        return mProductImage;
    }

    public void setProductImage(String mProductImage) {
        this.mProductImage = mProductImage;
    }

    public String getProductDesc() {
        return mProductDesc;
    }

    public void setProductDesc(String mProductDesc) {
        this.mProductDesc = mProductDesc;
    }

    public String getDistance() {
        return mDistance;
    }

    public void setDistance(String mDistance) {
        this.mDistance = mDistance;
    }

    public double getPrice() {
        return mPrice;
    }

    public void setPrice(double mPrice) {
        this.mPrice = mPrice;
    }

    public boolean isFavouriteFlag() {
        return mFavouriteFlag;
    }

    public void setFavouriteFlag(boolean mFavouriteFlag) {
        this.mFavouriteFlag = mFavouriteFlag;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }
}
