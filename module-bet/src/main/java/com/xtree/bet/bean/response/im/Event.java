package com.xtree.bet.bean.response.im;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.xtree.base.vo.BaseBean;

import java.util.List;

public class Event implements BaseBean, Parcelable {

    public String eventId;
    public String eventName;
    public String startTime;
    public String status;
    public List<MarketLine> marketLines;

    public Event() {
    }

    protected Event(Parcel in) {
        this.eventId = in.readString();
        this.eventName = in.readString();
        this.startTime = in.readString();
        this.status = in.readString();
        this.marketLines = in.createTypedArrayList(MarketLine.CREATOR);
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
        this.marketLines = source.createTypedArrayList(MarketLine.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}

