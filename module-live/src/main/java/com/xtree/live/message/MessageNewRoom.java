package com.xtree.live.message;

import com.google.gson.annotations.SerializedName;

public class MessageNewRoom extends MessageControl implements MessageVid{
    @SerializedName("newRoomvid")
    protected String vid;
    @SerializedName("isVir")
    private int isVir;
    @SerializedName("time")
    private String time;
    @SerializedName("room_type")
    private int room_type;
    @SerializedName("data")
    private NewRoomData data;
    @SerializedName("seed")
    private String seed;
    @SerializedName("seedsy")
    private String seedsy;

    @Override
    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public int getIsVir() {
        return isVir;
    }

    public void setIsVir(int isVir) {
        this.isVir = isVir;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getRoom_type() {
        return room_type;
    }

    public void setRoom_type(int room_type) {
        this.room_type = room_type;
    }


    public NewRoomData getData() {
        return data;
    }

    public void setData(NewRoomData data) {
        this.data = data;
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

