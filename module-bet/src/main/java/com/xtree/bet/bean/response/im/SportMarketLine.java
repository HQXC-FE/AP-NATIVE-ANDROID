package com.xtree.bet.bean.response.im;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.xtree.base.vo.BaseBean;

import java.util.List;

public class SportMarketLine implements BaseBean, Parcelable {

    public String marketLineId;
    public String marketType;
    public String handicap;
    public String status;
    public List<Outcome> outcomes;

    public SportMarketLine() {}

    protected SportMarketLine(Parcel in) {
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

    public static final Creator<SportMarketLine> CREATOR = new Creator<SportMarketLine>() {
        @Override
        public SportMarketLine createFromParcel(Parcel source) {
            return new SportMarketLine(source);
        }

        @Override
        public SportMarketLine[] newArray(int size) {
            return new SportMarketLine[size];
        }
    };
}
