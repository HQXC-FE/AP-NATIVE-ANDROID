package com.xtree.live.message;

import com.google.gson.annotations.SerializedName;

public class ChatRoomInfo extends ChatRoomPin {

    @SerializedName(value = "name")
    private String name;
    @SerializedName("room_type")
    private int roomType;//0直播间列表 ，1群聊列表 2私聊列表
    @SerializedName("pic")
    private String pic;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("anchor_id")
    private int anchorId;
    @SerializedName("anchor_name")
    private String anchorName;
    @SerializedName("last_msg")
    private ChatRoomLastMsg lastMsg;
    @SerializedName("room_mute")
    private int roomMute;
    @SerializedName("is_online")
    private int isOnline;
    @SerializedName("unread")
    private int unread;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRoomType() {
        return roomType;
    }

    public void setRoomType(int roomType) {
        this.roomType = roomType;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(int anchorId) {
        this.anchorId = anchorId;
    }

    public String getAnchorName() {
        return anchorName;
    }

    public void setAnchorName(String anchorName) {
        this.anchorName = anchorName;
    }

    public ChatRoomLastMsg getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(ChatRoomLastMsg lastMsg) {
        this.lastMsg = lastMsg;
    }

    public int getRoomMute() {
        return roomMute;
    }

    public void setRoomMute(int roomMute) {
        this.roomMute = roomMute;
    }

    public int getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(int isOnline) {
        this.isOnline = isOnline;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }
}

