package com.xtree.bet.bean.response.im;

import android.os.Parcel;
import android.os.Parcelable;

import com.xtree.base.vo.BaseBean;

import java.util.List;

public class Wager implements BaseBean, Parcelable {

    public String WagerId;
    public String WagerCreationDateTime;
    public String MemberCode;
    public double InputtedStakeAmount;
    public double MemberWinLossAmount;
    public int OddsType;
    public int WagerType;
    public String BettingPlatform;
    public int BetConfirmationStatus;
    public int BetSettlementStatus;
    public int BetTradeStatus;
    public String PricingId;
    public String BuyBackPricing;
    public double BetTradeBuyBackAmount;
    public int NoOfCombination;
    public int ComboSelection;
    public double PotentialPayout;
    public boolean CanSell;
    public List<WagerItem> WagerItemList;

    public Wager() {}

    protected Wager(Parcel in) {
        WagerId = in.readString();
        WagerCreationDateTime = in.readString();
        MemberCode = in.readString();
        InputtedStakeAmount = in.readDouble();
        MemberWinLossAmount = in.readDouble();
        OddsType = in.readInt();
        WagerType = in.readInt();
        BettingPlatform = in.readString();
        BetConfirmationStatus = in.readInt();
        BetSettlementStatus = in.readInt();
        BetTradeStatus = in.readInt();
        PricingId = in.readString();
        BuyBackPricing = in.readString();
        BetTradeBuyBackAmount = in.readDouble();
        NoOfCombination = in.readInt();
        ComboSelection = in.readInt();
        PotentialPayout = in.readDouble();
        CanSell = in.readByte() != 0;
        WagerItemList = in.createTypedArrayList(WagerItem.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(WagerId);
        dest.writeString(WagerCreationDateTime);
        dest.writeString(MemberCode);
        dest.writeDouble(InputtedStakeAmount);
        dest.writeDouble(MemberWinLossAmount);
        dest.writeInt(OddsType);
        dest.writeInt(WagerType);
        dest.writeString(BettingPlatform);
        dest.writeInt(BetConfirmationStatus);
        dest.writeInt(BetSettlementStatus);
        dest.writeInt(BetTradeStatus);
        dest.writeString(PricingId);
        dest.writeString(BuyBackPricing);
        dest.writeDouble(BetTradeBuyBackAmount);
        dest.writeInt(NoOfCombination);
        dest.writeInt(ComboSelection);
        dest.writeDouble(PotentialPayout);
        dest.writeByte((byte) (CanSell ? 1 : 0));
        dest.writeTypedList(WagerItemList);
    }

    public void readFromParcel(Parcel source) {
        WagerId = source.readString();
        WagerCreationDateTime = source.readString();
        MemberCode = source.readString();
        InputtedStakeAmount = source.readDouble();
        MemberWinLossAmount = source.readDouble();
        OddsType = source.readInt();
        WagerType = source.readInt();
        BettingPlatform = source.readString();
        BetConfirmationStatus = source.readInt();
        BetSettlementStatus = source.readInt();
        BetTradeStatus = source.readInt();
        PricingId = source.readString();
        BuyBackPricing = source.readString();
        BetTradeBuyBackAmount = source.readDouble();
        NoOfCombination = source.readInt();
        ComboSelection = source.readInt();
        PotentialPayout = source.readDouble();
        CanSell = source.readByte() != 0;
        WagerItemList = source.createTypedArrayList(WagerItem.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Wager> CREATOR = new Creator<Wager>() {
        @Override
        public Wager createFromParcel(Parcel in) {
            return new Wager(in);
        }

        @Override
        public Wager[] newArray(int size) {
            return new Wager[size];
        }
    };
}
