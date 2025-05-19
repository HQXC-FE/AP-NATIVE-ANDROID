package com.xtree.bet.bean.response.im;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.xtree.base.vo.BaseBean;

import java.util.ArrayList;
import java.util.List;

public class BetInfo implements BaseBean {

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

    protected BetInfo(Parcel in) {
        wagerSelectionInfos = in.createTypedArrayList(WagerSelectionInfo.CREATOR);
        betSetting = in.createTypedArrayList(BetSetting.CREATOR);
        serverTime = in.readString();
        statusCode = in.readInt();
        statusDesc = in.readString();
    }

    public static final Creator<BetInfo> CREATOR = new Creator<BetInfo>() {
        @Override
        public BetInfo createFromParcel(Parcel in) {
            return new BetInfo(in);
        }

        @Override
        public BetInfo[] newArray(int size) {
            return new BetInfo[size];
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


}

