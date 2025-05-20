package com.xtree.bet.bean.response.im;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.xtree.base.vo.BaseBean;

public class WagerSelectionInfo implements BaseBean {
    public int BetTypeId;
    public long EventId;
    public int SportId;
    public int OddsType;
    public double Odds;
    public long WagerSelectionId;
    public long MarketlineId;
    public int PeriodId;
    public double Handicap;
    public long RefId; //WagerSelectionId
    /**
     * 投注选项类型 ID. （只适用于定时赛事，如果优胜冠军会是0）
     */
    public int BetTypeSelectionId;
    /**
     * 优胜冠军类型 ID. （只适用于优胜冠军，如果定时赛事会是0）
     */
    public int OutrightTeamId;
    public boolean ReturnNearestHandicap;
    public String Specifiers;

    public String WagerId;
    public String BetStatusMessage;
    public int ComboSelectionId;
    public int BetConfirmationStatus;
    public double EstimatedPayoutFullAmount;

    /**
     * 指出盘口状态.
     * 1 = 开盘
     * 2 = 关盘
     */
    public int MarketlineStatusId;

    public WagerSelectionInfo() {
    }

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
        WagerId = in.readString();
        BetStatusMessage = in.readString();
        ComboSelectionId = in.readInt();
        BetConfirmationStatus = in.readInt();
        EstimatedPayoutFullAmount = in.readDouble();
        MarketlineStatusId = in.readInt();
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
        dest.writeString(WagerId);
        dest.writeString(BetStatusMessage);
        dest.writeInt(ComboSelectionId);
        dest.writeInt(BetConfirmationStatus);
        dest.writeDouble(EstimatedPayoutFullAmount);
        dest.writeInt(MarketlineStatusId);
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

    public int getBetTypeId() {
        return BetTypeId;
    }

    public void setBetTypeId(int betTypeId) {
        BetTypeId = betTypeId;
    }

    public long getEventId() {
        return EventId;
    }

    public void setEventId(long eventId) {
        EventId = eventId;
    }

    public int getSportId() {
        return SportId;
    }

    public void setSportId(int sportId) {
        SportId = sportId;
    }

    public int getOddsType() {
        return OddsType;
    }

    public void setOddsType(int oddsType) {
        OddsType = oddsType;
    }

    public double getOdds() {
        return Odds;
    }

    public void setOdds(double odds) {
        Odds = odds;
    }

    public long getWagerSelectionId() {
        return WagerSelectionId;
    }

    public void setWagerSelectionId(long wagerSelectionId) {
        WagerSelectionId = wagerSelectionId;
    }

    public long getMarketlineId() {
        return MarketlineId;
    }

    public void setMarketlineId(long marketlineId) {
        MarketlineId = marketlineId;
    }

    public int getPeriodId() {
        return PeriodId;
    }

    public void setPeriodId(int periodId) {
        PeriodId = periodId;
    }

    public double getHandicap() {
        return Handicap;
    }

    public void setHandicap(double handicap) {
        Handicap = handicap;
    }

    public long getRefId() {
        return RefId;
    }

    public void setRefId(long refId) {
        RefId = refId;
    }

    public int getBetTypeSelectionId() {
        return BetTypeSelectionId;
    }

    public void setBetTypeSelectionId(int betTypeSelectionId) {
        BetTypeSelectionId = betTypeSelectionId;
    }

    public int getOutrightTeamId() {
        return OutrightTeamId;
    }

    public void setOutrightTeamId(int outrightTeamId) {
        OutrightTeamId = outrightTeamId;
    }

    public boolean isReturnNearestHandicap() {
        return ReturnNearestHandicap;
    }

    public void setReturnNearestHandicap(boolean returnNearestHandicap) {
        ReturnNearestHandicap = returnNearestHandicap;
    }

    public String getSpecifiers() {
        return Specifiers;
    }

    public void setSpecifiers(String specifiers) {
        Specifiers = specifiers;
    }

    public String getWagerId() {
        return WagerId;
    }

    public void setWagerId(String wagerId) {
        WagerId = wagerId;
    }

    public String getBetStatusMessage() {
        return BetStatusMessage;
    }

    public void setBetStatusMessage(String betStatusMessage) {
        BetStatusMessage = betStatusMessage;
    }

    public int getComboSelectionId() {
        return ComboSelectionId;
    }

    public void setComboSelectionId(int comboSelectionId) {
        ComboSelectionId = comboSelectionId;
    }

    public int getBetConfirmationStatus() {
        return BetConfirmationStatus;
    }

    public void setBetConfirmationStatus(int betConfirmationStatus) {
        BetConfirmationStatus = betConfirmationStatus;
    }

    public double getEstimatedPayoutFullAmount() {
        return EstimatedPayoutFullAmount;
    }

    public void setEstimatedPayoutFullAmount(double estimatedPayoutFullAmount) {
        EstimatedPayoutFullAmount = estimatedPayoutFullAmount;
    }

    public int getMarketlineStatusId() {
        return MarketlineStatusId;
    }

    public void setMarketlineStatusId(int marketlineStatusId) {
        MarketlineStatusId = marketlineStatusId;
    }
}
