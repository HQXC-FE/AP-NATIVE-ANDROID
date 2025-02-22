package com.xtree.live.data.source.httpnew;

import com.xtree.live.data.source.response.LiveRoomBean;

import io.reactivex.Flowable;

public interface LiveDataSource {
    Flowable<LiveRoomBean> getRoomInfo(int uid,String channelCode);
}
