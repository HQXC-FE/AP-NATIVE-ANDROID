package com.xtree.bet.bean.request.im;

import java.util.List;

public class CometitionCountReq {
    private String timeStamp;
    private String languageCode;
    private String isCombo;
    private int sportId;
    private int market;
    private String includeCloseEvent;
    private String eventDate;
    private List<Integer> eventGroupTypeIds;
    private String programmeId;

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getIsCombo() {
        return isCombo;
    }

    public void setIsCombo(String isCombo) {
        this.isCombo = isCombo;
    }

    public int getSportId() {
        return sportId;
    }

    public void setSportId(int sportId) {
        this.sportId = sportId;
    }

    public int getMarket() {
        return market;
    }

    public void setMarket(int market) {
        this.market = market;
    }

    public String getIncludeCloseEvent() {
        return includeCloseEvent;
    }

    public void setIncludeCloseEvent(String includeCloseEvent) {
        this.includeCloseEvent = includeCloseEvent;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public List<Integer> getEventGroupTypeIds() {
        return eventGroupTypeIds;
    }

    public void setEventGroupTypeIds(List<Integer> eventGroupTypeIds) {
        this.eventGroupTypeIds = eventGroupTypeIds;
    }

    public String getProgrammeId() {
        return programmeId;
    }

    public void setProgrammeId(String programmeId) {
        this.programmeId = programmeId;
    }

    @Override
    public String toString() {
        return "CometitionCountReq{" +
                "timeStamp='" + timeStamp + '\'' +
                ", languageCode='" + languageCode + '\'' +
                ", isCombo='" + isCombo + '\'' +
                ", sportId=" + sportId +
                ", market=" + market +
                ", includeCloseEvent='" + includeCloseEvent + '\'' +
                ", eventDate='" + eventDate + '\'' +
                ", eventGroupTypeIds=" + eventGroupTypeIds +
                ", programmeId='" + programmeId + '\'' +
                '}';
    }
}
