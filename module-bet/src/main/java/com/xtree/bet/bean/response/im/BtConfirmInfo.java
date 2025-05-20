package com.xtree.bet.bean.response.im;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;
import com.xtree.base.vo.BaseBean;

import java.util.List;

public class BtConfirmInfo implements BaseBean {

    /**
     * 对应FB和BT的BtConfirmOptionInfo
     */
    @SerializedName("WagerSelectionInfos")
    private List<WagerSelectionInfo> wagerSelectionInfos;
    @SerializedName("BetSetting")
    private List<BetSetting> betSetting;

    @SerializedName("ServerTime")
    private String serverTime;

    @SerializedName("StatusCode")
    private int statusCode;

    @SerializedName("StatusDesc")
    private String statusDesc;

    protected BtConfirmInfo(Parcel in) {
        wagerSelectionInfos = in.createTypedArrayList(WagerSelectionInfo.CREATOR);
        betSetting = in.createTypedArrayList(BetSetting.CREATOR);
        serverTime = in.readString();
        statusCode = in.readInt();
        statusDesc = in.readString();
    }

    public static final Creator<BtConfirmInfo> CREATOR = new Creator<BtConfirmInfo>() {
        @Override
        public BtConfirmInfo createFromParcel(Parcel in) {
            return new BtConfirmInfo(in);
        }

        @Override
        public BtConfirmInfo[] newArray(int size) {
            return new BtConfirmInfo[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(wagerSelectionInfos);
        dest.writeTypedList(betSetting);
        dest.writeString(serverTime);
        dest.writeInt(statusCode);
        dest.writeString(statusDesc);
    }

    @Override
    public int describeContents() {
        return 0;
    }


    public List<WagerSelectionInfo> getWagerSelectionInfos() {
        return wagerSelectionInfos;
    }

    public void setWagerSelectionInfos(List<WagerSelectionInfo> wagerSelectionInfos) {
        this.wagerSelectionInfos = wagerSelectionInfos;
    }

    public List<BetSetting> getBetSetting() {
        return betSetting;
    }

    public void setBetSetting(List<BetSetting> betSetting) {
        this.betSetting = betSetting;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }
}

