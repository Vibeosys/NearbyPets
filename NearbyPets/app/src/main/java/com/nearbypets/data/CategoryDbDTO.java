package com.nearbypets.data;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by shrinivas on 04-06-2016.
 */
public class CategoryDbDTO extends BaseDTO {
    private String image;
    private int products;
    private String title;
    private int categoryId;

    public CategoryDbDTO() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getProducts() {
        return products;
    }

    public void setProducts(int products) {
        this.products = products;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public static List<CategoryDbDTO> deserialize(List<String> serializedStringList) {
        Gson gson = new Gson();
        ArrayList<CategoryDbDTO> objectList = new ArrayList<>();

        for (String serializedString : serializedStringList) {
            CategoryDbDTO deserializeObject = gson.fromJson(serializedString, CategoryDbDTO.class);
            objectList.add(deserializeObject);
        }
        return objectList;
    }

    public static ArrayList<CategoryDbDTO> deserializeToArray(String serializedString) {
        Gson gson = new Gson();
        ArrayList<CategoryDbDTO> categoryDbDTOs = null;
        try {
            CategoryDbDTO[] deserializeObject = gson.fromJson(serializedString, CategoryDbDTO[].class);
            for (CategoryDbDTO categoryDbDTO : deserializeObject) {
                categoryDbDTOs = new ArrayList<>();
                categoryDbDTOs.add(categoryDbDTO);
            }
        } catch (JsonSyntaxException e) {
            Log.e("Deserilization", "Category  DB DTO error" + e.toString());
        }


        return categoryDbDTOs;
    }


    @Override
    public String toString() {
        return "CategoryDbDTO{" +
                "image='" + image + '\'' +
                ", products=" + products +
                ", title='" + title + '\'' +
                '}';
    }
}
