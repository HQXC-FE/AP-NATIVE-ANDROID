package com.xtree.bet.bean.response.pm;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

import java.util.ArrayList;
import java.util.List;

public class MatchListRspTemp implements BaseBean {
    private String code;
    private List<MatchInfo> data; // List of integers
    private String msg;
    private long ts;


public List<MatchInfo> getData(){
    return data;
}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeString(this.msg);
        dest.writeTypedList(this.data);
    }

    public void readFromParcel(Parcel source) {
        this.code = source.readString();
        this.msg = source.readString();
        this.data = source.createTypedArrayList(MatchInfo.CREATOR);
    }

    public MatchListRspTemp() {
    }

    protected MatchListRspTemp(Parcel in) {
        this.code = in.readString();
        this.msg = in.readString();
        this.data = in.createTypedArrayList(MatchInfo.CREATOR);
    }

    public static final Creator<MatchListRspTemp> CREATOR = new Creator<MatchListRspTemp>() {
        @Override
        public MatchListRspTemp createFromParcel(Parcel source) {
            return new MatchListRspTemp(source);
        }

        @Override
        public MatchListRspTemp[] newArray(int size) {
            return new MatchListRspTemp[size];
        }
    };
}
