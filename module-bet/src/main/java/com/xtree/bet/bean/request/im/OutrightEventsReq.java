package com.xtree.bet.bean.request.im;

import com.google.gson.annotations.SerializedName;

public class OutrightEventsReq {

    @SerializedName("IsCombo")
    private boolean isCombo;

    @SerializedName("SportId")
    private int sportId;

    @SerializedName("MatchDay")
    private int matchDay;

    @SerializedName("OddsType")
    private String oddsType;

    @SerializedName("OrderBy")
    private int orderBy;

    @SerializedName("Page")
    private int page;

    @SerializedName("Season")
    private int season;

    @SerializedName("MemberCode")
    private String memberCode;

    public boolean getIsCombo() {
        return isCombo;
    }

    public void setIsCombo(boolean isCombo) {
        this.isCombo = isCombo;
    }

    public int getSportId() {
        return sportId;
    }

    public void setSportId(int sportId) {
        this.sportId = sportId;
    }

    public int getMatchDay() {
        return matchDay;
    }

    public void setMatchDay(int matchDay) {
        this.matchDay = matchDay;
    }

    public String getOddsType() {
        return oddsType;
    }

    public void setOddsType(String oddsType) {
        this.oddsType = oddsType;
    }

    public int getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(int orderBy) {
        this.orderBy = orderBy;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    @Override
    public String toString() {
        return "OutrightEventsReq{" +
                "isCombo=" + isCombo +
                ", sportId=" + sportId +
                ", matchDay=" + matchDay +
                ", oddsType='" + oddsType + '\'' +
                ", orderBy=" + orderBy +
                ", page=" + page +
                ", season=" + season +
                ", memberCode='" + memberCode + '\'' +
                '}';
    }
}
