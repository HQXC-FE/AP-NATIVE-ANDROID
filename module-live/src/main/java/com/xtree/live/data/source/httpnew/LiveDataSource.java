package com.xtree.live.data.source.httpnew;

import com.google.gson.JsonElement;
import com.xtree.base.net.live.X9LiveInfo;
import com.xtree.live.data.AdsBean;
import com.xtree.live.data.source.response.LiveRoomBean;
import com.xtree.live.data.source.response.LiveTokenResponse;
import com.xtree.live.data.source.response.SearchChatRoomInfo;
import com.xtree.live.message.ChatRoomInfo;
import com.xtree.live.message.ChatRoomPin;
import com.xtree.live.message.MessageRecord;
import com.xtree.live.message.SystemMessageRecord;
import com.xtree.live.message.inroom.InRoomData;
import com.xtree.live.model.GiftBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public interface LiveDataSource {



    Flowable<InRoomData> pin(RequestBody body);

    Flowable<List<AdsBean>> getAdList();

    Flowable<JsonElement> sendToAnchor(RequestBody body);
    Flowable<JsonElement> sendToAnchor(Map<String, RequestBody> body, MultipartBody.Part file);

    Flowable<JsonElement> sendToAssistant(RequestBody body);
    Flowable<JsonElement> sendToAssistant(Map<String, RequestBody> body, MultipartBody.Part file);

    Flowable<JsonElement> sendMessage(RequestBody body);
    Flowable<JsonElement> sendMessage(Map<String, RequestBody> body, MultipartBody.Part file);

    Flowable<SearchChatRoomInfo> searchAssistant(RequestBody body);

    Flowable<List<ChatRoomInfo>> getChatRoomList(RequestBody body);

    Flowable<ChatRoomPin> pinChatRoom(RequestBody body);
    Flowable<List<GiftBean>> getGiftList();
    Flowable<JsonElement> readMessage(String vid,String msgId);

}
