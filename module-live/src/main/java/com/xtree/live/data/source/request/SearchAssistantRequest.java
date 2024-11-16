package com.xtree.live.data.source.request;


/**
 * /api/chat/searchAssistant
 搜索主播助理
 */
public class SearchAssistantRequest {

    public String nickName ;
    public SearchAssistantRequest(final String nickName){
        this.nickName = nickName;
    }

}
