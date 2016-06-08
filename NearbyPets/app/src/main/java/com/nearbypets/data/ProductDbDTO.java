package com.nearbypets.data;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by akshay on 03-06-2016.
 */
public class ProductDbDTO extends BaseDTO {
    private String adid;
    private String name;
    private String image;
    private String description;
    private double distance;
    private String date;
    private double price;
   /* private boolean mFavouriteFlag;
    private String mDate;*/

    public ProductDbDTO() {
    }

    public String getAdid() {
        return adid;
    }

    public void setAdid(String adid) {
        this.adid = adid;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public static ArrayList<ProductDbDTO> deserializeToArray(String serializedString) {
        Gson gson = new Gson();
        ArrayList<ProductDbDTO> productDbList = null;
        try {
            ProductDbDTO[] deserializeObject = gson.fromJson(serializedString, ProductDbDTO[].class);
            productDbList = new ArrayList<>();
            for (ProductDbDTO productDbRow : deserializeObject                    ) {
                productDbList.add(productDbRow);
            }
        } catch (JsonSyntaxException e) {
            Log.e("Deserialization", "## error in Product Db DTO" + e.toString());
        }
        return productDbList;
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
