package com.xtree.live.ui.main.model.chat;

public class GetChatHistroyRequest {
    private int room_type;
    private String vid;
    private String last_id;
    private int limit;
    private String channel_code;

    public GetChatHistroyRequest(int room_type, String vid, String last_id, int limit, String channel_code) {
        this.room_type = room_type;
        this.vid = vid;
        this.last_id = last_id;
        this.limit = limit;
        this.channel_code = channel_code;
    }

    public GetChatHistroyRequest(String vid, int limit, String channel_code) {
        this.vid = vid;
        this.limit = limit;
        this.channel_code = channel_code;
    }

    public int getRoom_type() {
        return room_type;
    }

    public void setRoom_type(int room_type) {
        this.room_type = room_type;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getLast_id() {
        return last_id;
    }

    public void setLast_id(String last_id) {
        this.last_id = last_id;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getChannel_code() {
        return channel_code;
    }

    public void setChannel_code(String channel_code) {
        this.channel_code = channel_code;
    }
}
