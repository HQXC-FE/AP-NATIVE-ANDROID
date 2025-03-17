package com.xtree.live.data.source.httpnew;

import com.google.gson.JsonElement;
import com.xtree.base.net.live.X9LiveInfo;
import com.xtree.live.SPKey;
import com.xtree.live.data.AdsBean;
import com.xtree.live.data.source.response.LiveRoomBean;
import com.xtree.live.data.source.response.LiveTokenResponse;
import com.xtree.live.data.source.response.SearchChatRoomInfo;
import com.xtree.live.message.ChatRoomInfo;
import com.xtree.live.message.ChatRoomPin;
import com.xtree.live.message.MessageRecord;
import com.xtree.live.message.SystemMessageRecord;
import com.xtree.live.message.inroom.InRoomData;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import me.xtree.mvvmhabit.http.BaseResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface LiveService {

    //获取token
    @POST("api/ThirdUser/ThirdLogin")
    Flowable<BaseResponse<LiveTokenResponse>> getXLiveToken(@Body RequestBody body);
    // 获取直播间详情
    @GET("api/li_stre/getRoomInfo")
    Flowable<BaseResponse<LiveRoomBean>> getRoomInfo(@Query("uid") int uid, @Query("channel_code") String code);

//    直播间渠道公告
    @POST("api/chat/pin")
    Flowable<BaseResponse<InRoomData>> pin(@Body RequestBody body);

    // 获取历史消息
    @GET("api/chat/history")
    Flowable<BaseResponse<List<MessageRecord>>> getChatHistory(@Query("room_type") int type, @Query("vid") String vid, @Query("last_id") String lastId, @Query("limit") int limit, @Query(SPKey.CHANNEL_CODE) String code);

    @GET("api/chat/anchorHistory")
    Flowable<BaseResponse<List<MessageRecord>>> getAnchorChatHistory(@Query("anchorId") int uid, @Query("last_id") String lastId, @Query("limit") int limit, @Query(SPKey.CHANNEL_CODE) String code);

    // 获取直播详情
    @GET("api/li_stre/getRoomInfo")
    Flowable<BaseResponse<LiveRoomBean>> getLiveDetail(@Query("uid") int uid, @Query(SPKey.CHANNEL_CODE) String code);

    //直播间进房纪录
    @GET("api/chat/getLiveInRoomLog")
    Flowable<BaseResponse<List<SystemMessageRecord>>> getLiveInroomLog(@Query("vid") String vid, @Query("limit") int limit, @Query(SPKey.CHANNEL_CODE) String code);

    // 获取广告
    @GET("api/advertising_list/getAdList")
    Flowable<BaseResponse<List<AdsBean>>> getAdList(@Query(SPKey.CHANNEL_CODE) String code);

    //主播私聊接口
    @POST("api/chat/sendToAnchor")
    Flowable<BaseResponse<JsonElement>> sendToAnchor(@Body RequestBody body);
    // 主播私聊带图片
    @Multipart
    @POST("api/chat/sendToAnchor")
    Flowable<BaseResponse<JsonElement>> sendToAnchor(@PartMap Map<String, RequestBody> map, @Part MultipartBody.Part file);
    // 助手私聊
    @POST("api/chat/sendToAssistant")
    Flowable<BaseResponse<JsonElement>> sendToAssistant(@Body RequestBody body);
    @Multipart
    @POST("api/chat/sendToAssistant")
    Flowable<BaseResponse<JsonElement>> sendToAssistant(@PartMap Map<String, RequestBody> map,@Part MultipartBody.Part file);
    //聊天室发言
    @POST("api/chat/sendMessage")
    Flowable<BaseResponse<JsonElement>> sendMessage(@Body RequestBody body);
    //聊天室发言
    @Multipart
    @POST("api/chat/sendMessage")
    Flowable<BaseResponse<JsonElement>> sendMessage(@PartMap Map<String, RequestBody> map,@Part MultipartBody.Part file);

    @POST("api/chat/searchAssistant")
    Flowable<BaseResponse<SearchChatRoomInfo>> searchAssistant(@Body RequestBody body);

    @POST("api/chat/getChatRoomList")
    Flowable<BaseResponse<List<ChatRoomInfo>>> getChatRoomList(@Body RequestBody body);

    @POST("api/chat_room/pin")
    Flowable<BaseResponse<ChatRoomPin>> pinChatRoom(@Body RequestBody body);



}
