package com.xtree.lottery.data.source.vo;

import com.google.gson.annotations.SerializedName;

public class BetResult {

    // {
    //        "wcodes": "79556",
    //        "bonus": "0.0000",
    //        "lotteryid": 23,
    //        "writetime": "0000-00-00 00:00:00",
    //        "code": "",
    //        "totalprice": 0,
    //        "method": ""
    //    }
    @SerializedName(value = "wcodes")
    private String wcodes;

    @SerializedName(value = "bonus")
    private String bonus;

    @SerializedName(value = "lotteryid")
    private Long lotteryid;

    @SerializedName(value = "code")
    private String code;

    @SerializedName(value = "method")
    private String method;

    @SerializedName(value = "totalprice")
    private String totalprice;

    public String getWcodes() {
        return wcodes;
    }

    public void setWcodes(String wcodes) {
        this.wcodes = wcodes;
    }

    public String getBonus() {
        return bonus;
    }

    public void setBonus(String bonus) {
        this.bonus = bonus;
    }

    public Long getLotteryid() {
        return lotteryid;
    }

    public void setLotteryid(Long lotteryid) {
        this.lotteryid = lotteryid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }


}
