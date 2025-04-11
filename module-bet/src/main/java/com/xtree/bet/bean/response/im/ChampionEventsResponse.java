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

    public static class MarketLine implements Parcelable {
        public int MarketlineId;
        public int BetTypeId;
        public String BetTypeName;
        public int PeriodId;
        public String PeriodName;
        public int MarketLineLevel;
        public int MarketlineStatusId;
        public boolean IsLocked;
        public List<WagerSelection> WagerSelections;

        protected MarketLine(Parcel in) {
            MarketlineId = in.readInt();
            BetTypeId = in.readInt();
            BetTypeName = in.readString();
            PeriodId = in.readInt();
            PeriodName = in.readString();
            MarketLineLevel = in.readInt();
            MarketlineStatusId = in.readInt();
            IsLocked = in.readByte() != 0;
            WagerSelections = in.createTypedArrayList(WagerSelection.CREATOR);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(MarketlineId);
            dest.writeInt(BetTypeId);
            dest.writeString(BetTypeName);
            dest.writeInt(PeriodId);
            dest.writeString(PeriodName);
            dest.writeInt(MarketLineLevel);
            dest.writeInt(MarketlineStatusId);
            dest.writeByte((byte) (IsLocked ? 1 : 0));
            dest.writeTypedList(WagerSelections);
        }

        public static final Creator<MarketLine> CREATOR = new Creator<MarketLine>() {
            @Override
            public MarketLine createFromParcel(Parcel in) {
                return new MarketLine(in);
            }

            @Override
            public MarketLine[] newArray(int size) {
                return new MarketLine[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }
    }

    public static class WagerSelection implements Parcelable {
        public long WagerSelectionId;
        public int SelectionId;
        public String SelectionName;
        public Object Handicap;
        public Object Specifiers;
        public int OddsType;
        public double Odds;
        public List<OddsListItem> OddsList;

        protected WagerSelection(Parcel in) {
            WagerSelectionId = in.readLong();
            SelectionId = in.readInt();
            SelectionName = in.readString();
            Handicap = in.readValue(Object.class.getClassLoader());
            Specifiers = in.readValue(Object.class.getClassLoader());
            OddsType = in.readInt();
            Odds = in.readDouble();
            OddsList = in.createTypedArrayList(OddsListItem.CREATOR);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(WagerSelectionId);
            dest.writeInt(SelectionId);
            dest.writeString(SelectionName);
            dest.writeValue(Handicap);
            dest.writeValue(Specifiers);
            dest.writeInt(OddsType);
            dest.writeDouble(Odds);
            dest.writeTypedList(OddsList);
        }

        public static final Creator<WagerSelection> CREATOR = new Creator<WagerSelection>() {
            @Override
            public WagerSelection createFromParcel(Parcel in) {
                return new WagerSelection(in);
            }

            @Override
            public WagerSelection[] newArray(int size) {
                return new WagerSelection[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }
    }

    public static class OddsListItem implements Parcelable {
        public int OddsType;
        public OddsValues OddsValues;

        protected OddsListItem(Parcel in) {
            OddsType = in.readInt();
            OddsValues = in.readParcelable(OddsValues.class.getClassLoader());
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(OddsType);
            dest.writeParcelable(OddsValues, flags);
        }

        public static final Creator<OddsListItem> CREATOR = new Creator<OddsListItem>() {
            @Override
            public OddsListItem createFromParcel(Parcel in) {
                return new OddsListItem(in);
            }

            @Override
            public OddsListItem[] newArray(int size) {
                return new OddsListItem[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }
    }

    public static class OddsValues implements Parcelable {
        public String A;
        public String B;
        public String C;
        public String D;
        public String E;
        public String F;
        public String G;

        protected OddsValues(Parcel in) {
            A = in.readString();
            B = in.readString();
            C = in.readString();
            D = in.readString();
            E = in.readString();
            F = in.readString();
            G = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(A);
            dest.writeString(B);
            dest.writeString(C);
            dest.writeString(D);
            dest.writeString(E);
            dest.writeString(F);
            dest.writeString(G);
        }

        public static final Creator<OddsValues> CREATOR = new Creator<OddsValues>() {
            @Override
            public OddsValues createFromParcel(Parcel in) {
                return new OddsValues(in);
            }

            @Override
            public OddsValues[] newArray(int size) {
                return new OddsValues[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }
    }
}

