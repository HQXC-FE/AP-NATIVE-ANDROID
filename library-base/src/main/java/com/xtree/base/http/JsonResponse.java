package com.xtree.base.http;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JsonResponse {

    private int status = -1;
    private int code = -1;
    private boolean success;
    @SerializedName(value = "message", alternate = {"msg", "sMsg"})
    @Expose
    private String message = ""; // msg, sMsg
    public int timestamp; // 1700702751
    private Data data;

    //    //原数据
//    @SerializedName("data")
//    private Object data;
//    //转换数据
//    private T dataBean;
    private String dataString;

    @Override
    public String toString() {
        return "JsonResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                //", data=" + data +
                '}';
    }

    public int getStatus() {
        return status;
    }

    public int getCode() {
        return code;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public void setDataString(String dataString) {
        this.dataString = dataString;
    }

    public boolean isOk() {
        return status == 10000;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public static class Data<MatchInfo> {
        private String code;
        private List<MatchInfo> data; // List of integers
        private String msg;
        private long ts;

        // Getters and Setters
        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public List<MatchInfo> getData() {
            return data;
        }

        public void setData(List<MatchInfo> data) {
            this.data = data;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public long getTs() {
            return ts;
        }

        public void setTs(long ts) {
            this.ts = ts;
        }


    }
}
