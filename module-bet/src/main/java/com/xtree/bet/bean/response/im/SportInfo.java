package com.xtree.bet.bean.response.im;

import android.os.Parcel;

import androidx.annotation.NonNull;

import com.xtree.base.vo.BaseBean;

import java.util.List;

public class SportInfo implements BaseBean {

    public String sportId;
    public String sportName;
    public List<Event> events;

    public SportInfo() {
    }

    protected SportInfo(Parcel in) {
        this.sportId = in.readString();
        this.sportName = in.readString();
        this.events = in.createTypedArrayList(Event.CREATOR);
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(this.sportId);
        dest.writeString(this.sportName);
        dest.writeTypedList(this.events);
    }

    public void readFromParcel(Parcel source) {
        this.sportId = source.readString();
        this.sportName = source.readString();
        this.events = source.createTypedArrayList(Event.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SportInfo> CREATOR = new Creator<SportInfo>() {
        @Override
        public SportInfo createFromParcel(Parcel source) {
            return new SportInfo(source);
        }

        @Override
        public SportInfo[] newArray(int size) {
            return new SportInfo[size];
        }
    };
}

