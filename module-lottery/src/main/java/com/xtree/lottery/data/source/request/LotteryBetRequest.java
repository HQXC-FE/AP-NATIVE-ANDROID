package com.xtree.lottery.data.source.request;

import com.google.gson.annotations.SerializedName;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.base.utils.UuidUtil;

import java.util.List;

/**
 * Created by KAKA on 2024/5/10.
 * Describe:
 */
public class LotteryBetRequest {

    //{"curmid":"2309","lotteryid":14,"lt_issue_start":"20240510-258","lt_project":[{"methodid":2370,"codes":"2|7|2","omodel":2,"mode":1,"times":1,"poschoose":null,"menuid":2331,"type":"digital","nums":1,"money":2,"solo":true,"desc":"[后三码_复式]-,-,2,7,2"},{"methodid":2370,"codes":"8|8|8","omodel":2,"mode":1,"times":1,"poschoose":null,"menuid":2331,"type":"digital","nums":1,"money":2,"solo":true,"desc":"[后三码_复式]-,-,8,8,8"}],"lt_total_money":4,"lt_total_nums":2,"play_source":6,"nonce":"1096024803a2c1cb95e456ebad51f444"}

    /**
     * curmid
     */
    @SerializedName("curmid")
    private String curmid;
    /**
     * lotteryid
     */
    @SerializedName("lotteryid")
    private int lotteryid;
    /**
     * ltIssueStart
     */
    @SerializedName("lt_issue_start")
    private String lt_issue_start;
    /**
     * ltProject
     */
    @SerializedName("lt_project")
    private List<BetOrderData> lt_project;
    /**
     * ltTotalMoney
     */
    @SerializedName("lt_total_money")
    private String lt_total_money;
    /**
     * ltTotalNums
     */
    @SerializedName("lt_total_nums")
    private int lt_total_nums;
    /**
     * playSource
     */
    @SerializedName("play_source")
    private int play_source;
    /**
     * nonce
     */
    @SerializedName("nonce")
    private String nonce;

    public String getCurmid() {
        return curmid;
    }

    public void setCurmid(String curmid) {
        this.curmid = curmid;
    }

    public int getLotteryid() {
        return lotteryid;
    }

    public void setLotteryid(int lotteryid) {
        this.lotteryid = lotteryid;
    }

    public String getNonce() {
        return UuidUtil.getID24();
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getLt_issue_start() {
        return lt_issue_start;
    }

    public void setLt_issue_start(String lt_issue_start) {
        this.lt_issue_start = lt_issue_start;
    }

    public List<BetOrderData> getLt_project() {
        return lt_project;
    }

    public void setLt_project(List<BetOrderData> lt_project) {
        this.lt_project = lt_project;
    }

    public String getLt_total_money() {
        return lt_total_money;
    }

    public void setLt_total_money(String lt_total_money) {
        this.lt_total_money = lt_total_money;
    }

    public int getLt_total_nums() {
        return lt_total_nums;
    }

    public void setLt_total_nums(int lt_total_nums) {
        this.lt_total_nums = lt_total_nums;
    }

    public int getPlay_source() {
        return play_source;
    }

    public void setPlay_source(int play_source) {
        this.play_source = play_source;
    }

    public static class BetOrderData extends BindModel {
        /**
         * methodid
         */
        @SerializedName("methodid")
        private String methodid;
        /**
         * codes
         */
        @SerializedName("codes")
        private String codes;
        /**
         * omodel
         */
        @SerializedName("omodel")
        private int omodel;
        /**
         * mode
         */
        @SerializedName("mode")
        private int mode;
        /**
         * times
         */
        @SerializedName("times")
        private int times;
        /**
         * poschoose
         */
        @SerializedName("poschoose")
        private String poschoose = null;
        /**
         * menuid
         */
        @SerializedName("menuid")
        private String menuid;
        /**
         * type
         */
        @SerializedName("type")
        private String type;
        /**
         * nums
         */
        @SerializedName("nums")
        private int nums;
        /**
         * money
         */
        @SerializedName("money")
        private double money;
        /**
         * solo
         */
        @SerializedName("solo")
        private boolean solo;

        /**
         * moneyName
         */
        @SerializedName("solo")
        private String moneyName = "";

        public String getMoneyName() {
            return moneyName;
        }

        public void setMoneyName(String moneyName) {
            this.moneyName = moneyName;
        }

        /**
         * desc
         */
        @SerializedName("desc")
        private String desc;

        public String getMethodid() {
            return methodid;
        }

        public void setMethodid(String methodid) {
            this.methodid = methodid;
        }

        public String getCodes() {
            return codes;
        }

        public void setCodes(String codes) {
            this.codes = codes;
        }

        public int getOmodel() {
            return omodel;
        }

        public void setOmodel(int omodel) {
            this.omodel = omodel;
        }

        public int getMode() {
            return mode;
        }

        public void setMode(int mode) {
            this.mode = mode;
        }

        public int getTimes() {
            return times;
        }

        public void setTimes(int times) {
            this.times = times;
        }

        public String getPoschoose() {
            return poschoose;
        }

        public void setPoschoose(String poschoose) {
            this.poschoose = poschoose;
        }

        public String getMenuid() {
            return menuid;
        }

        public void setMenuid(String menuid) {
            this.menuid = menuid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getNums() {
            return nums;
        }

        public void setNums(int nums) {
            this.nums = nums;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public boolean isSolo() {
            return solo;
        }

        public void setSolo(boolean solo) {
            this.solo = solo;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
