package com.xtree.lottery.data.source.request;

import com.google.gson.annotations.SerializedName;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.base.utils.UuidUtil;

import java.util.HashMap;
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
    private String ltIssueStart;
    /**
     * ltProject
     */
    @SerializedName("lt_project")
    private List<BetOrderData> ltProject;
    /**
     * ltTotalMoney
     */
    @SerializedName("lt_total_money")
    private int ltTotalMoney;
    /**
     * ltTotalNums
     */
    @SerializedName("lt_total_nums")
    private int ltTotalNums;
    /**
     * playSource
     */
    @SerializedName("play_source")
    private int playSource;
    /**
     * nonce
     */
    @SerializedName("nonce")
    private String nonce;
    @SerializedName("lt_trace_count_input")
    public int lt_trace_count_input;
    @SerializedName("lt_trace_if")
    public String lt_trace_if;
    @SerializedName("lt_trace_issues")
    public HashMap<String, Integer> lt_trace_issues;
    @SerializedName("lt_trace_money")
    public String lt_trace_money;
    @SerializedName("lt_trace_stop")
    public String lt_trace_stop;

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

    public String getLtIssueStart() {
        return ltIssueStart;
    }

    public void setLtIssueStart(String ltIssueStart) {
        this.ltIssueStart = ltIssueStart;
    }

    public List<BetOrderData> getLtProject() {
        return ltProject;
    }

    public void setLtProject(List<BetOrderData> ltProject) {
        this.ltProject = ltProject;
    }

    public int getLtTotalMoney() {
        return ltTotalMoney;
    }

    public void setLtTotalMoney(int ltTotalMoney) {
        this.ltTotalMoney = ltTotalMoney;
    }

    public int getLtTotalNums() {
        return ltTotalNums;
    }

    public void setLtTotalNums(int ltTotalNums) {
        this.ltTotalNums = ltTotalNums;
    }

    public int getPlaySource() {
        return playSource;
    }

    public void setPlaySource(int playSource) {
        this.playSource = playSource;
    }

    public String getNonce() {
        return UuidUtil.getID24();
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
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
        private String poschoose;
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
        private int money;
        /**
         * solo
         */
        @SerializedName("solo")
        private boolean solo;
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

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
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
