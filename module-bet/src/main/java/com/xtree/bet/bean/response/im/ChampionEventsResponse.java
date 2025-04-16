package com.xtree.bet.bean.response.im;

import android.os.Parcel;
import android.os.Parcelable;

import com.xtree.base.vo.BaseBean;

import java.util.List;

public class ChampionEventsResponse implements BaseBean {
    public List<Event> Events;
    public double Delta;
    public String ServerTime;
    public int StatusCode;
    public String StatusDesc;

    protected ChampionEventsResponse(Parcel in) {
        Events = in.createTypedArrayList(Event.CREATOR);
        Delta = in.readDouble();
        ServerTime = in.readString();
        StatusCode = in.readInt();
        StatusDesc = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(Events);
        dest.writeDouble(Delta);
        dest.writeString(ServerTime);
        dest.writeInt(StatusCode);
        dest.writeString(StatusDesc);
    }

    public static final Creator<ChampionEventsResponse> CREATOR = new Creator<ChampionEventsResponse>() {
        @Override
        public ChampionEventsResponse createFromParcel(Parcel in) {
            return new ChampionEventsResponse(in);
        }

        @Override
        public ChampionEventsResponse[] newArray(int size) {
            return new ChampionEventsResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public static class Event implements Parcelable {
        public String OutrightEventName;
        public int EventId;
        public int EventStatusId;
        public int OrderNumber;
        public String EventDate;
        public Competition Competition;
        public List<MarketLine> MarketLines;

        protected Event(Parcel in) {
            OutrightEventName = in.readString();
            EventId = in.readInt();
            EventStatusId = in.readInt();
            OrderNumber = in.readInt();
            EventDate = in.readString();
            Competition = in.readParcelable(Competition.class.getClassLoader());
            MarketLines = in.createTypedArrayList(MarketLine.CREATOR);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(OutrightEventName);
            dest.writeInt(EventId);
            dest.writeInt(EventStatusId);
            dest.writeInt(OrderNumber);
            dest.writeString(EventDate);
            dest.writeParcelable(Competition, flags);
            dest.writeTypedList(MarketLines);
        }

        public static final Creator<Event> CREATOR = new Creator<Event>() {
            @Override
            public Event createFromParcel(Parcel in) {
                return new Event(in);
            }

            @Override
            public Event[] newArray(int size) {
                return new Event[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }
    }

    public static class Competition implements Parcelable {
        public int CompetitionId;
        public String CompetitionName;
        public int PMOrderNumber;
        public int RBOrderNumber;

        protected Competition(Parcel in) {
            CompetitionId = in.readInt();
            CompetitionName = in.readString();
            PMOrderNumber = in.readInt();
            RBOrderNumber = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(CompetitionId);
            dest.writeString(CompetitionName);
            dest.writeInt(PMOrderNumber);
            dest.writeInt(RBOrderNumber);
        }

        public static final Creator<Competition> CREATOR = new Creator<Competition>() {
            @Override
            public Competition createFromParcel(Parcel in) {
                return new Competition(in);
            }

            @Override
            public Competition[] newArray(int size) {
                return new Competition[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }
    }

}

