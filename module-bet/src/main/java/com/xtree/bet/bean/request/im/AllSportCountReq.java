package com.xtree.bet.bean.request.im;

import java.util.Collections;
import java.util.List;

public class AllSportCountReq {

    private String api;
    private String method;
    private String format;
    private String LanguageCode;
    private boolean IsCombo;
    private List<Integer> FilterType;

    public AllSportCountReq() {
        this.api = "GetAllSportCount";
        this.method = "post";
        this.format = "json";
        this.LanguageCode = "CHS";
        this.IsCombo = false;
        this.FilterType = Collections.singletonList(1);
    }

    public String getLanguageCode() {
        return LanguageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.LanguageCode = languageCode;
    }

    public boolean isCombo() {
        return IsCombo;
    }

    public void setCombo(boolean combo) {
        IsCombo = combo;
    }

    public List<Integer> getFilterType() {
        return FilterType;
    }

    public void setFilterType(List<Integer> filterType) {
        this.FilterType = filterType;
    }

    @Override
    public String toString() {
        return "SportCountReq{" +
                ", languageCode='" + LanguageCode + '\'' +
                ", isCombo=" + IsCombo +
                ", filterType=" + FilterType +
                '}';
    }
}
