package com.nearbypets.data.downloaddto;

/**
 * Created by akshay on 07-06-2016.
 */
public class ProductDescDataDTO extends DataDbDTO {
    private ProductDescDbDTO data;

    public ProductDescDataDTO() {
    }

    public ProductDescDbDTO getData() {
        return data;
    }

    public void setData(ProductDescDbDTO data) {
        this.data = data;
    }
}
