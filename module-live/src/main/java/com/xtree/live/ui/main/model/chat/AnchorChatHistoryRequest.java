package com.xtree.live.ui.main.model.chat;

public class AnchorChatHistoryRequest {
    private int anchorId;
    private String last_id;
    private int limit;
    private String channel_code;

    public AnchorChatHistoryRequest(int uid, String lastId, int limit, String channelCode) {
        this.anchorId = uid;
        this.last_id = lastId;
        this.limit = limit;
        this.channel_code = channelCode;
    }

    public int getUid() {
        return anchorId;
    }

    public void setUid(int uid) {
        this.anchorId = uid;
    }

    public String getLastId() {
        return last_id;
    }

    public void setLastId(String lastId) {
        this.last_id = lastId;
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
