package com.xtree.mine.vo.response;

import com.google.gson.annotations.SerializedName;

public class ShowThirdManagement {
    @SerializedName("data")
    private DataDTO data;
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private int status;
    @SerializedName("timestamp")
    private int timestamp;

    public DataDTO getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public class DataDTO {
        @SerializedName("show")
        private int show;
        @SerializedName("info")
        private String info;

        public int getShow() {
            return show;
        }

        public String getInfo() {
            return info;
        }
    }

}
