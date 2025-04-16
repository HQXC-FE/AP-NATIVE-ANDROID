package com.xtree.bet.bean.response.im;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

import java.util.List;


public class MarketLine implements BaseBean {

    public long MarketlineId;
    public int BetTypeId;
    public String BetTypeName;
    public int PeriodId;
    public String PeriodName;
    public int MarketLineLevel;
    public int MarketlineStatusId;
    public boolean IsLocked;
    public List<WagerSelection> WagerSelections;

    public MarketLine() {}

    protected MarketLine(Parcel in) {
        MarketlineId = in.readLong();
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
        dest.writeLong(MarketlineId);
        dest.writeInt(BetTypeId);
        dest.writeString(BetTypeName);
        dest.writeInt(PeriodId);
        dest.writeString(PeriodName);
        dest.writeInt(MarketLineLevel);
        dest.writeInt(MarketlineStatusId);
        dest.writeByte((byte) (IsLocked ? 1 : 0));
        dest.writeTypedList(WagerSelections);
    }

    @Override
    public int describeContents() {
        return 0;
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
}
