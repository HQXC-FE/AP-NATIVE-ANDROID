package com.xtree.main.data.source;

import com.xtree.base.vo.MsgPersonInfoVo;
import com.xtree.base.vo.WsToken;

import io.reactivex.Flowable;
import com.xtree.base.http.BaseResponse;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MainApiService {

    //==========websocket=============
    @GET("/api/ws/token")
    Flowable<BaseResponse<WsToken>> getWsToken();

    /**
     * 获取 消息详情
     */
    @GET("/api/message/{key}")
    Flowable<BaseResponse<MsgPersonInfoVo>> getMessagePerson(@Path("key") String key);
    //==========websocket=============
}
