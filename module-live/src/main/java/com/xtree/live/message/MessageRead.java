package com.xtree.live.message;

import com.google.gson.annotations.SerializedName;

public class MessageRead extends MessageControl implements MessageVid{
    @SerializedName("vid")
    private String vid;
    @SerializedName("room_type")
    private int roomType;
    @SerializedName("readed_count")
    private int readCount;
    @SerializedName("seed")
    private String seed;
    @SerializedName("seedsy")
    private String seedsy;

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

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
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

