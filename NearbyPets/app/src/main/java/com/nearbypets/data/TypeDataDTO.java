package com.nearbypets.data;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

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
        ArrayList<TypeDataDTO> typeDataDTOs = null;

        try
        {
            TypeDataDTO[] deserializeObject = gson.fromJson(serializedString, TypeDataDTO[].class);
            typeDataDTOs = new ArrayList<>();
            for(TypeDataDTO typeDataDTO: deserializeObject)
            {
                typeDataDTOs.add(typeDataDTO);
            }


        }catch (JsonSyntaxException e)
        {
            Log.e("Deserialization", "## error in post Ad type spinner DTO" + e.toString());
        }
        return typeDataDTOs;

    }
}
