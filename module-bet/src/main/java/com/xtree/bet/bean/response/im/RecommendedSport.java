package com.xtree.bet.bean.response.im;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

import java.util.List;

public class RecommendedSport implements BaseBean {


    public int SportId;
    public String SportName;
    public int OrderNumber;
    public List<Event> Events;

    protected RecommendedSport(Parcel in) {
        SportId = in.readInt();
        SportName = in.readString();
        OrderNumber = in.readInt();
        Events = in.createTypedArrayList(Event.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(SportId);
        dest.writeString(SportName);
        dest.writeInt(OrderNumber);
        dest.writeTypedList(Events);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RecommendedSport> CREATOR = new Creator<RecommendedSport>() {
        @Override
        public RecommendedSport createFromParcel(Parcel in) {
            return new RecommendedSport(in);
        }

        @Override
        public RecommendedSport[] newArray(int size) {
            return new RecommendedSport[size];
        }
    };
}
