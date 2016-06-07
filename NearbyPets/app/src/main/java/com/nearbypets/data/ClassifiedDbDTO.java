package com.nearbypets.data;

/**
 * Created by akshay on 07-06-2016.
 */
public class ClassifiedDbDTO extends ProductListDbDTO {

    private int categoryId;

    public ClassifiedDbDTO(double latitude, double longitude, int sortOption,
                           String sortChoice, int pageNumber, int categoryId) {
        super(latitude, longitude, sortOption, sortChoice, pageNumber);
        this.categoryId = categoryId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
