package com.xtree.live.message;

import com.google.gson.annotations.SerializedName;

public class MessageLiveClose extends MessageControl implements MessageVid{

    @SerializedName("fd")
    private String fd;
    @SerializedName("vid")
    private String vid;
    @SerializedName("userid")
    private long userid;
    @SerializedName("seedsy")
    private String seedsy;

    public String getFd() {
        return fd;
    }

    public void setFd(String fd) {
        this.fd = fd;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public String getSeedsy() {
        return seedsy;
    }

    public void setSeedsy(String seedsy) {
        this.seedsy = seedsy;
    }
}