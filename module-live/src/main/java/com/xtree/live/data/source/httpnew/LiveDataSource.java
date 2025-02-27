package com.xtree.live.data.source.httpnew;

import com.xtree.live.data.source.response.LiveRoomBean;
import com.xtree.live.message.MessageRecord;

import java.util.List;

import io.reactivex.Flowable;

public interface LiveDataSource {
    Flowable<LiveRoomBean> getRoomInfo(int uid,String channelCode);

    Flowable<List<MessageRecord>> getChatHistory(int roomType, String vid, String lastId, int limit);

    Flowable<List<MessageRecord>> getAnchorChatHistory(int uid, String lastId, int limit);
}
