package com.xtree.bet.bean.request.im;

import com.google.gson.annotations.SerializedName;
import com.xtree.base.global.SPKeyGlobal;

import me.xtree.mvvmhabit.utils.SPUtils;

public class BaseIMRequest {

    @SerializedName("LanguageCode")
    private String LanguageCode = "CHS";
    @SerializedName("TimeStamp")
    String TimeStamp = "H3DZU5M2FBrUipvxJGOa6XkicwKu3Qw6Dh33YB5QuEI";
    String format = "json";
    String method = "post";
    String api;
    @SerializedName("Token")
    String token = SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN);

    @SerializedName("MemberCode")
    String memberCode;


    public String getLanguageCode() {
        return LanguageCode;
    }

    public void setLanguageCode(String languageCode) {
        LanguageCode = languageCode;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        TimeStamp = timeStamp;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }
}
