package com.xtree.live.ui.main.model.chat;

public class GetRoomInfoRequest {
    private int uid;
    private String channel_code;
    public GetRoomInfoRequest(int uid, String channel_code) {
        this.uid = uid;
        this.channel_code = channel_code;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getChannel_code() {
        return channel_code;
    }

    public void setChannel_code(String channel_code) {
        this.channel_code = channel_code;
    }
}
