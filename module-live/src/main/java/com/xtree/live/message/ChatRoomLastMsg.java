package com.xtree.live.message;

import com.google.gson.annotations.SerializedName;

public class ChatRoomLastMsg {
    @SerializedName("text")
    private String text;
    @SerializedName("creation_time")
    private String creationTime;
    @SerializedName("vid")
    private String vid;
    @SerializedName("room_type")
    private int roomType;
    @SerializedName("sender")
    private String sender;
    @SerializedName("sender_name")
    private String senderName;
    @SerializedName("seed")
    private String seed;
    @SerializedName("send_at_ms")
    private long sendAtMs;
    @SerializedName("pic")
    private String pic;


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setRoomType(int roomType) {
        this.roomType = roomType;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public Integer getRoomType() {
        return roomType;
    }

    public void setRoomType(Integer roomType) {
        this.roomType = roomType;
    }


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }


    public String getCreationTime() {
        return creationTime;
    }

    public long getSendAtMs() {
        return sendAtMs;
    }

    public void setSendAtMs(long sendAtMs) {
        this.sendAtMs = sendAtMs;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }
}

