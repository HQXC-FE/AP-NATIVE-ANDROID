package com.xtree.live.data.source.httpnew;

import com.google.gson.JsonElement;
import com.xtree.live.LiveConfig;
import com.xtree.live.data.AdsBean;
import com.xtree.live.data.source.response.LiveRoomBean;
import com.xtree.live.message.MessageRecord;
import com.xtree.live.message.SystemMessageRecord;
import com.xtree.live.message.inroom.InRoomData;

import java.util.List;

import io.reactivex.Flowable;
import me.xtree.mvvmhabit.utils.RxUtils;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class LiveRep extends BaseRepository implements LiveDataSource {

    private static LiveRep mInstance;

    private LiveRep() {
    }

    public static LiveRep getInstance() {
        if (mInstance == null) {
            synchronized (LiveRep.class) {
                mInstance = new LiveRep();
            }
        }
        return mInstance;
    }


    @Override
    public Flowable<LiveRoomBean> getRoomInfo(int uid, String channelCode) {

        return obtainJsonService(LiveService.class)
                .getRoomInfo(uid, channelCode)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer());
    }

    @Override
    public Flowable<List<MessageRecord>> getChatHistory(int roomType, String vid, String lastId, int limit) {

        String channelCode = LiveConfig.getChannelCode();
        return obtainJsonService(LiveService.class)
                .getChatHistory(roomType, vid, lastId, limit, channelCode)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer());
    }

    @Override
    public Flowable<List<MessageRecord>> getAnchorChatHistory(int uid, String lastId, int limit) {
        String channelCode = LiveConfig.getChannelCode();
        return obtainJsonService(LiveService.class)
                .getAnchorChatHistory(uid, lastId, limit, channelCode)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer());
    }

    @Override
    public Flowable<LiveRoomBean> getLiveDetail(int uid) {
        String channelCode = LiveConfig.getChannelCode();
        return obtainJsonService(LiveService.class)
                .getLiveDetail(uid, channelCode)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer());
    }

    @Override
    public Flowable<List<SystemMessageRecord>> getLiveInroomLog(String vid, int limit) {
        String channelCode = LiveConfig.getChannelCode();
        return obtainJsonService(LiveService.class)
                .getLiveInroomLog(vid, limit,channelCode)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer());
    }

    @Override
    public Flowable<InRoomData> pin(RequestBody body) {

        return obtainJsonService(LiveService.class)
                .pin(body)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer());
    }

    @Override
    public Flowable<List<AdsBean>> getAdList() {
        String channelCode = LiveConfig.getChannelCode();
        return obtainJsonService(LiveService.class)
                .getAdList(channelCode)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer());
    }

    @Override
    public Flowable<JsonElement> sendToAnchor(RequestBody body) {

        return obtainJsonService(LiveService.class)
                .sendToAnchor(body)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer());
    }

    @Override
    public Flowable<JsonElement> sendToAnchor(RequestBody body,MultipartBody.Part file) {

        return obtainJsonService(LiveService.class)
                .sendToAnchor(body,file)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer());
    }

    @Override
    public Flowable<JsonElement> sendToAssistant(RequestBody body) {

        return obtainJsonService(LiveService.class)
                .sendToAssistant(body)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer());
    }
    @Override
    public Flowable<JsonElement> sendToAssistant(RequestBody body,MultipartBody.Part file) {

        return obtainJsonService(LiveService.class)
                .sendToAssistant(body,file)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer());
    }

    @Override
    public Flowable<JsonElement> sendMessage(RequestBody body) {

        return obtainJsonService(LiveService.class)
                .sendMessage(body)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer());
    }
    @Override
    public Flowable<JsonElement> sendMessage(RequestBody body,MultipartBody.Part file) {

        return obtainJsonService(LiveService.class)
                .sendMessage(body,file)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer());
    }


}
