package com.xtree.bet.bean.response.im;


import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

import java.util.List;


public class EventListRsp implements BaseBean {
    private List<Sport> Sports;
    private Long TempDelta;
    private Long Delta;
    private String ServerTime;

    protected EventListRsp(Parcel in) {
        Sports = in.createTypedArrayList(Sport.CREATOR);
        if (in.readByte() == 0) {
            TempDelta = null;
        } else {
            TempDelta = in.readLong();
        }
        Delta = in.readLong();
        ServerTime = in.readString();
    }

    public static final Creator<EventListRsp> CREATOR = new Creator<EventListRsp>() {
        @Override
        public EventListRsp createFromParcel(Parcel in) {
            return new EventListRsp(in);
        }

        @Override
        public EventListRsp[] newArray(int size) {
            return new EventListRsp[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(Sports);
        if (TempDelta == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(TempDelta);
        }
        dest.writeLong(Delta);
        dest.writeString(ServerTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getter / Setter 略，可使用 Lombok 或生成
}
