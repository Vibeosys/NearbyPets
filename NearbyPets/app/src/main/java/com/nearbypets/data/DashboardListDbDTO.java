package com.nearbypets.data;

/**
 * Created by akshay on 13-06-2016.
 */
public class DashboardListDbDTO extends ProductListDbDTO {

    private String search;

    public DashboardListDbDTO(double latitude, double longitude, int sortOption, String sortChoice,
                              int pageNumber, String search) {
        super(latitude, longitude, sortOption, sortChoice, pageNumber);
        this.search = search;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
