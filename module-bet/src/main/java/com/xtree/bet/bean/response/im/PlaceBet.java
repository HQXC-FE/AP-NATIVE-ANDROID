package com.xtree.bet.bean.response.im;

import android.os.Parcel;
import android.os.Parcelable;

import com.xtree.base.vo.BaseBean;

import java.util.ArrayList;
import java.util.List;

public class PlaceBet implements BaseBean {

    public List<WagerSelectionInfo> WagerSelectionInfos = new ArrayList<>();
    public List<AcceptedWagerSelection> AcceptedWagerSelectionList = new ArrayList<>();
    public List<UpdatedWagerSelectionInfo> UpdatedWagerSelectionInfos = new ArrayList<>();
    public List<UpdatedBetSetting> UpdatedBetSetting = new ArrayList<>();
    public double AvailableBalance;
    public String ServerTime;
    public int StatusCode;
    public String StatusDesc;


    protected PlaceBet(Parcel in) {
        in.readList(WagerSelectionInfos, WagerSelectionInfo.class.getClassLoader());
        in.readList(AcceptedWagerSelectionList, AcceptedWagerSelection.class.getClassLoader());
        in.readList(UpdatedWagerSelectionInfos, UpdatedWagerSelectionInfo.class.getClassLoader());
        in.readList(UpdatedBetSetting, UpdatedBetSetting.class.getClassLoader());
        AvailableBalance = in.readDouble();
        ServerTime = in.readString();
        StatusCode = in.readInt();
        StatusDesc = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(WagerSelectionInfos);
        dest.writeList(AcceptedWagerSelectionList);
        dest.writeList(UpdatedWagerSelectionInfos);
        dest.writeList(UpdatedBetSetting);
        dest.writeDouble(AvailableBalance);
        dest.writeString(ServerTime);
        dest.writeInt(StatusCode);
        dest.writeString(StatusDesc);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PlaceBet> CREATOR = new Creator<PlaceBet>() {
        @Override
        public PlaceBet createFromParcel(Parcel in) {
            return new PlaceBet(in);
        }

        @Override
        public PlaceBet[] newArray(int size) {
            return new PlaceBet[size];
        }
    };

    public List<WagerSelectionInfo> getWagerSelectionInfos() {
        return WagerSelectionInfos;
    }

    public void setWagerSelectionInfos(List<WagerSelectionInfo> wagerSelectionInfos) {
        WagerSelectionInfos = wagerSelectionInfos;
    }

    public List<AcceptedWagerSelection> getAcceptedWagerSelectionList() {
        return AcceptedWagerSelectionList;
    }

    public void setAcceptedWagerSelectionList(List<AcceptedWagerSelection> acceptedWagerSelectionList) {
        AcceptedWagerSelectionList = acceptedWagerSelectionList;
    }

    public List<UpdatedWagerSelectionInfo> getUpdatedWagerSelectionInfos() {
        return UpdatedWagerSelectionInfos;
    }

    public void setUpdatedWagerSelectionInfos(List<UpdatedWagerSelectionInfo> updatedWagerSelectionInfos) {
        UpdatedWagerSelectionInfos = updatedWagerSelectionInfos;
    }

    public List<UpdatedBetSetting> getUpdatedBetSetting() {
        return UpdatedBetSetting;
    }

    public void setUpdatedBetSetting(List<UpdatedBetSetting> updatedBetSetting) {
        UpdatedBetSetting = updatedBetSetting;
    }

    public double getAvailableBalance() {
        return AvailableBalance;
    }

    public void setAvailableBalance(double availableBalance) {
        AvailableBalance = availableBalance;
    }

    public int getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(int statusCode) {
        StatusCode = statusCode;
    }

    public String getStatusDesc() {
        return StatusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        StatusDesc = statusDesc;
    }
}
