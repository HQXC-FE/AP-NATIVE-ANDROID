package com.xtree.live.message.inroom;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class InRoomLink implements Serializable {

    @SerializedName("text_link")
    private String textLink;

    @SerializedName("text_link_url")
    private String textLinkUrl;

    public String getTextLink() {
        return textLink;
    }

    public void setTextLink(String textLink) {
        this.textLink = textLink;
    }

    public String getTextLinkUrl() {
        return textLinkUrl;
    }

    public void setTextLinkUrl(String textLinkUrl) {
        this.textLinkUrl = textLinkUrl;
    }
}
