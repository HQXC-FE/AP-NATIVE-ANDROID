package com.xtree.bet.bean.request.im;

import java.util.List;

public class SportCountReq {

    private String timeStamp;
    private String languageCode = "CMN";
    private boolean isCombo;
    private List<Integer> filterType;

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

    public boolean isCombo() {
        return isCombo;
    }

    public void setCombo(boolean combo) {
        isCombo = combo;
    }

    public List<Integer> getFilterType() {
        return filterType;
    }

    public void setFilterType(List<Integer> filterType) {
        this.filterType = filterType;
    }

    @Override
    public String toString() {
        return "SportCountReq{" +
                "timeStamp='" + timeStamp + '\'' +
                ", languageCode='" + languageCode + '\'' +
                ", isCombo=" + isCombo +
                ", filterType=" + filterType +
                '}';
    }
}
