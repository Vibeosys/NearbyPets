package com.nearbypets.data;

/**
 * Created by akshay on 07-06-2016.
 */
public class SortDTO {

    private String name;
    private int value;
    private String sorting;

    public SortDTO(String name, int value, String sorting) {
        this.name = name;
        this.value = value;
        this.sorting = sorting;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getSorting() {
        return sorting;
    }

    public void setSorting(String sorting) {
        this.sorting = sorting;
    }
}
