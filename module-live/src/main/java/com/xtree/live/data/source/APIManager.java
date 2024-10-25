package com.xtree.live.data.source;

/**
 * Created by KAKA on 2024/3/12.
 * Describe: api 静态管理
 */
public class APIManager {

    public static final String FRONT_LIVES = "/api/front/lives";
    //直播
    private static final String X9_API = "/api/x9/";
    //获取直播token
    public static final String X9_TOKEN_URL = X9_API + "getXLiveToken";
    //聊天房列表
    public static final String CHATROOMLIST_API = "/api/chat/getChatRoomList";

    // 已关注主播列表
    public static final String ATTENTION_API="/api/user/attentionList";
    //获取主播排序列表
    public static final String ANCHOR_SORT_API="/api/anchor/sort";

}
