package com.nearbypets.data.downloaddto;

import com.nearbypets.data.BaseDTO;

/**
 * Created by akshay on 04-06-2016.
 */
public class ProductImagesDbDTO extends BaseDTO {

    private String url;

    public ProductImagesDbDTO() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
