package com.xtree.live.message;

import com.google.gson.annotations.SerializedName;

public class NewRoomData {

    @SerializedName("last_msg")
    private ChatRoomLastMsg lastMsg;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("pic")
    private String pic;
    @SerializedName("name")
    private String name;
    @SerializedName("channel")
    private String channel;
    @SerializedName("is_online")
    private int isOnline;

    public ChatRoomLastMsg getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(ChatRoomLastMsg lastMsg) {
        this.lastMsg = lastMsg;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(int isOnline) {
        this.isOnline = isOnline;
    }
}

