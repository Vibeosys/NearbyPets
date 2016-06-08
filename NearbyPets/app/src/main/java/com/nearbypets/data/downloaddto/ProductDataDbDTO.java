package com.nearbypets.data.downloaddto;

import com.nearbypets.data.ProductDbDTO;

import java.util.ArrayList;

/**
 * Created by akshay on 07-06-2016.
 */
public class ProductDataDbDTO extends DownloadDataDbDTO {

    private ArrayList<ProductDbDTO> data;

    public ProductDataDbDTO() {
    }

    /*public ArrayList<ProductDbDTO> getData() {
        return data;
    }*/

    public void setData(ArrayList<ProductDbDTO> data) {
        this.data = data;
    }
}
