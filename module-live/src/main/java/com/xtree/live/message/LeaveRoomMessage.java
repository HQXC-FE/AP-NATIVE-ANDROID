package com.xtree.live.message;

import com.google.gson.annotations.SerializedName;

public class LeaveRoomMessage extends Message{
    @SerializedName("vid")
    private String vid;
    public LeaveRoomMessage(String vid) {
        this.action = "unsub";
        this.vid = vid;
    }

    public String getVid() {
        return vid;
    }
}

