package com.xtree.bet.bean.response.im;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;
import com.xtree.base.vo.BaseBean;

import java.util.List;

/**
 * 优胜冠军赛事Event
 */

public class OutrightEvent implements BaseBean {
    @SerializedName("OutrightEventName")
    public String outrightEventName;
    @SerializedName("EventId")
    public long eventId;
    @SerializedName("EventStatusId")
    public int eventStatusId;
    @SerializedName("OrderNumber")
    public int orderNumber;
    @SerializedName("EventDate")
    public String eventDate;
    @SerializedName("Competition")
    public SportCompetition competition;
    @SerializedName("MarketLines")
    public List<SportMarketLine> marketLines;

    public OutrightEvent() {
    }

    protected OutrightEvent(Parcel in) {
        outrightEventName = in.readString();
        eventId = in.readLong();
        eventStatusId = in.readInt();
        orderNumber = in.readInt();
        eventDate = in.readString();
        competition = in.readParcelable(SportCompetition.class.getClassLoader());
        marketLines = in.createTypedArrayList(SportMarketLine.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(outrightEventName);
        dest.writeLong(eventId);
        dest.writeInt(eventStatusId);
        dest.writeInt(orderNumber);
        dest.writeString(eventDate);
        dest.writeParcelable(competition, flags);
        dest.writeTypedList(marketLines);
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
