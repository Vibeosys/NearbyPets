package com.nearbypets.converter;

import com.nearbypets.data.CategoryDTO;
import com.nearbypets.data.CategoryDbDTO;

import java.util.ArrayList;

/**
 * Created by shrinivas on 04-06-2016.
 */
public class CateDbToCateDTO {
    ArrayList<CategoryDbDTO> data;

    public CateDbToCateDTO(ArrayList<CategoryDbDTO> data) {
        this.data = data;
    }
    public ArrayList<CategoryDTO> getCategoryDTOs() {
        ArrayList<CategoryDTO> categoryDTOs = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            CategoryDbDTO category = data.get(i);
            categoryDTOs.add(new CategoryDTO(category.getImage(),category.getProducts(),category.getTitle()));

        }
        return categoryDTOs;
    }
}
