package com.nearbypets.data;

import com.google.gson.Gson;

import java.util.ArrayList;
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

    @Override
    public String toString() {
        return "CategoryDbDTO{" +
                "image='" + image + '\'' +
                ", products=" + products +
                ", title='" + title + '\'' +
                '}';
    }
}
