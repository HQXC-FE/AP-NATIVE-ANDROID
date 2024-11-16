package com.xtree.live.data.source.request;

/**
 * /api/chat/getChatRoomList 请求对象
 */

public class ChatRoomListRequest {
    public String type ;//房间类型 0 - 直播间列表; 1 - 群组聊天列表; 2 - 私聊列表; 1,2 - 组合群聊和私聊的房间列表
    public int page ;   //页数
    public int limit ;//笔数

    public ChatRoomListRequest(){
        this.type = "0" ;
        this.page = 1 ;
        this.limit = 100;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
