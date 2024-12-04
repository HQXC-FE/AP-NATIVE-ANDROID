package com.xtree.lottery.data.source.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by KAKA on 2024/5/9.
 * Describe:
 */
public class BalanceResponse {

    //{"status":10000,"message":"success","data":{"pre_ag":"10.00","now_ag":"0.00","balance":"10356.0252","dispensing_mul":null},"timestamp":1715259543}

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
    private DataDTO data;
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

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public static class DataDTO {
        /**
         * preAg
         */
        @SerializedName("pre_ag")
        private String preAg;
        /**
         * nowAg
         */
        @SerializedName("now_ag")
        private String nowAg;
        /**
         * balance
         */
        @SerializedName("balance")
        private String balance;
        /**
         * dispensingMul
         */
        @SerializedName("dispensing_mul")
        private Object dispensingMul;

        public String getPreAg() {
            return preAg;
        }

        public void setPreAg(String preAg) {
            this.preAg = preAg;
        }

        public String getNowAg() {
            return nowAg;
        }

        public void setNowAg(String nowAg) {
            this.nowAg = nowAg;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public Object getDispensingMul() {
            return dispensingMul;
        }

        public void setDispensingMul(Object dispensingMul) {
            this.dispensingMul = dispensingMul;
        }
    }
}
