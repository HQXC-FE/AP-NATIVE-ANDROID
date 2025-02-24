package com.xtree.live.data.source.httpnew;

import com.xtree.live.data.source.response.LiveRoomBean;
import com.xtree.live.message.inroom.InRoomData;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import me.xtree.mvvmhabit.http.BaseResponse;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LiveService {

    // 获取直播间详情
    @GET("api/li_stre/getRoomInfo")
    Flowable<BaseResponse<LiveRoomBean>> getRoomInfo(@Query("uid") int uid,
                                                     @Query("channel_code") String code);

//    直播间渠道公告
    @POST("api/chat/pin")
    Flowable<BaseResponse<InRoomData>> pin(@Body RequestBody body);

}
