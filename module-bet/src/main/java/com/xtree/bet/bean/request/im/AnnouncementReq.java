package com.xtree.bet.bean.request.im;

import com.google.gson.annotations.SerializedName;


public class AnnouncementReq {

    private String api;
    private String method;
    private String format;
    @SerializedName("TimeStamp")
    private String timeStamp;
    private String languageCode;

    public AnnouncementReq() {
        this.api = "GetAnnouncement";
        this.method = "post";
        this.format = "json";
        this.languageCode = "chz";
    }

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

    @Override
    public String toString() {
        return "AnnouncementReq{" +
                "api='" + api + '\'' +
                ", method='" + method + '\'' +
                ", format='" + format + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", languageCode='" + languageCode + '\'' +
                '}';
    }
}
