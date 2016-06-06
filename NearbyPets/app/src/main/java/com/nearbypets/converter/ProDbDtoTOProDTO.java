package com.nearbypets.converter;

import com.nearbypets.data.ProductDataDTO;
import com.nearbypets.data.ProductDbDTO;

import java.util.ArrayList;

/**
 * Created by akshay on 03-06-2016.
 */
public class ProDbDtoTOProDTO {

    private ArrayList<ProductDbDTO> productDbDTOs;

    public ProDbDtoTOProDTO(ArrayList<ProductDbDTO> productDbDTOs) {
        this.productDbDTOs = productDbDTOs;
    }

    public ArrayList<ProductDataDTO> getProductDTOs() {
        ArrayList<ProductDataDTO> productDataDTOs = new ArrayList<>();
        for (int i = 0; i < productDbDTOs.size(); i++) {
            ProductDbDTO product = productDbDTOs.get(i);
            productDataDTOs.add(new ProductDataDTO(product.getAdid(), product.getName(), product.getImage(),
                    product.getDescription(), product.getDistance(), product.getPrice(), false, "Posted On 14/5/2016"));

        }
        return productDataDTOs;
    }
}
