package com.xtree.bet.bean.request.im;

public class OutrightEventsReq {

    private String timeStamp;
    private String languageCode;
    private String isCombo;
    private int sportId;
    private String token;
    private String memberCode;
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

    public String getProgrammeId() {
        return programmeId;
    }

    public void setProgrammeId(String programmeId) {
        this.programmeId = programmeId;
    }

    @Override
    public String toString() {
        return "OutrightEventsReq{" + "timeStamp='" + timeStamp + '\'' + ", languageCode='" + languageCode + '\'' + ", isCombo='" + isCombo + '\'' + ", sportId=" + sportId + ", token='" + token + '\'' + ", memberCode='" + memberCode + '\'' + ", programmeId='" + programmeId + '\'' + '}';
    }

}
