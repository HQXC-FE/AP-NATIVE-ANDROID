package com.xtree.live.message;

import com.google.gson.annotations.SerializedName;

public class MessageReserve extends MessageControl {

    @SerializedName("userid")
    private String userid;
    @SerializedName("room_type")
    private int roomType;
    @SerializedName("data")
    private String data;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getRoomType() {
        return roomType;
    }

    public void setRoomType(int roomType) {
        this.roomType = roomType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
