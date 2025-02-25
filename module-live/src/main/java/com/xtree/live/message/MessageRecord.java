package com.xtree.live.message;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.xtree.live.socket.BNC;

import java.util.Objects;

public class MessageRecord implements BNC {
    @SerializedName("action")
    private String action;
    /**
     * 房间vid
     */
    @SerializedName("vid")
    private String vid;
    /**
     * 房间类型 0 直播间  1 群组 2 私聊
     */
    @SerializedName("type")
    private int type;
    /**
     * 房间类型 0 直播间  1 群组 2 私聊
     */
    @SerializedName("room_type")
    private int roomType;
    /**
     * 文本发言
     */
    @SerializedName("text")
    private String text;
    /**
     * 消息类型 1 - 7
     */
    @SerializedName("msg_type")
    private int msgType;
    /**
     * 用户id
     */
    @SerializedName("sender")
    private String sender;
    /**
     * 用户昵称
     */
    @SerializedName("sender_nickname")
    private String senderNickname;
    /**
     * 消息id
     */
    @SerializedName("msg_id")
    private String msgId;
    /**
     * 发送者生产的消息id
     */
    @SerializedName("seed")
    private String seed;
    /**
     * 后台生产的消息id
     */
    @SerializedName("seedsy")
    private String seedsy;
    /**
     * 发送时间
     */
    @SerializedName("time")
    private String time;

    /**
     * 用户头像
     */
    @SerializedName("avatar")
    private String avatar;
    /**
     * 用户发送的图片信息
     */
    @SerializedName("pic")
    private String pic;
    @SerializedName("pic_bnc")
    private String picBnc;
    /**
     * 用户等级
     */
    @SerializedName("sender_exp")
    private int senderExp;
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

    /**
     * banner title
     */
    @SerializedName("title")
    private String title;

    /**
     * banner 链接
     */
    @SerializedName("link")
    private String link;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("sender_user_type")
    private int senderType;

    @SerializedName("readed_count")
    private int readCount;


    public String getVid() {
        return TextUtils.isEmpty(vid) ? "" : vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public @RoomType int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return TextUtils.isEmpty(text) ? "" : text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getSender() {
        return TextUtils.isEmpty(sender) ? "" : sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public @NonNull String getSenderNickname() {
        return TextUtils.isEmpty(senderNickname) ? "" : senderNickname.trim();
    }

    public void setSenderNickname(String senderNickname) {
        this.senderNickname = senderNickname;
    }

    public String getMsgId() {
        return TextUtils.isEmpty(msgId) ? "" : msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getSeed() {
        return TextUtils.isEmpty(seed) ? "" : seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public String getSeedsy() {
        return TextUtils.isEmpty(seedsy) ? "" : seedsy;
    }

    public void setSeedsy(String seedsy) {
        this.seedsy = seedsy;
    }

    public String getTime() {
        return TextUtils.isEmpty(time) ? "" : time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAvatar() {
        return TextUtils.isEmpty(avatar) ? "" : avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPic() {
        return TextUtils.isEmpty(pic) ? "" : pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getSenderExp() {
        return senderExp;
    }

    public void setSenderExp(int senderExp) {
        this.senderExp = senderExp;
    }

    public String getDesignation() {
        return TextUtils.isEmpty(designation) ? "" : designation;
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

    public String getTitle() {
        return TextUtils.isEmpty(title) ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return TextUtils.isEmpty(link) ? "" : link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }

        MessageRecord bean = (MessageRecord) object;
        return !TextUtils.isEmpty(seed) && Objects.equals(seed, bean.seed) || !TextUtils.isEmpty(seedsy) && Objects.equals(seedsy, bean.seedsy);
    }

    public String getUserId() {
        return TextUtils.isEmpty(userId) ? "" : userId;
    }

    public String getAction() {
        return TextUtils.isEmpty(action) ? "" : action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getSenderType() {
        return senderType;
    }

    public void setSenderType(int senderType) {
        this.senderType = senderType;
    }

    public int getReadCount() {
        return this.readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
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
        return TextUtils.isEmpty(picBnc) ? TextUtils.isEmpty(pic) ? "" : pic : picBnc;
    }

    @NonNull
    @Override
    public String objectKey() {
        return imagePath().replace(BNC.SUFFIX, "");
    }
}
