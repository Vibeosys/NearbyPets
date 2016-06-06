package com.nearbypets.data;

import java.util.ArrayList;

/**
 * Created by shrinivas on 06-06-2016.
 */
public class PostMyAdDBDTO {
    private int categoryId;
    private String title;
    private String description;
    private String address;
    private String displayAddress;
    private String latitude;
    private String longitude;
    private double price;
    private int typeId;
    private String userId;
    private ArrayList<ImagesDbDTO> images;


    public PostMyAdDBDTO() {
    }

    public PostMyAdDBDTO(int categoryId, String title, String description, String address,
                         String displayAddress, String latitude, String longitude, double price, int typeId, String userId, ArrayList<ImagesDbDTO> images) {
        this.categoryId = categoryId;
        this.title = title;
        this.description = description;
        this.address = address;
        this.displayAddress = displayAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.price = price;
        this.typeId = typeId;
        this.userId = userId;
        this.images = images;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDisplayAddress() {
        return displayAddress;
    }

    public void setDisplayAddress(String displayAddress) {
        this.displayAddress = displayAddress;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<ImagesDbDTO> getImages() {
        return images;
    }

    public void setImages(ArrayList<ImagesDbDTO> images) {
        this.images = images;
    }


}
