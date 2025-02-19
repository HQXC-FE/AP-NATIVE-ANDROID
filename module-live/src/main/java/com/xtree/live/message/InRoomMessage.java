package com.xtree.live.message;

import com.google.gson.annotations.SerializedName;

public class InRoomMessage extends Message{
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

