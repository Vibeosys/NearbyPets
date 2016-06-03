package com.nearbypets.data.downloaddto;

import com.nearbypets.data.BaseDTO;

/**
 * Created by shrinivas on 03-06-2016.
 */
public class NotificationDTO extends BaseDTO {
    private int errorCode;
    private String message;

    public NotificationDTO() {

    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "NotificationDTO{" +
                "errorCode='" + errorCode + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
