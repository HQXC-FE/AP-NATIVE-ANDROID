package com.xtree.live.data.source.httpnew;

import com.xtree.live.data.source.response.LiveRoomBean;

import io.reactivex.Flowable;
import me.xtree.mvvmhabit.http.BaseResponse;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LiveService {

    // 获取直播间详情
    @GET("api/li_stre/getRoomInfo")
    Flowable<BaseResponse<LiveRoomBean>> getRoomInfo(@Query("uid") int uid,
                                                     @Query("channel_code") String code);


}
