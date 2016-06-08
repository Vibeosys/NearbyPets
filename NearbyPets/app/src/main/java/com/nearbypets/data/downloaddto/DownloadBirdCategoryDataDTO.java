package com.nearbypets.data.downloaddto;

import java.util.ArrayList;

/**
 * Created by shrinivas on 04-06-2016.
 */
public class DownloadBirdCategoryDataDTO extends DownloadDataDbDTO {

    private ArrayList<BirdDataDTO> data;

    public DownloadBirdCategoryDataDTO() {
    }

    public ArrayList<BirdDataDTO> getData() {
        return data;
    }

    public void setData(ArrayList<BirdDataDTO> data) {
        this.data = data;
    }
}