package com.nearbypets.data.downloaddto;

import com.nearbypets.data.TypeDataDTO;

import java.util.ArrayList;

/**
 * Created by akshay on 07-06-2016.
 */
public class TypeDataDbDTO extends DownloadDataDbDTO {
    private ArrayList<TypeDataDTO> data;

    public TypeDataDbDTO() {
    }

    public ArrayList<TypeDataDTO> getData() {
        return data;
    }

    public void setData(ArrayList<TypeDataDTO> data) {
        this.data = data;
    }
}
