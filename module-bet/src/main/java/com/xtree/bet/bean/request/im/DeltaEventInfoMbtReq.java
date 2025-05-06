package com.xtree.bet.bean.request.im;


public class DeltaEventInfoMbtReq {

    private int sportId;
    private int market;
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

    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    @Override
    public String toString() {
        return "DeltaCompetitionReq{" + "sportId=" + sportId + ", market=" + market + '\'' + ", delta=" + delta + '}';
    }


}
