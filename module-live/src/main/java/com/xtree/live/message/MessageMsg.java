package com.xtree.live.message;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.xtree.live.socket.BNC;

public class MessageMsg extends MessageChat implements BNC {

    @SerializedName("msg_id")
    private String msgId;
    @SerializedName("text")
    private String text;
    @SerializedName("token")
    private String token;
    @SerializedName("msg_type")
    private int msgType;
    @SerializedName("sender")
    private String sender;

    /**
     * 0 游客
     * 1 助理
     * 2 普通注册用户
     */
    @SerializedName("sender_user_type")
    private int senderUserType;
    @SerializedName("sender_nickname")
    private String senderNickname;
    @SerializedName("time")
    private String time;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("channel")
    private String channel;
    @SerializedName("has_tag")
    private int hasTag;
    @SerializedName("note")
    private String note;
    @SerializedName("designation")
    private String designation;
    @SerializedName("designation_color")
    private int designationColor;
    @SerializedName("readed_count")
    private int readedCount;
    @SerializedName("is_batch")
    private int isBatch;
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

    @SerializedName("pic")
    private String pic;
    @SerializedName("pic_bnc")
    private String picBnc;

    @SerializedName("link")
    private String link;

    @SerializedName("title")
    private String title;
    @SerializedName("isVir")
    protected  int isVir;

    /**
     * 机器人评论才有这个字段
     */
    @SerializedName("comment")
    private String comment;

    public int getIsVir() {
        return isVir;
    }

    public void setIsVir(int isVir) {
        this.isVir = isVir;
    }
    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderNickname() {
        return senderNickname;
    }

    public void setSenderNickname(String senderNickname) {
        this.senderNickname = senderNickname;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }


    public int getHasTag() {
        return hasTag;
    }

    public void setHasTag(int hasTag) {
        this.hasTag = hasTag;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public int getReadedCount() {
        return readedCount;
    }

    public void setReadedCount(int readedCount) {
        this.readedCount = readedCount;
    }

    public int getIsBatch() {
        return isBatch;
    }

    public void setIsBatch(int isBatch) {
        this.isBatch = isBatch;
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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSenderUserType() {
        return senderUserType;
    }

    public void setSenderUserType(int senderUserType) {
        this.senderUserType = senderUserType;
    }

    public String getComment() {
        return comment;
    }


    public String getPicBnc() {
        return picBnc;
    }

    public void setPicBnc(String picBnc) {
        this.picBnc = picBnc;
    }

    @NonNull
    @Override
    public String imagePath() {
        return TextUtils.isEmpty(picBnc) ? pic : picBnc;
    }

    @NonNull
    @Override
    public String objectKey() {
        return imagePath().replace(BNC.SUFFIX, "");
    }
}
