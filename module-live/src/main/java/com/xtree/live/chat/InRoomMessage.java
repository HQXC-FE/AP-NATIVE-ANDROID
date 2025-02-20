package com.xtree.live.chat;

import com.google.gson.annotations.SerializedName;
import com.xtree.live.message.Message;

public class InRoomMessage extends Message {
    @SerializedName("vid")
    private String vid;
    public InRoomMessage(String vid) {
        this.action = "sub";
        this.vid = vid;
    }

    public String getVid() {
        return vid;
    }
}