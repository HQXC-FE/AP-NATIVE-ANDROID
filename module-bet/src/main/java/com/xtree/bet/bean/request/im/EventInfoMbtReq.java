package com.xtree.bet.bean.request.im;

public class EventInfoMbtReq {

    private String api = "GetEventInfoMBT";
    private String method = "post";
    private String format = "json";
    private String LanguageCode = "CHS";
    private int SportId;
    private int Market;
    private int MatchDay;
    private int OddsType;
    private int page;
    private int Season;
    private Boolean Combo = false;

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getLanguageCode() {
        return LanguageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.LanguageCode = languageCode;
    }

    public int getSportId() {
        return SportId;
    }

    public void setSportId(int sportId) {
        this.SportId = sportId;
    }

    public int getMarket() {
        return Market;
    }

    public void setMarket(int market) {
        this.Market = market;
    }

    public int getMatchDay() {
        return MatchDay;
    }

    public void setMatchDay(int matchDay) {
        this.MatchDay = matchDay;
    }

    public int getOddsType() {
        return OddsType;
    }

    public void setOddsType(int oddsType) {
        this.OddsType = oddsType;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSeason() {
        return Season;
    }

    public void setSeason(int season) {
        this.Season = season;
    }

    public Boolean getCombo() {
        return Combo;
    }

    public void setCombo(Boolean combo) {
        this.Combo = combo;
    }

    @Override
    public String toString() {
        return "EventInfoMbtReq{" +
                "api='" + api + '\'' +
                ", method='" + method + '\'' +
                ", format='" + format + '\'' +
                ", LanguageCode='" + LanguageCode + '\'' +
                ", SportId=" + SportId +
                ", Market=" + Market +
                ", MatchDay=" + MatchDay +
                ", OddsType=" + OddsType +
                ", page=" + page +
                ", Season=" + Season +
                ", Combo=" + Combo +
                '}';
    }
}

