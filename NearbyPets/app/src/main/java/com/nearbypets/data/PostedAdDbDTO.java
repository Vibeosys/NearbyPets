package com.nearbypets.data;

/**
 * Created by akshay on 07-06-2016.
 */
public class PostedAdDbDTO extends ProductListDbDTO {

    private String userId;

    public PostedAdDbDTO(double latitude, double longitude, int sortOption, String sortChoice,
                         int pageNumber, String userId) {
        super(latitude, longitude, sortOption, sortChoice, pageNumber);
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
