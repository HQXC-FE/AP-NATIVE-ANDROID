package com.xtree.live.data.source.response;

public class QualityBean {
    /**
     * smooth : http://livepull.mhuan.shop/live/2_1636707429_ly540.m3u8
     * sd : http://livepull.mhuan.shop/live/2_1636707429_ly720.m3u8
     * hd : http://livepull.mhuan.shop/live/2_1636707429_ly1080.m3u8
     */

    private String smooth;
    private String sd;
    private String hd;

    public String getSmooth() {
        return smooth;
    }

    public void setSmooth(String smooth) {
        this.smooth = smooth;
    }

    public String getSd() {
        return sd;
    }

    public void setSd(String sd) {
        this.sd = sd;
    }

    public String getHd() {
        return hd;
    }

    public void setHd(String hd) {
        this.hd = hd;
    }
}

