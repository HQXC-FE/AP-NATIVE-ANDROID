package com.xtree.bet.bean.response.im;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;
import com.xtree.base.vo.BaseBean;

import java.util.List;

public class Sport implements BaseBean {

    @SerializedName("SportId")
    private int sportId;
    @SerializedName("SportName")
    private String sportName;
    @SerializedName("OrderNumber")
    private int orderNumber;
    @SerializedName("Events")
    private List<MatchInfo> events;

    public Sport() {
    }

    protected Sport(Parcel in) {
        sportId = in.readInt();
        sportName = in.readString();
        orderNumber = in.readInt();
        events = in.createTypedArrayList(MatchInfo.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(sportId);
        dest.writeString(sportName);
        dest.writeInt(orderNumber);
        dest.writeTypedList(events);
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

    public int getSportId() {
        return sportId;
    }

    public void setSportId(int sportId) {
        sportId = sportId;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        sportName = sportName;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        orderNumber = orderNumber;
    }

    public List<MatchInfo> getEvents() {
        return events;
    }

    public void setEvents(List<MatchEvent> events) {
        events = events;
    }
}


