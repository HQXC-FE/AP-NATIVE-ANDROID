package com.xtree.live.message;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

public class Recipient {
    private String id;
    private int level;

    private String nickname;

    private String profileAvatar;
    // 0 游客 1 admin 2 会员
    private int userType;


    /**
     * 头衔文字
     */
    @SerializedName("designation")

    private String designation;
    /**
     * 头衔颜色
     */
    @SerializedName("designation_color")
    private int designationColor;


    public Recipient(MessageRecord record) {
        this.id = !TextUtils.isEmpty(record.getSender()) ? record.getSender() : !TextUtils.isEmpty(record.getUserId()) ? record.getUserId() : "";
        this.level = record.getSenderExp();
        this.userType = record.getSenderType();
        this.nickname = !TextUtils.isEmpty(record.getSenderNickname()) ? record.getSenderNickname() : "";
        this.profileAvatar = !TextUtils.isEmpty(record.getAvatar()) ? record.getAvatar() : "";
        this.designation = !TextUtils.isEmpty(record.getDesignation()) ? record.getDesignation() : "";
        this.designationColor = record.getDesignationColor();
    }


    public String getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public String getNickname() {
        return nickname;
    }

    public String getProfileAvatar() {
        return profileAvatar;
    }

    public String getDesignation() {
        return designation;
    }

    public int getDesignationColor() {
        return designationColor;
    }

    public @UserType int getUserType() {
        return userType;
    }
}

