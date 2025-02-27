package com.xtree.live.data.source.httpnew;

import com.xtree.live.LiveConfig;
import com.xtree.live.data.source.response.LiveRoomBean;
import com.xtree.live.message.MessageRecord;

import java.util.List;

import io.reactivex.Flowable;
import me.xtree.mvvmhabit.utils.RxUtils;

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


}
