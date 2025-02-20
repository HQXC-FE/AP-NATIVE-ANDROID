package com.xtree.live.chat;

import com.google.gson.annotations.SerializedName;
import com.xtree.live.message.Message;

public class LeaveRoomMessage extends Message {
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

