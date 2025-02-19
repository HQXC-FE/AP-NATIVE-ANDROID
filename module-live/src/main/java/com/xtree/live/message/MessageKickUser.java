package com.xtree.live.message;

import com.google.gson.annotations.SerializedName;

public class MessageKickUser extends  Message implements MessageVid{
    @SerializedName("vid")
    private String vid;
    @SerializedName("room_type")
    private int roomType;
    @SerializedName("userid")
    private String userid;

    @Override
    public String getVid() {
        return vid;
    }

    public int getRoomType() {
        return roomType;
    }

    public String getUserid() {
        return userid;
    }
}

