package com.xtree.bet.bean.response.im;

import android.os.Parcel;
import android.os.Parcelable;

import com.xtree.base.vo.BaseBean;

public class OddsList implements BaseBean {

    private int oddsType;
    private OddsValues oddsValues;

    public OddsList() {
    }

    protected OddsList(Parcel in) {
        oddsType = in.readInt();
        oddsValues = in.readParcelable(OddsValues.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(oddsType);
        dest.writeParcelable(oddsValues, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OddsList> CREATOR = new Creator<OddsList>() {
        @Override
        public OddsList createFromParcel(Parcel in) {
            return new OddsList(in);
        }

        @Override
        public OddsList[] newArray(int size) {
            return new OddsList[size];
        }
    };

    // Getter & Setter
    public int getOddsType() {
        return oddsType;
    }

    public void setOddsType(int oddsType) {
        this.oddsType = oddsType;
    }

    public OddsValues getOddsValues() {
        return oddsValues;
    }

    public void setOddsValues(OddsValues oddsValues) {
        this.oddsValues = oddsValues;
    }
}

