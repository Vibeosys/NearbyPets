package com.nearbypets.data;

/**
 * Created by shrinivas on 06-06-2016.
 */
public class ImagesDbDTO {
    private int imageId;
    private String imageFileName;
    private String imageData;

    public ImagesDbDTO(int imageId, String imageFileName, String imageData) {
        this.imageId = imageId;
        this.imageFileName = imageFileName;
        this.imageData = imageData;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }

   /* @Override
    public String toString() {
        return "ImagesDbDTO{" +
                "imageId=" + imageId +
                ", imageFileName='" + imageFileName + '\'' +
                ", imageData='" + imageData + '\'' +
                '}';
    }*/
}
