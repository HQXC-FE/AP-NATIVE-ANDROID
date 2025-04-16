package com.xtree.bet.bean.response.im;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

import java.util.ArrayList;
import java.util.List;


/**
 * 获取优胜冠军赛事指定比赛,如足球或篮球等
 */

public class OutrightEventRsp implements BaseBean{

    public List<OutrightEvent> InsertUpdateEvent;
    public List<Integer> RemoveEvent;
    public double Delta;
    public String ServerTime;
    public int StatusCode;
    public String StatusDesc;

    public OutrightEventRsp() {}

    protected OutrightEventRsp(Parcel in) {
        InsertUpdateEvent = in.createTypedArrayList(OutrightEvent.CREATOR);
        RemoveEvent = new ArrayList<>();
        Delta = in.readDouble();
        ServerTime = in.readString();
        StatusCode = in.readInt();
        StatusDesc = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(InsertUpdateEvent);
        dest.writeList(RemoveEvent);
        dest.writeDouble(Delta);
        dest.writeString(ServerTime);
        dest.writeInt(StatusCode);
        dest.writeString(StatusDesc);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OutrightEventRsp> CREATOR = new Creator<OutrightEventRsp>() {
        @Override
        public OutrightEventRsp createFromParcel(Parcel in) {
            return new OutrightEventRsp(in);
        }

        @Override
        public OutrightEventRsp[] newArray(int size) {
            return new OutrightEventRsp[size];
        }
    };
}

