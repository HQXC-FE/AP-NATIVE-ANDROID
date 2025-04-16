package com.xtree.bet.bean.response.im;

import android.os.Parcel;
import android.os.Parcelable;

public  class WagerSelectionInfo implements Parcelable {
    public int BetTypeId;
    public long EventId;
    public int SportId;
    public int OddsType;
    public double Odds;
    public long WagerSelectionId;
    public long MarketlineId;
    public int PeriodId;
    public double Handicap;
    public int BetTypeSelectionId;
    public long RefId;
    public int OutrightTeamId;
    public boolean ReturnNearestHandicap;
    public String Specifiers;

    public WagerSelectionInfo() {}

    protected WagerSelectionInfo(Parcel in) {
        BetTypeId = in.readInt();
        EventId = in.readLong();
        SportId = in.readInt();
        OddsType = in.readInt();
        Odds = in.readDouble();
        WagerSelectionId = in.readLong();
        MarketlineId = in.readLong();
        PeriodId = in.readInt();
        Handicap = in.readDouble();
        BetTypeSelectionId = in.readInt();
        RefId = in.readLong();
        OutrightTeamId = in.readInt();
        ReturnNearestHandicap = in.readByte() != 0;
        Specifiers = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(BetTypeId);
        dest.writeLong(EventId);
        dest.writeInt(SportId);
        dest.writeInt(OddsType);
        dest.writeDouble(Odds);
        dest.writeLong(WagerSelectionId);
        dest.writeLong(MarketlineId);
        dest.writeInt(PeriodId);
        dest.writeDouble(Handicap);
        dest.writeInt(BetTypeSelectionId);
        dest.writeLong(RefId);
        dest.writeInt(OutrightTeamId);
        dest.writeByte((byte) (ReturnNearestHandicap ? 1 : 0));
        dest.writeString(Specifiers);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WagerSelectionInfo> CREATOR = new Creator<WagerSelectionInfo>() {
        @Override
        public WagerSelectionInfo createFromParcel(Parcel in) {
            return new WagerSelectionInfo(in);
        }

        @Override
        public WagerSelectionInfo[] newArray(int size) {
            return new WagerSelectionInfo[size];
        }
    };
}
