package com.xtree.bet.bean.response.im;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

public class EventGroupType implements BaseBean {

    public int eventGroupTypeId;
    public int count;
    public int earlyFECount;
    public int todayFECount;
    public int rbfeCount;
    public int orCount;
    public boolean isHasLive;

    public EventGroupType() {}

    protected EventGroupType(Parcel in) {
        eventGroupTypeId = in.readInt();
        count = in.readInt();
        earlyFECount = in.readInt();
        todayFECount = in.readInt();
        rbfeCount = in.readInt();
        orCount = in.readInt();
        isHasLive = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(eventGroupTypeId);
        dest.writeInt(count);
        dest.writeInt(earlyFECount);
        dest.writeInt(todayFECount);
        dest.writeInt(rbfeCount);
        dest.writeInt(orCount);
        dest.writeByte((byte) (isHasLive ? 1 : 0));
    }

    public void readFromParcel(Parcel source) {
        eventGroupTypeId = source.readInt();
        count = source.readInt();
        earlyFECount = source.readInt();
        todayFECount = source.readInt();
        rbfeCount = source.readInt();
        orCount = source.readInt();
        isHasLive = source.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EventGroupType> CREATOR = new Creator<EventGroupType>() {
        @Override
        public EventGroupType createFromParcel(Parcel in) {
            return new EventGroupType(in);
        }

        @Override
        public EventGroupType[] newArray(int size) {
            return new EventGroupType[size];
        }
    };
}
