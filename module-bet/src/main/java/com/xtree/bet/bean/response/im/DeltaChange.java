package com.xtree.bet.bean.response.im;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

public  class DeltaChange implements BaseBean {

    public long EventId;
    public int Action;
    public int SportId;
    public int EventTypeId;
    public String Value;

    public DeltaChange() {}

    protected DeltaChange(Parcel in) {
        EventId = in.readLong();
        Action = in.readInt();
        SportId = in.readInt();
        EventTypeId = in.readInt();
        Value = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(EventId);
        dest.writeInt(Action);
        dest.writeInt(SportId);
        dest.writeInt(EventTypeId);
        dest.writeString(Value);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DeltaChange> CREATOR = new Creator<DeltaChange>() {
        @Override
        public DeltaChange createFromParcel(Parcel in) {
            return new DeltaChange(in);
        }

        @Override
        public DeltaChange[] newArray(int size) {
            return new DeltaChange[size];
        }
    };
}