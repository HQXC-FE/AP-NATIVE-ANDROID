package com.xtree.live.message;

import com.google.gson.annotations.SerializedName;

public class MessageDelete extends MessageChat {
    @SerializedName("msg_id")
    private String msgId;

    @SerializedName("msg_seed")
    private String msgSeed;

    public String getMsgSeed() {
        return msgSeed;
    }

    public void setMsgSeed(String msgSeed) {
        this.msgSeed = msgSeed;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}

