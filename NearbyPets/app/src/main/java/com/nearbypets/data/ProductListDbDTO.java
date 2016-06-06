package com.nearbypets.data;

/**
 * Created by akshay on 06-06-2016.
 */
public class ProductListDbDTO extends BaseDTO {

    private double latitude;
    private double longitude;
    private int sortOption;
    private String sortChoice;
    private int pageNumber;

    public ProductListDbDTO(double latitude, double longitude, int sortOption, String sortChoice, int pageNumber) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.sortOption = sortOption;
        this.sortChoice = sortChoice;
        this.pageNumber = pageNumber;
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

    public int getSortOption() {
        return sortOption;
    }

    public void setSortOption(int sortOption) {
        this.sortOption = sortOption;
    }

    public String getSortChoice() {
        return sortChoice;
    }

    public void setSortChoice(String sortChoice) {
        this.sortChoice = sortChoice;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
}
