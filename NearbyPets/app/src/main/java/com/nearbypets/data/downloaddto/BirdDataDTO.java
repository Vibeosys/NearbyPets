package com.nearbypets.data.downloaddto;

import com.nearbypets.data.BirdCategoryDataDTO;

import java.util.ArrayList;

/**
 * Created by akshay on 07-06-2016.
 */
public class BirdDataDTO extends DataDbDTO {
    private ArrayList<BirdCategoryDataDTO> data;

    public BirdDataDTO() {
    }

    public ArrayList<BirdCategoryDataDTO> getData() {
        return data;
    }

    public void setData(ArrayList<BirdCategoryDataDTO> data) {
        this.data = data;
    }
}
