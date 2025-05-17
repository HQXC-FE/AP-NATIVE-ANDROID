package com.xtree.bet.bean.response.im;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;
import com.xtree.base.vo.BaseBean;
import com.xtree.bet.constant.IMMarketTag;

import java.util.Collections;
import java.util.List;


public class MarketLine implements BaseBean {

    @SerializedName("MarketlineId")
    public long marketlineId;

    @SerializedName("BetTypeId")
    public int betTypeId;

    @SerializedName("BetTypeName")
    public String betTypeName;

    @SerializedName("PeriodId")
    public int periodId;

    @SerializedName("PeriodName")
    public String periodName;

    @SerializedName("MarketLineLevel")
    public int marketLineLevel;

    @SerializedName("MarketlineStatusId")
    public int marketlineStatusId;

    @SerializedName("IsLocked")
    public boolean isLocked;

    @SerializedName("WagerSelections")
    public List<WagerSelection> wagerSelections;

    public boolean openParlay;

    public List<String> betTypeGroupName;//经过转化之后的分组名称

    public List<String> getBetTypeGroupName() {
        return betTypeGroupName;
    }

    public void setBetTypeGroupName(List<String> betTypeGroupName) {
        this.betTypeGroupName = betTypeGroupName;
    }

    public MarketLine() {
    }

    protected MarketLine(Parcel in) {
        marketlineId = in.readLong();
        betTypeId = in.readInt();
        betTypeName = in.readString();
        periodId = in.readInt();
        periodName = in.readString();
        marketLineLevel = in.readInt();
        marketlineStatusId = in.readInt();
        isLocked = in.readByte() != 0;
        wagerSelections = in.createTypedArrayList(WagerSelection.CREATOR);
        openParlay = in.readByte() != 0;
        betTypeGroupName = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(marketlineId);
        dest.writeInt(betTypeId);
        dest.writeString(betTypeName);
        dest.writeInt(periodId);
        dest.writeString(periodName);
        dest.writeInt(marketLineLevel);
        dest.writeInt(marketlineStatusId);
        dest.writeByte((byte) (isLocked ? 1 : 0));
        dest.writeTypedList(wagerSelections);
        dest.writeByte((byte) (openParlay ? 1 : 0));
        dest.writeStringList(betTypeGroupName);
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

    // Getter & Setter methods

    public long getMarketlineId() {
        return marketlineId;
    }

    public void setMarketlineId(long marketlineId) {
        this.marketlineId = marketlineId;
    }

    public int getBetTypeId() {
        return betTypeId;
    }

    public void setBetTypeId(int betTypeId) {
        this.betTypeId = betTypeId;
    }

    public String getBetTypeName() {
        return betTypeName;
    }

    public void setBetTypeName(String betTypeName) {
        this.betTypeName = betTypeName;
    }

    public int getPeriodId() {
        return periodId;
    }

    public void setPeriodId(int periodId) {
        this.periodId = periodId;
    }

    public String getPeriodName() {
        return periodName;
    }

    public void setPeriodName(String periodName) {
        this.periodName = periodName;
    }

    public int getMarketLineLevel() {
        return marketLineLevel;
    }

    public void setMarketLineLevel(int marketLineLevel) {
        this.marketLineLevel = marketLineLevel;
    }

    public int getMarketlineStatusId() {
        return marketlineStatusId;
    }

    public void setMarketlineStatusId(int marketlineStatusId) {
        this.marketlineStatusId = marketlineStatusId;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public List<WagerSelection> getWagerSelections() {
        return wagerSelections;
    }

    public void setWagerSelections(List<WagerSelection> wagerSelections) {
        this.wagerSelections = wagerSelections;
    }

    public boolean isOpenParlay() {
        return openParlay;
    }

    public void setOpenParlay(boolean openParlay) {
        this.openParlay = openParlay;
    }
}

