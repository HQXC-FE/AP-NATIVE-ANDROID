package com.xtree.bet.bean.response.im;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

import java.util.List;

/**
 * 优胜冠军赛事Event
 */

public class OutrightEvent implements BaseBean {

    public String OutrightEventName;
    public long EventId;
    public int EventStatusId;
    public int OrderNumber;
    public String EventDate;
    public SportCompetition Competition;
    public List<SportMarketLine> MarketLines;

    public OutrightEvent() {
    }

    protected OutrightEvent(Parcel in) {
        OutrightEventName = in.readString();
        EventId = in.readLong();
        EventStatusId = in.readInt();
        OrderNumber = in.readInt();
        EventDate = in.readString();
        Competition = in.readParcelable(SportCompetition.class.getClassLoader());
        MarketLines = in.createTypedArrayList(SportMarketLine.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(OutrightEventName);
        dest.writeLong(EventId);
        dest.writeInt(EventStatusId);
        dest.writeInt(OrderNumber);
        dest.writeString(EventDate);
        dest.writeParcelable(Competition, flags);
        dest.writeTypedList(MarketLines);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OutrightEvent> CREATOR = new Creator<OutrightEvent>() {
        @Override
        public OutrightEvent createFromParcel(Parcel in) {
            return new OutrightEvent(in);
        }

        @Override
        public OutrightEvent[] newArray(int size) {
            return new OutrightEvent[size];
        }
    };

}
