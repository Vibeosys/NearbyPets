package com.nearbypets.data.downloaddto;

import com.nearbypets.data.BaseDTO;

/**
 * Created by akshay on 04-06-2016.
 */
public class ProductPetDbDTO extends BaseDTO {

    private String title;
    private double price;
    private String description;

    public ProductPetDbDTO() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ProductPetDbDTO{" +
                "title='" + title + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                '}';
    }
}
