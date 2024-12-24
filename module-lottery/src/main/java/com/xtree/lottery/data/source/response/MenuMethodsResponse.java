package com.xtree.lottery.data.source.response;

import com.google.gson.annotations.SerializedName;
import com.xtree.lottery.data.source.vo.MenuMethodsData;

/**
 * Created by KAKA on 2024/4/29.
 * Describe:
 */
public class MenuMethodsResponse {

    /**
     * status
     */
    @SerializedName("status")
    private int status;
    /**
     * message
     */
    @SerializedName("message")
    private String message;
    /**
     * data
     */
    @SerializedName("data")
    private MenuMethodsData data;
    /**
     * timestamp
     */
    @SerializedName("timestamp")
    private int timestamp;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MenuMethodsData getData() {
        return data;
    }

    public void setData(MenuMethodsData data) {
        this.data = data;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

}
