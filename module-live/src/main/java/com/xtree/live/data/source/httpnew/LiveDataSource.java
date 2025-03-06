package com.xtree.live.data.source.httpnew;

import com.google.gson.JsonElement;
import com.xtree.live.data.AdsBean;
import com.xtree.live.data.source.response.LiveRoomBean;
import com.xtree.live.data.source.response.SearchChatRoomInfo;
import com.xtree.live.message.ChatRoomInfo;
import com.xtree.live.message.ChatRoomPin;
import com.xtree.live.message.MessageRecord;
import com.xtree.live.message.SystemMessageRecord;
import com.xtree.live.message.inroom.InRoomData;

import java.util.List;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public interface LiveDataSource {
    Flowable<LiveRoomBean> getRoomInfo(int uid,String channelCode);

    Flowable<List<MessageRecord>> getChatHistory(int roomType, String vid, String lastId, int limit);

    Flowable<List<MessageRecord>> getAnchorChatHistory(int uid, String lastId, int limit);

    Flowable<LiveRoomBean> getLiveDetail(int uid);

    Flowable<List<SystemMessageRecord>> getLiveInroomLog(String vid, int limit);

    Flowable<InRoomData> pin(RequestBody body);

    Flowable<List<AdsBean>> getAdList();

    Flowable<JsonElement> sendToAnchor(RequestBody body);
    Flowable<JsonElement> sendToAnchor(RequestBody body, MultipartBody.Part file);

    Flowable<JsonElement> sendToAssistant(RequestBody body);
    Flowable<JsonElement> sendToAssistant(RequestBody body,MultipartBody.Part file);

    Flowable<JsonElement> sendMessage(RequestBody body);
    Flowable<JsonElement> sendMessage(RequestBody body,MultipartBody.Part file);

    Flowable<SearchChatRoomInfo> searchAssistant(RequestBody body);

    Flowable<List<ChatRoomInfo>> getChatRoomList(RequestBody body);

    Flowable<ChatRoomPin> pinChatRoom(RequestBody body);


}
