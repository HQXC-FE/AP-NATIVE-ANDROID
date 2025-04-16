package com.xtree.bet.bean.response.im;

import android.os.Parcel;
import android.os.Parcelable;

import com.xtree.base.vo.BaseBean;

import java.util.List;

public class WagerListRsp implements BaseBean, Parcelable {

    public List<Wager> WagerList;

    public WagerListRsp() {}

    protected WagerListRsp(Parcel in) {
        WagerList = in.createTypedArrayList(Wager.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(WagerList);
    }

    public void readFromParcel(Parcel source) {
        WagerList = source.createTypedArrayList(Wager.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WagerListRsp> CREATOR = new Creator<WagerListRsp>() {
        @Override
        public WagerListRsp createFromParcel(Parcel in) {
            return new WagerListRsp(in);
        }

        @Override
        public WagerListRsp[] newArray(int size) {
            return new WagerListRsp[size];
        }
    };
}

