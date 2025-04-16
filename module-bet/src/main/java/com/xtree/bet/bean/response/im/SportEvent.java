package com.xtree.bet.bean.response.im;

import android.os.Parcel;

import androidx.annotation.NonNull;

import com.xtree.base.vo.BaseBean;

import java.util.List;

public class SportEvent implements BaseBean{

    public String eventId;
    public String eventName;
    public String startTime;
    public String status;
    public List<SportMarketLine> marketLines;

    public SportEvent() {
    }

    protected SportEvent(Parcel in) {
        this.eventId = in.readString();
        this.eventName = in.readString();
        this.startTime = in.readString();
        this.status = in.readString();
        this.marketLines = in.createTypedArrayList(SportMarketLine.CREATOR);
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(this.eventId);
        dest.writeString(this.eventName);
        dest.writeString(this.startTime);
        dest.writeString(this.status);
        dest.writeTypedList(this.marketLines);
    }

    public void readFromParcel(Parcel source) {
        this.eventId = source.readString();
        this.eventName = source.readString();
        this.startTime = source.readString();
        this.status = source.readString();
        this.marketLines = source.createTypedArrayList(SportMarketLine.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SportEvent> CREATOR = new Creator<SportEvent>() {
        @Override
        public SportEvent createFromParcel(Parcel source) {
            return new SportEvent(source);
        }

        @Override
        public SportEvent[] newArray(int size) {
            return new SportEvent[size];
        }
    };
}

