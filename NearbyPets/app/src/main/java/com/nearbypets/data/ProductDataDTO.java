package com.nearbypets.data;

/**
 * Created by akshay on 30-05-2016.
 */
public class ProductDataDTO {

    private String mAdId;
    private String mProductName;
    private String mProductImage;
    private String mProductDesc;
    private double mDistance;
    private double mPrice;
    private boolean mFavouriteFlag;
    private String mDate;

    public ProductDataDTO(String adId, String productName, String productImage, String productDesc,
                          double distance, double price, boolean favouriteFlag, String date) {
        this.mAdId = adId;
        this.mProductName = productName;
        this.mProductImage = productImage;
        this.mProductDesc = productDesc;
        this.mDistance = distance;
        this.mPrice = price;
        this.mFavouriteFlag = favouriteFlag;
        this.mDate = date;
    }

    public ProductDataDTO(String mProductName, String mProductImage, String mProductDesc,
                          double mDistance, double mPrice, boolean mFavouriteFlag, String mDate) {
        this.mProductName = mProductName;
        this.mProductImage = mProductImage;
        this.mProductDesc = mProductDesc;
        this.mDistance = mDistance;
        this.mPrice = mPrice;
        this.mFavouriteFlag = mFavouriteFlag;
        this.mDate = mDate;
    }

    public String getAdId() {
        return mAdId;
    }

    public void setAdId(String mAdId) {
        this.mAdId = mAdId;
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

    public double getDistance() {
        return mDistance;
    }

    public void setDistance(double mDistance) {
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
