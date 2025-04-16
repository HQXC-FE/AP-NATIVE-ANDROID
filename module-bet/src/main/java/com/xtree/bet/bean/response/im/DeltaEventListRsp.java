package com.xtree.bet.bean.response.im;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

import java.util.List;

public class DeltaEventListRsp implements BaseBean {

    public List<DeltaChange> DeltaChanges;
    public double Delta;
    public String ServerTime;
    public int StatusCode;
    public String StatusDesc;

    public DeltaEventListRsp() {}

    protected DeltaEventListRsp(Parcel in) {
        DeltaChanges = in.createTypedArrayList(DeltaChange.CREATOR);
        Delta = in.readDouble();
        ServerTime = in.readString();
        StatusCode = in.readInt();
        StatusDesc = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(DeltaChanges);
        dest.writeDouble(Delta);
        dest.writeString(ServerTime);
        dest.writeInt(StatusCode);
        dest.writeString(StatusDesc);
    }

    @Override
    public int describeContents() {
        return 0;
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
}
