package com.xtree.live.message.inroom;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class InRoomData implements Serializable {

    @SerializedName("pinData")
    private String pinData;

    @SerializedName("day")
    private String day;

    @SerializedName("pinType")
    private int pinType;

    @SerializedName("chat_inroom_mode")
    private String chatInroomMode;
    @SerializedName("room_mute")
    private int room_mute;
    @SerializedName("extra")
    private InRoomExtra extra;

    @SerializedName("uid")
    private String uid;
    @SerializedName("pinObj")
    private JsonElement pinObj;

    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPinData() {
        return pinData;
    }

    public void setPinData(String pinData) {
        this.pinData = pinData;
    }

    public int getPinType() {
        return pinType;
    }

    public void setPinType(int pinType) {
        this.pinType = pinType;
    }

    public String getChatInroomMode() {
        return chatInroomMode;
    }

    public void setChatInroomMode(String chatInroomMode) {
        this.chatInroomMode = chatInroomMode;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public InRoomExtra getExtra() {
        return extra;
    }

    public void setExtra(InRoomExtra extra) {
        this.extra = extra;
    }

    public JsonElement getPinObj() {
        return pinObj;
    }

    public void setPinObj(JsonElement pinObj) {
        this.pinObj = pinObj;
    }

    public int getRoom_mute() {
        return room_mute;
    }

    public void setRoom_mute(int room_mute) {
        this.room_mute = room_mute;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
