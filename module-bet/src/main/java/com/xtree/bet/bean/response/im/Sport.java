package com.xtree.bet.bean.response.im;


import android.os.Parcel;
import android.os.Parcelable;

import com.xtree.base.vo.BaseBean;

import java.util.List;

public class Sport implements BaseBean {

    public int SportId;
    public String SportName;
    public List<Event> Events;

    public Sport() {}

    protected Sport(Parcel in) {
        SportId = in.readInt();
        SportName = in.readString();
        Events = in.createTypedArrayList(Event.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(SportId);
        dest.writeString(SportName);
        dest.writeTypedList(Events);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Sport> CREATOR = new Creator<Sport>() {
        @Override
        public Sport createFromParcel(Parcel in) {
            return new Sport(in);
        }

        @Override
        public Sport[] newArray(int size) {
            return new Sport[size];
        }
    };
}
