package com.nearbypets.data;

/**
 * Created by akshay on 07-06-2016.
 */
public class ClassifiedDbDTO extends ProductListDbDTO {

    private int categoryId;

    private String  search;


    public ClassifiedDbDTO(double latitude, double longitude, int sortOption,
                           String sortChoice, int pageNumber, int categoryId, String  search) {
        super(latitude, longitude, sortOption, sortChoice, pageNumber);
        this.categoryId = categoryId;
        this.search=search;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

}
