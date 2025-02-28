package com.xtree.live.data.source.httpnew;

import com.xtree.live.data.AdsBean;
import com.xtree.live.data.source.response.LiveRoomBean;
import com.xtree.live.message.MessageRecord;
import com.xtree.live.message.SystemMessageRecord;
import com.xtree.live.message.inroom.InRoomData;

import java.util.List;

import io.reactivex.Flowable;
import okhttp3.RequestBody;

public interface LiveDataSource {
    Flowable<LiveRoomBean> getRoomInfo(int uid,String channelCode);

    Flowable<List<MessageRecord>> getChatHistory(int roomType, String vid, String lastId, int limit);

    Flowable<List<MessageRecord>> getAnchorChatHistory(int uid, String lastId, int limit);

    Flowable<LiveRoomBean> getLiveDetail(int uid);

    Flowable<List<SystemMessageRecord>> getLiveInroomLog(String vid, int limit);

    Flowable<InRoomData> pin(RequestBody body);

    Flowable<List<AdsBean>> getAdList();

}
