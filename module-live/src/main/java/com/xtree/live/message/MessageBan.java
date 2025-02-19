package com.xtree.live.message;

import com.google.gson.annotations.SerializedName;

public class MessageBan extends MessageChat {
    @SerializedName("userid")
    private String useId;

    public String getUseId() {
        return useId;
    }
}
