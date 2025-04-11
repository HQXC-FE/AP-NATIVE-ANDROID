package com.xtree.bet.bean.response.im;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.xtree.base.vo.BaseBean;

import java.util.List;

public class MarketLine implements BaseBean, Parcelable {

    public String marketLineId;
    public String marketType;
    public String handicap;
    public String status;
    public List<Outcome> outcomes;

    public MarketLine() {}

    protected MarketLine(Parcel in) {
        this.marketLineId = in.readString();
        this.marketType = in.readString();
        this.handicap = in.readString();
        this.status = in.readString();
        this.outcomes = in.createTypedArrayList(Outcome.CREATOR);
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(this.marketLineId);
        dest.writeString(this.marketType);
        dest.writeString(this.handicap);
        dest.writeString(this.status);
        dest.writeTypedList(this.outcomes);
    }

    public void readFromParcel(Parcel source) {
        this.marketLineId = source.readString();
        this.marketType = source.readString();
        this.handicap = source.readString();
        this.status = source.readString();
        this.outcomes = source.createTypedArrayList(Outcome.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MarketLine> CREATOR = new Creator<MarketLine>() {
        @Override
        public MarketLine createFromParcel(Parcel source) {
            return new MarketLine(source);
        }

        @Override
        public MarketLine[] newArray(int size) {
            return new MarketLine[size];
        }
    };
}
