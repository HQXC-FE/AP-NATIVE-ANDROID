package com.xtree.live.message;

import com.google.gson.annotations.SerializedName;

public abstract class Message {
    @SerializedName("action")
    protected String action;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
