package com.xtree.bet.bean.response.im;

import android.os.Parcel;
import android.os.Parcelable;

import com.xtree.base.vo.BaseBean;

public class WagerItem implements BaseBean, Parcelable {

    public int WagerItemConfirmationStatus;
    public int WagerItemConfirmationType;
    public int WagerItemCancelType;
    public int WagerItemCancelReason;
    public int Market;
    public int EventId;
    public int EventTypeId;
    public String EventDateTime;
    public int SportId;
    public int CompetitionId;
    public String CompetitionName;
    public int EventGroupTypeId;
    public int HomeTeamId;
    public String HomeTeamName;
    public int AwayTeamId;
    public String AwayTeamName;
    public String FavTeam;
    public int BetTypeId;
    public String BetTypeName;
    public int PeriodId;
    public int BetTypeSelectionId;
    public String SelectionName;
    public String EventOutrightName;
    public int OutrightTeamId;
    public String OutrightTeamName;
    public double Odds;
    public double Handicap;
    public Integer HomeTeamHTScore;
    public Integer AwayTeamHTScore;
    public Integer HomeTeamFTScore;
    public Integer AwayTeamFTScore;
    public Integer WagerHomeTeamScore;
    public Integer WagerAwayTeamScore;
    public int GroundTypeId;
    public int Season;
    public int MatchDay;
    public String Specifiers;

    public WagerItem() {}

    protected WagerItem(Parcel in) {
        WagerItemConfirmationStatus = in.readInt();
        WagerItemConfirmationType = in.readInt();
        WagerItemCancelType = in.readInt();
        WagerItemCancelReason = in.readInt();
        Market = in.readInt();
        EventId = in.readInt();
        EventTypeId = in.readInt();
        EventDateTime = in.readString();
        SportId = in.readInt();
        CompetitionId = in.readInt();
        CompetitionName = in.readString();
        EventGroupTypeId = in.readInt();
        HomeTeamId = in.readInt();
        HomeTeamName = in.readString();
        AwayTeamId = in.readInt();
        AwayTeamName = in.readString();
        FavTeam = in.readString();
        BetTypeId = in.readInt();
        BetTypeName = in.readString();
        PeriodId = in.readInt();
        BetTypeSelectionId = in.readInt();
        SelectionName = in.readString();
        EventOutrightName = in.readString();
        OutrightTeamId = in.readInt();
        OutrightTeamName = in.readString();
        Odds = in.readDouble();
        Handicap = in.readDouble();
        HomeTeamHTScore = (Integer) (in.readByte() == 0 ? null : in.readInt());
        AwayTeamHTScore = (Integer) (in.readByte() == 0 ? null : in.readInt());
        HomeTeamFTScore = (Integer) (in.readByte() == 0 ? null : in.readInt());
        AwayTeamFTScore = (Integer) (in.readByte() == 0 ? null : in.readInt());
        WagerHomeTeamScore = (Integer) (in.readByte() == 0 ? null : in.readInt());
        WagerAwayTeamScore = (Integer) (in.readByte() == 0 ? null : in.readInt());
        GroundTypeId = in.readInt();
        Season = in.readInt();
        MatchDay = in.readInt();
        Specifiers = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(WagerItemConfirmationStatus);
        dest.writeInt(WagerItemConfirmationType);
        dest.writeInt(WagerItemCancelType);
        dest.writeInt(WagerItemCancelReason);
        dest.writeInt(Market);
        dest.writeInt(EventId);
        dest.writeInt(EventTypeId);
        dest.writeString(EventDateTime);
        dest.writeInt(SportId);
        dest.writeInt(CompetitionId);
        dest.writeString(CompetitionName);
        dest.writeInt(EventGroupTypeId);
        dest.writeInt(HomeTeamId);
        dest.writeString(HomeTeamName);
        dest.writeInt(AwayTeamId);
        dest.writeString(AwayTeamName);
        dest.writeString(FavTeam);
        dest.writeInt(BetTypeId);
        dest.writeString(BetTypeName);
        dest.writeInt(PeriodId);
        dest.writeInt(BetTypeSelectionId);
        dest.writeString(SelectionName);
        dest.writeString(EventOutrightName);
        dest.writeInt(OutrightTeamId);
        dest.writeString(OutrightTeamName);
        dest.writeDouble(Odds);
        dest.writeDouble(Handicap);
        if (HomeTeamHTScore == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(HomeTeamHTScore);
        }
        if (AwayTeamHTScore == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(AwayTeamHTScore);
        }
        if (HomeTeamFTScore == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(HomeTeamFTScore);
        }
        if (AwayTeamFTScore == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(AwayTeamFTScore);
        }
        if (WagerHomeTeamScore == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(WagerHomeTeamScore);
        }
        if (WagerAwayTeamScore == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(WagerAwayTeamScore);
        }
        dest.writeInt(GroundTypeId);
        dest.writeInt(Season);
        dest.writeInt(MatchDay);
        dest.writeString(Specifiers);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WagerItem> CREATOR = new Creator<WagerItem>() {
        @Override
        public WagerItem createFromParcel(Parcel in) {
            return new WagerItem(in);
        }

        @Override
        public WagerItem[] newArray(int size) {
            return new WagerItem[size];
        }
    };
}

