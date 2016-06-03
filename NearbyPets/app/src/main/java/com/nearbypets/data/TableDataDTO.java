package com.nearbypets.data;

import android.support.annotation.Nullable;

/**
 * Created by akshay on 3/6/2016.
 */
public class TableDataDTO {
    private String operationData;
    private String operation;

    public TableDataDTO(String data, String operation) {
        this.operationData = data;
        this.operation = operation;
    }

    public String getOperationData() {
        return operationData;
    }

    public void setOperationData(String operationData) {
        this.operationData = operationData;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
