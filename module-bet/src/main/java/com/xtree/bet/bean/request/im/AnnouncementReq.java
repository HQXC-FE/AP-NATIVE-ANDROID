package com.xtree.bet.bean.request.im;

public class AnnouncementReq {
    private String timeStamp;

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "AnnouncementReq{" +
                "timeStamp='" + timeStamp + '\'' +
                '}';
    }
}
