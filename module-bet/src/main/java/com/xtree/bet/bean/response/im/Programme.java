package com.xtree.bet.bean.response.im;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

public class Programme implements BaseBean {

    public int programmeId;
    public String programmeName;
    public int count;
    public int earlyFECount;
    public int todayFECount;
    public int rbfeCount;
    public int orCount;
    public boolean isHasLive;

    public Programme() {}

    protected Programme(Parcel in) {
        programmeId = in.readInt();
        programmeName = in.readString();
        count = in.readInt();
        earlyFECount = in.readInt();
        todayFECount = in.readInt();
        rbfeCount = in.readInt();
        orCount = in.readInt();
        isHasLive = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(programmeId);
        dest.writeString(programmeName);
        dest.writeInt(count);
        dest.writeInt(earlyFECount);
        dest.writeInt(todayFECount);
        dest.writeInt(rbfeCount);
        dest.writeInt(orCount);
        dest.writeByte((byte) (isHasLive ? 1 : 0));
    }

    public void readFromParcel(Parcel source) {
        programmeId = source.readInt();
        programmeName = source.readString();
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

    public static final Creator<Programme> CREATOR = new Creator<Programme>() {
        @Override
        public Programme createFromParcel(Parcel in) {
            return new Programme(in);
        }

        @Override
        public Programme[] newArray(int size) {
            return new Programme[size];
        }
    };
}
