package com.xtree.bet.bean.response.im;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.xtree.base.vo.BaseBean;

public class Outcome implements BaseBean, Parcelable {

    public String outcomeId;
    public String outcomeName;
    public String odds;
    public String oddsType;
    public String oddsValues;

    public Outcome() {}

    protected Outcome(Parcel in) {
        this.outcomeId = in.readString();
        this.outcomeName = in.readString();
        this.odds = in.readString();
        this.oddsType = in.readString();
        this.oddsValues = in.readString();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(this.outcomeId);
        dest.writeString(this.outcomeName);
        dest.writeString(this.odds);
        dest.writeString(this.oddsType);
        dest.writeString(this.oddsValues);
    }

    public void readFromParcel(Parcel source) {
        this.outcomeId = source.readString();
        this.outcomeName = source.readString();
        this.odds = source.readString();
        this.oddsType = source.readString();
        this.oddsValues = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Outcome> CREATOR = new Creator<Outcome>() {
        @Override
        public Outcome createFromParcel(Parcel source) {
            return new Outcome(source);
        }

        @Override
        public Outcome[] newArray(int size) {
            return new Outcome[size];
        }
    };
}

