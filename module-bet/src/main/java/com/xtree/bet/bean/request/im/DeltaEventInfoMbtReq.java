package com.xtree.bet.bean.request.im;

public class DeltaEventInfoMbtReq {


    private int sportId;
    private int market;
    private String timeStamp;
    private double delta;

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

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    @Override
    public String toString() {
        return "DeltaCompetitionReq{" + "sportId=" + sportId + ", market=" + market + ", timeStamp='" + timeStamp + '\'' + ", delta=" + delta + '}';
    }


}
