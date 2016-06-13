package com.nearbypets.data;

/**
 * Created by shrinivas on 13-06-2016.
 */
public class HiddenAdSearchDbDTO extends ProductListDbDTO {
    private String  search;
    public HiddenAdSearchDbDTO(double latitude, double longitude, int sortOption, String sortChoice, int pageNumber) {
        super(latitude, longitude, sortOption, sortChoice, pageNumber);
        this.search=search;
    }
}
