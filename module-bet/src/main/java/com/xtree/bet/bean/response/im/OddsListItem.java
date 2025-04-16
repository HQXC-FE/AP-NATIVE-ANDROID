package com.xtree.bet.bean.response.im;

import android.os.Parcel;
import android.os.Parcelable;

import com.xtree.base.vo.BaseBean;

public  class OddsListItem implements BaseBean {
    public int OddsType;
    public OddsValues OddsValues;

    protected OddsListItem(Parcel in) {
        OddsType = in.readInt();
        OddsValues = in.readParcelable(OddsValues.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(OddsType);
        dest.writeParcelable(OddsValues, flags);
    }

    public static final Creator<OddsListItem> CREATOR = new Creator<OddsListItem>() {
        @Override
        public OddsListItem createFromParcel(Parcel in) {
            return new OddsListItem(in);
        }

        @Override
        public OddsListItem[] newArray(int size) {
            return new OddsListItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}
