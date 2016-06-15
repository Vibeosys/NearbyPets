package com.nearbypets.data.downloaddto;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.nearbypets.data.BaseDTO;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by akshay on 04-06-2016.
 */
public class ProductDescDbDTO extends BaseDTO {

    private String adId;
    private String adTitle;
    private String description;
    private String adAddress;
    private String displayAddress;
    private double price;
    private String name;
    private String phone;
    private String email;
    private String postedDate;
    private int adViews;
    private double latitude;
    private double longitude;
    private ArrayList<String> images;
    private Date postedDt;
    private int isAddress;

    public ProductDescDbDTO() {
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getAdTitle() {
        return adTitle;
    }

    public void setAdTitle(String adTitle) {
        this.adTitle = adTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdAddress() {
        return adAddress;
    }

    public void setAdAddress(String adAddress) {
        this.adAddress = adAddress;
    }

    public String getDisplayAddress() {
        return displayAddress;
    }

    public void setDisplayAddress(String displayAddress) {
        this.displayAddress = displayAddress;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }

    public int getAdViews() {
        return adViews;
    }

    public void setAdViews(int adViews) {
        this.adViews = adViews;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public int getIsAddress() {
        return isAddress;
    }

    public void setIsAddress(int isAddress) {
        this.isAddress = isAddress;
    }

    public Date getPostedDt() {
        if (this.postedDate == null || this.postedDate.isEmpty())
            return this.postedDt;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        //formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        java.util.Date value = null;
        try {
            value = formatter.parse(this.postedDate);
            postedDt = new Date(value.getTime());
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTime(value);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return postedDt;
    }

    public void setPostedDt(Date postedDt) {
        this.postedDt = postedDt;
    }

    public static ProductDescDbDTO deserializeJson(String serializedString) {
        Gson gson = new Gson();
        ProductDescDbDTO productDescDbDTO = null;
        try {
            productDescDbDTO = gson.fromJson(serializedString, ProductDescDbDTO.class);
        } catch (JsonParseException e) {
            Log.d("Download User Db DTO", "Exception in deserialization" + e.toString());
        }
        return productDescDbDTO;
    }
}
