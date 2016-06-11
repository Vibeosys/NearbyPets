package com.nearbypets.data;

/**
 * Created by shrinivas on 11-06-2016.
 */
public class SavedAdListDbDTO extends ProductListDbDTO {

    private String userId;
    private String search;

    public SavedAdListDbDTO(double latitude, double longitude, int sortOption, String sortChoice,
                         int pageNumber, String userId,String search) {
        super(latitude, longitude, sortOption, sortChoice, pageNumber);
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
