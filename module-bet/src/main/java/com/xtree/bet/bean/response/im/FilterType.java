package com.xtree.bet.bean.response.im;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

public class FilterType implements BaseBean {

    public int filterTypeId;
    public String filterTypeName;
    public int count;
    public int earlyFECount;
    public int todayFECount;
    public int rbfeCount;
    public int orCount;
    public boolean isHasLive;

    public FilterType() {}

    protected FilterType(Parcel in) {
        filterTypeId = in.readInt();
        filterTypeName = in.readString();
        count = in.readInt();
        earlyFECount = in.readInt();
        todayFECount = in.readInt();
        rbfeCount = in.readInt();
        orCount = in.readInt();
        isHasLive = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(filterTypeId);
        dest.writeString(filterTypeName);
        dest.writeInt(count);
        dest.writeInt(earlyFECount);
        dest.writeInt(todayFECount);
        dest.writeInt(rbfeCount);
        dest.writeInt(orCount);
        dest.writeByte((byte) (isHasLive ? 1 : 0));
    }

    public void readFromParcel(Parcel source) {
        filterTypeId = source.readInt();
        filterTypeName = source.readString();
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

    public static final Creator<FilterType> CREATOR = new Creator<FilterType>() {
        @Override
        public FilterType createFromParcel(Parcel in) {
            return new FilterType(in);
        }

        @Override
        public FilterType[] newArray(int size) {
            return new FilterType[size];
        }
    };
}
