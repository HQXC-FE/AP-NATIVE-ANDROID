package com.xtree.live.data.source.httpnew;

import com.xtree.live.data.source.response.LiveRoomBean;
import com.xtree.live.message.inroom.InRoomData;

import io.reactivex.Flowable;
import me.xtree.mvvmhabit.utils.RxUtils;

public class LiveRep extends BaseRepository implements LiveDataSource{

    private static LiveRep mInstance;
    private LiveRep(){}

    public static LiveRep getInstance(){
        if(mInstance==null){
            synchronized (LiveRep.class){
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


}
