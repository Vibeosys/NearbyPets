package com.nearbypets.data;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by shrinivas on 04-06-2016.
 */
public class TypeDataDTO extends BaseDTO {
    private String typeTitle;
    private int typeId;

    public TypeDataDTO() {
    }

    public String getTypeTitle() {
        return typeTitle;
    }

    public void setTypeTitle(String typeTitle) {
        this.typeTitle = typeTitle;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public static ArrayList<TypeDataDTO> deserializeToArray(String serializedString) {
        Gson gson = new Gson();
        TypeDataDTO[] deserializeObject = gson.fromJson(serializedString, TypeDataDTO[].class);
        ArrayList<TypeDataDTO> objectList = (ArrayList<TypeDataDTO>) Arrays.asList(deserializeObject);
        return objectList;
    }
}
