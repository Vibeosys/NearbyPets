package com.nearbypets.data;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 03-06-2016.
 */
public class ProductDbDTO extends BaseDTO {
    private String name;
    private String image;
    private String description;
    private double distance;
    private double price;
   /* private boolean mFavouriteFlag;
    private String mDate;*/

    public ProductDbDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public static List<ProductDbDTO> deserialize(List<String> serializedStringList) {
        Gson gson = new Gson();
        ArrayList<ProductDbDTO> objectList = new ArrayList<>();

        for (String serializedString : serializedStringList) {
            ProductDbDTO deserializeObject = gson.fromJson(serializedString, ProductDbDTO.class);
            objectList.add(deserializeObject);
        }
        return objectList;
    }

    @Override
    public String toString() {
        return "ProductDbDTO{" +
                "name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", description='" + description + '\'' +
                ", distance=" + distance +
                ", price=" + price +
                '}';
    }
}
