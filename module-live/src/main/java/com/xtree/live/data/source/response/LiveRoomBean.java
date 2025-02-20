package com.xtree.live.data.source.response;

import com.google.gson.annotations.SerializedName;

public class LiveRoomBean {
    @SerializedName("info")
    private LiveRoomInfoBean info;

    @SerializedName("userData")
    private LiveUserBean userData;

    @SerializedName("vid")
    private String vid;

    public LiveRoomInfoBean getInfo() {
        return info;
    }

    public void setInfo(LiveRoomInfoBean info) {
        this.info = info;
    }

    public LiveUserBean getUserData() {
        return userData;
    }

    public void setUserData(LiveUserBean userData) {
        this.userData = userData;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }
}
