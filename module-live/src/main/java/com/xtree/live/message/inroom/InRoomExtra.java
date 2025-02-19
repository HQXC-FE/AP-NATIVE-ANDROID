package com.xtree.live.message.inroom;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class InRoomExtra implements Serializable {

    @SerializedName("list")
    private List<InRoomLink> beans;

    @SerializedName("bottom_pic_url")
    private String bottomPicUrl;

    @SerializedName("guest_register")
    private String guestRegister;

    @SerializedName("user_betsite")
    private String userBetSite;

    public List<InRoomLink> getBeans() {
        return beans;
    }

    public void setBeans(List<InRoomLink> beans) {
        this.beans = beans;
    }

    public String getBottomPicUrl() {
        return bottomPicUrl;
    }

    public void setBottomPicUrl(String bottomPicUrl) {
        this.bottomPicUrl = bottomPicUrl;
    }

    public String getGuestRegister() {
        return guestRegister;
    }

    public void setGuestRegister(String guestRegister) {
        this.guestRegister = guestRegister;
    }

    public String getUserBetSite() {
        return userBetSite;
    }

    public void setUserBetSite(String userBetSite) {
        this.userBetSite = userBetSite;
    }
}
