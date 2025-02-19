package com.xtree.live.message;

import com.google.gson.annotations.SerializedName;

public abstract class MessageChat extends Message implements MessageVid {
    @SerializedName("fd")
    protected int fd;
    @SerializedName(value = "vid", alternate = {"newMsgRoomvid"})
    protected String vid;
    @SerializedName(value = "room_type",alternate = {"type"})
    protected int roomType;
    @SerializedName("seed")
    protected String seed;
    @SerializedName("seedsy")
    protected String seedsy;

    public int getFd() {
        return fd;
    }

    public void setFd(int fd) {
        this.fd = fd;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public int getRoomType() {
        return roomType;
    }

    public void setRoomType(int roomType) {
        this.roomType = roomType;
    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public String getSeedsy() {
        return seedsy;
    }

    public void setSeedsy(String seedsy) {
        this.seedsy = seedsy;
    }
}

