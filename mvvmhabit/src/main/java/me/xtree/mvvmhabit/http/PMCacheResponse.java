package me.xtree.mvvmhabit.http;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by vickers on 2024/12/4.
 *
 * 处理本地缓存的三方接口数据Response,因缓存到后台转发后多增加了data嵌套，
 *
 * 所以需要改变之前的response结构
 */

public class PMCacheResponse<T> {

    private int status = -1;
    private int code = -1;
    private boolean success;
    @SerializedName(value = "message", alternate = {"msg", "sMsg"})
    @Expose
    private String message = ""; // msg, sMsg
    public int timestamp; // 1700702751
    private Data<T> data;

    @Override
    public String toString() {
        return "PMCacheResponse{" +
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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public static class Data<T> {
        private int code;
        private  T data; // List of integers
        private String msg;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public T getData() {
            return data;
        }

    }
}
