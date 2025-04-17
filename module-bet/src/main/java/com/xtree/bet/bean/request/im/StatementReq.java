package com.xtree.bet.bean.request.im;

public class StatementReq {

    private String timeStamp;
    private String token;
    private String memberCode;
    private String languageCode;
    private int dateType;
    private String startDate;
    private String endDate;

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
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

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public int getDateType() {
        return dateType;
    }

    public void setDateType(int dateType) {
        this.dateType = dateType;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "StatementReq{" + "timeStamp='" + timeStamp + '\'' + ", token='" + token + '\'' + ", memberCode='" + memberCode + '\'' + ", languageCode='" + languageCode + '\'' + ", dateType=" + dateType + ", startDate='" + startDate + '\'' + ", endDate='" + endDate + '\'' + '}';
    }

}
