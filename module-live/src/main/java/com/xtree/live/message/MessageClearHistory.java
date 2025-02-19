package com.xtree.live.message;

import com.google.gson.annotations.SerializedName;

public class MessageClearHistory extends MessageControl{
    @SerializedName("room_type")
    private @RoomType int roomType;
    @SerializedName("vid")
    private String vid;

    public int getRoomType() {
        return roomType;
    }

    public void setRoomType(@RoomType int roomType) {
        this.roomType = roomType;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getVid() {
        return vid;
    }
}