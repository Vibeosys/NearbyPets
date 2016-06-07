package com.nearbypets.data;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

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
    private Date mDt;

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

    public Date getPostedDt() {
        if (this.mDate == null || this.mDate.isEmpty())
            return this.mDt;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        //formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        java.util.Date value = null;
        try {
            value = formatter.parse(this.mDate);
            mDt = new Date(value.getTime());
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTime(value);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return mDt;
    }
}
