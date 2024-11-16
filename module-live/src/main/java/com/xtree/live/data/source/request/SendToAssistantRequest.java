package com.xtree.live.data.source.request;

/**
 * 私聊助手通讯接口，发讯息给助手
 * /api/chat/sendToAssistant
 */
public class SendToAssistantRequest {

    public int assignId;//助理编号
    public String text ;
    public int msgType ;
    public String pic ;
    public String seed ;

}
