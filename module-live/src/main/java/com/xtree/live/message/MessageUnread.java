package com.xtree.live.message;

import com.google.gson.annotations.SerializedName;

public class MessageUnread extends MessageControl implements MessageVid{

    @SerializedName("unread_count")
    private int unreadCount;//未读消息
    @SerializedName("vid")
    private String vid;
    @SerializedName("room_type")
    private int roomType;//聊天类型 1群聊 2私聊

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public int getRoomType() {
        return roomType;
    }

    public void setRoomType(int roomType) {
        this.roomType = roomType;
    }
}

