package com.xtree.live.message;

import com.google.gson.annotations.SerializedName;

public class MessageSystem extends MessageChat {
    @SerializedName("userid")
    private String userid;
    @SerializedName("msg_type")
    private int msgType;
    @SerializedName("text")
    private String text;
    @SerializedName("time")
    private String time;
    @SerializedName("channel")
    private String channel;
    @SerializedName("designation")
    private String designation;
    @SerializedName("designation_color")
    private int designationColor;
    @SerializedName("has_tag")
    private int hasTag;
    @SerializedName("sender_total_exp")
    private int senderTotalExp;
    @SerializedName("sender_current_exp")
    private int senderCurrentExp;
    @SerializedName("sender_exp")
    private int senderExp;
    @SerializedName("sender_difference")
    private int senderDifference;
    @SerializedName("sender_exp_icon")
    private String senderExpIcon;


    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public int getDesignationColor() {
        return designationColor;
    }

    public void setDesignationColor(int designationColor) {
        this.designationColor = designationColor;
    }

    public int getHasTag() {
        return hasTag;
    }

    public void setHasTag(int hasTag) {
        this.hasTag = hasTag;
    }

    public int getSenderTotalExp() {
        return senderTotalExp;
    }

    public void setSenderTotalExp(int senderTotalExp) {
        this.senderTotalExp = senderTotalExp;
    }

    public int getSenderCurrentExp() {
        return senderCurrentExp;
    }

    public void setSenderCurrentExp(int senderCurrentExp) {
        this.senderCurrentExp = senderCurrentExp;
    }

    public int getSenderExp() {
        return senderExp;
    }

    public void setSenderExp(int senderExp) {
        this.senderExp = senderExp;
    }

    public int getSenderDifference() {
        return senderDifference;
    }

    public void setSenderDifference(int senderDifference) {
        this.senderDifference = senderDifference;
    }

    public String getSenderExpIcon() {
        return senderExpIcon;
    }

    public void setSenderExpIcon(String senderExpIcon) {
        this.senderExpIcon = senderExpIcon;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
