package com.xtree.live.data.source.response;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.xtree.live.message.ChatRoomInfo;
import com.xtree.live.message.ChatRoomLastMsg;

public class SearchChatRoomInfo {
    @SerializedName("id")
    private int id;
    @SerializedName("action")
    private String action;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("designation")
    private String designation;
    @SerializedName("designation_color")
    private int designationColor;
    @SerializedName("title")
    private String title;
    @SerializedName("link")
    private String link;
    @SerializedName("msg_id")
    private long msgId;
    @SerializedName("msg_type")
    private int msgType;
    @SerializedName("originalText")
    private String originalText;
    @SerializedName("note")
    private String note;
    @SerializedName("text")
    private String text;
    @SerializedName("pic")
    private String pic;
    @SerializedName("pic_bnc")
    private String picBnc;
    @SerializedName("room_type")
    private int roomType;
    @SerializedName("seed")
    private String seed;
    @SerializedName("sender")
    private int sender;
    @SerializedName("sender_nickname")
    private String senderNickname;
    @SerializedName("sender_user_type")
    private int senderUserType;
    @SerializedName("sender_exp")
    private int senderExp;
    @SerializedName("sender_exp_icon")
    private String senderExpIcon;
    @SerializedName("sender_current_exp")
    private int senderCurrentExp;
    @SerializedName("sender_total_exp")
    private int senderTotalExp;
    @SerializedName("sender_difference")
    private int senderDifference;
    @SerializedName("time")
    private String time;
    @SerializedName("time_ms")
    private long timeMs;
    @SerializedName("type")
    private int type;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("username")
    private String username;
    @SerializedName("user_nickname")
    private String userNickname;
    @SerializedName("vid")
    private String vid;
    @SerializedName("welcome")
    private int welcome;

    public ChatRoomInfo toChatRoomInfo(){
        if(TextUtils.isEmpty(vid))return null;
        ChatRoomInfo roomInfo = new ChatRoomInfo();
        roomInfo.setRoomType(roomType);
        roomInfo.setVid(vid);
        roomInfo.setIsPin(0);
        roomInfo.setPinTime(0);
        roomInfo.setUpdateTime(timeMs / 1000L);

        roomInfo.setUserId(userId);
        roomInfo.setName(userNickname);
        roomInfo.setIsOnline(1);
        roomInfo.setRoomMute(0);
        roomInfo.setPic(avatar);
        roomInfo.setUnread(0);

        ChatRoomLastMsg lastMsg = new ChatRoomLastMsg();
        lastMsg.setRoomType(roomType);
        lastMsg.setSenderName(senderNickname);
        lastMsg.setSender(sender + "");
        lastMsg.setSeed(seed);
        lastMsg.setText(text);
        lastMsg.setVid(vid);
        lastMsg.setPic(pic);
        lastMsg.setCreationTime(time);
        lastMsg.setSendAtMs(timeMs);
        roomInfo.setLastMsg(lastMsg);
        return roomInfo;
    }

    public String getVid() {
        return vid;
    }
}

