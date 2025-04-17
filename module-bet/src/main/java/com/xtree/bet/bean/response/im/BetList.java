package com.xtree.bet.bean.response.im;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

import java.util.List;

public class BetList implements BaseBean {

    public List<Wager> WagerList;

    public int Page;

    public int PageSize;


    protected BetList(Parcel in) {
        WagerList = in.createTypedArrayList(Wager.CREATOR);
        Page = in.readInt();
        PageSize = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(WagerList);

        dest.writeInt(Page);
        dest.writeInt(PageSize);
    }

    public static final Creator<BetList> CREATOR = new Creator<BetList>() {
        @Override
        public BetList createFromParcel(Parcel in) {
            return new BetList(in);
        }

        @Override
        public BetList[] newArray(int size) {
            return new BetList[size];
        }
    };



    @Override
    public int describeContents() {
        return 0;
    }

}
