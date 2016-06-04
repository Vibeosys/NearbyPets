package com.nearbypets.data.downloaddto;

import com.nearbypets.data.BaseDTO;

/**
 * Created by shrinivas on 03-06-2016.
 */
public class NotificationDTO extends BaseDTO {
    private int errorCode;
    private String message;
    private UserDbDTO data;

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

    public UserDbDTO getData() {
        return data;
    }

    public void setData(UserDbDTO data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "NotificationDTO{" +
                "errorCode=" + errorCode +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
