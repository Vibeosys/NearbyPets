package com.nearbypets.data.downloaddto;

import java.util.ArrayList;

/**
 * Created by akshay on 07-06-2016.
 */
public class NotificationDataDTO extends DownloadDataDbDTO {
    private ArrayList<NotificationDTO> data;

    public NotificationDataDTO() {
    }

   /* public ArrayList<NotificationDTO> getData() {
        return data;
    }*/

    public void setData(ArrayList<NotificationDTO> data) {
        this.data = data;
    }
}
