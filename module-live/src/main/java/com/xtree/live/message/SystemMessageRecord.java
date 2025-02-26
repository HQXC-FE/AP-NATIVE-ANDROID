package com.xtree.live.message;

import com.google.gson.annotations.SerializedName;

public class SystemMessageRecord {
    @SerializedName("user_id")
    private String userId;
    @SerializedName("create_time")
    private String createTime;
    @SerializedName("channel_code")
    private String channelCode;
    @SerializedName("sender_nickname")
    private String senderNickname;
    @SerializedName("sender_user_type")
    private int senderUserType;
    @SerializedName(value = "sender_exp")
    private int senderExp;
    @SerializedName("has_tag")
    private String hasTag;
    @SerializedName("designation")
    private String designation;
    @SerializedName("designation_color")
    private int designationColor;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getSenderNickname() {
        return senderNickname;
    }

    public void setSenderNickname(String senderNickname) {
        this.senderNickname = senderNickname;
    }

    public int getSenderUserType() {
        return senderUserType;
    }

    public void setSenderUserType(int senderUserType) {
        this.senderUserType = senderUserType;
    }

    public int getSenderExp() {
        return senderExp;
    }

    public void setSenderExp(int senderExp) {
        this.senderExp = senderExp;
    }

    public String getHasTag() {
        return hasTag;
    }

    public void setHasTag(String hasTag) {
        this.hasTag = hasTag;
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
}
