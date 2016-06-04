package com.nearbypets.data.downloaddto;

import com.nearbypets.data.BaseDTO;

import java.util.ArrayList;

/**
 * Created by akshay on 04-06-2016.
 */
public class ProductDescDbDTO extends BaseDTO {

    private ProductPetDbDTO pet;
    private ProductSellerDbDTO seller;
    private ProductDetailsDbDTO details;
    private ArrayList<ProductImagesDbDTO> images;

    public ProductDescDbDTO() {
    }

    public ProductPetDbDTO getPet() {
        return pet;
    }

    public void setPet(ProductPetDbDTO pet) {
        this.pet = pet;
    }

    public ProductSellerDbDTO getSeller() {
        return seller;
    }

    public void setSeller(ProductSellerDbDTO seller) {
        this.seller = seller;
    }

    public ProductDetailsDbDTO getDetails() {
        return details;
    }

    public void setDetails(ProductDetailsDbDTO details) {
        this.details = details;
    }

    public ArrayList<ProductImagesDbDTO> getImages() {
        return images;
    }

    public void setImages(ArrayList<ProductImagesDbDTO> images) {
        this.images = images;
    }
}
