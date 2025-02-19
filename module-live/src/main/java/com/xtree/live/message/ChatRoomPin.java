package com.xtree.live.message;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class ChatRoomPin {
    @SerializedName("is_pin")
    private int isPin;
    @SerializedName("pin_time")
    private int pinTime;
    @SerializedName("updatetime")
    private long updateTime;
    @SerializedName("vid")
    private String vid;

    public int getIsPin() {
        return isPin;
    }

    public void setIsPin(int isPin) {
        this.isPin = isPin;
    }

    public int getPinTime() {
        return pinTime;
    }

    public void setPinTime(int pinTime) {
        this.pinTime = pinTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj == this)return true;
        return obj instanceof  ChatRoomInfo && Objects.equals(getVid(), ((ChatRoomInfo) obj).getVid());
    }
}
