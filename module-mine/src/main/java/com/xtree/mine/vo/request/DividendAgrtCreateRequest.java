package com.xtree.mine.vo.request;

import com.google.gson.annotations.SerializedName;
import com.xtree.base.utils.UuidUtil;

import java.util.List;

/**
 * Created by KAKA on 2024/4/5.
 * Describe: 彩票契约创建请求体
 */
public class DividendAgrtCreateRequest {

    /**
     * userid
     */
    @SerializedName("userid")
    private Object userid;
    /**
     * profit
     */
    @SerializedName("profit")
    private List<String> profit;
    /**
     * people
     */
    @SerializedName("people")
    private List<String> people;

    /**
     * lose streak
     */
    @SerializedName("lose_streak")
    private List<String> lose_streak;
    /**
     * ratio
     */
    @SerializedName("ratio")
    private List<String> ratio;
    /**
     * ratio_range 分红比例范围
     */
    @SerializedName("ratio_range")
    private List<String> ratio_range;
    /**
     * dayPeople
     */
    @SerializedName("day_people")
    private List<String> day_people;
    /**
     * weekPeople
     */
    @SerializedName("week_people")
    private List<String> week_people;
    /**
     * type
     */
    @SerializedName("type")
    private String type;
    /**
     * netProfit
     */
    @SerializedName("net_profit")
    private List<String> net_profit;

    /**
     * netProfit
     */
    @SerializedName("flag")
    private String flag;

    /**
     * nonce
     */
    @SerializedName("nonce")
    private String nonce;

    public List<String> getRatio_range() {
        return ratio_range;
    }

    public void setRatio_range(List<String> ratio_range) {
        this.ratio_range = ratio_range;
    }

    public Object getUserid() {
        return userid;
    }

    public void setUserid(Object userid) {
        this.userid = userid;
    }

    public List<String> getProfit() {
        return profit;
    }

    public void setProfit(List<String> profit) {
        this.profit = profit;
    }

    public List<String> getPeople() {
        return people;
    }

    public void setPeople(List<String> people) {
        this.people = people;
    }

    public List<String> getRatio() {
        return ratio;
    }

    public void setRatio(List<String> ratio) {
        this.ratio = ratio;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getNonce() {
        return UuidUtil.getID24();
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public List<String> getDay_people() {
        return day_people;
    }

    public void setDay_people(List<String> day_people) {
        this.day_people = day_people;
    }

    public List<String> getWeek_people() {
        return week_people;
    }

    public void setWeek_people(List<String> week_people) {
        this.week_people = week_people;
    }

    public List<String> getNet_profit() {
        return net_profit;
    }

    public void setNet_profit(List<String> net_profit) {
        this.net_profit = net_profit;
    }

    public List<String> getLose_streak() {
        return lose_streak;
    }

    public void setLose_streak(List<String> lose_streak) {
        this.lose_streak = lose_streak;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
