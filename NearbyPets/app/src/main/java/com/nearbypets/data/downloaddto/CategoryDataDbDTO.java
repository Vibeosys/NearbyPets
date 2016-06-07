package com.nearbypets.data.downloaddto;

import com.nearbypets.data.CategoryDbDTO;

import java.util.ArrayList;

/**
 * Created by akshay on 07-06-2016.
 */
public class CategoryDataDbDTO extends DataDbDTO {

    private ArrayList<CategoryDbDTO> data;

    public CategoryDataDbDTO() {
    }

    public ArrayList<CategoryDbDTO> getData() {
        return data;
    }

    public void setData(ArrayList<CategoryDbDTO> data) {
        this.data = data;
    }
}
