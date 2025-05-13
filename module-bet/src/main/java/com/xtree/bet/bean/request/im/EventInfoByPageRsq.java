package com.xtree.bet.bean.request.im;

import java.util.Arrays;
import java.util.List;

public class EventInfoByPageRsq {

    private String api = "GetEventInfoByPage";
    private String method = "post";
    private String format = "json";


    private String LanguageCode = "CHS";
    private String TimeStamp;
    //private String Token = "09adefdde10c202a0c6c9bb54c810850";
    private int OddsType;
    //private String MemberCode = "p02hill999";
    private List<Integer> BetTypeIds = Arrays.asList(1, 2, 3, 4);
    private String Market;
    private int MatchDay;
    private int OrderBy = 2;
    private int Page;
    private List<Integer> PeriodIds = Arrays.asList(1, 2);
    private int Season;
    private int SportId;
    private Boolean IsCombo;

    public String getLanguageCode() {
        return LanguageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.LanguageCode = languageCode;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.TimeStamp = timeStamp;
    }
//
//    public String getToken() {
//        return Token;
//    }
//
//    public void setToken(String token) {
//        this.Token = token;
//    }

    public int getOddsType() {
        return OddsType;
    }

    public void setOddsType(int oddsType) {
        this.OddsType = oddsType;
    }

//    public String getMemberCode() {
//        return MemberCode;
//    }
//
//    public void setMemberCode(String memberCode) {
//        this.MemberCode = memberCode;
//    }

    public List<Integer> getBetTypeIds() {
        return BetTypeIds;
    }

    public void setBetTypeIds(List<Integer> betTypeIds) {
        this.BetTypeIds = betTypeIds;
    }

    public String getMarket() {
        return Market;
    }

    public void setMarket(String market) {
        this.Market = market;
    }

    public int getMatchDay() {
        return MatchDay;
    }

    public void setMatchDay(int matchDay) {
        this.MatchDay = matchDay;
    }

    public int getOrderBy() {
        return OrderBy;
    }

    public void setOrderBy(int orderBy) {
        this.OrderBy = orderBy;
    }

    public int getPage() {
        return Page;
    }

    public void setPage(int page) {
        this.Page = page;
    }

    public List<Integer> getPeriodIds() {
        return PeriodIds;
    }

    public void setPeriodIds(List<Integer> periodIds) {
        this.PeriodIds = periodIds;
    }

    public int getSeason() {
        return Season;
    }

    public void setSeason(int season) {
        this.Season = season;
    }

    public int getSportId() {
        return SportId;
    }

    public void setSportId(int sportId) {
        this.SportId = sportId;
    }

    public Boolean getIsCombo() {
        return IsCombo;
    }

    public void setIsCombo(Boolean isCombo) {
        this.IsCombo = isCombo;
    }

    @Override
    public String toString() {
        return "EventInfoFullReq{" +
                "LanguageCode='" + LanguageCode + '\'' +
                ", TimeStamp='" + TimeStamp + '\'' +
//                ", Token='" + Token + '\'' +
                ", OddsType=" + OddsType +
//                ", MemberCode='" + MemberCode + '\'' +
                ", BetTypeIds=" + BetTypeIds +
                ", Market='" + Market + '\'' +
                ", MatchDay=" + MatchDay +
                ", OrderBy=" + OrderBy +
                ", Page=" + Page +
                ", PeriodIds=" + PeriodIds +
                ", Season=" + Season +
                ", SportId=" + SportId +
                ", IsCombo=" + IsCombo +
                '}';
    }
}

