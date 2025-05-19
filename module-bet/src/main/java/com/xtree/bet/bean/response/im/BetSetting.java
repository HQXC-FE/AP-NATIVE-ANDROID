package com.xtree.bet.bean.response.im;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class BetSetting implements Parcelable {

    @SerializedName("MaxStakeAmount")
    private double maxStakeAmount;

    @SerializedName("MinStakeAmount")
    private double minStakeAmount;

    @SerializedName("NoOfCombination")
    private int noOfCombination;

    @SerializedName("ComboSelection")
    private int comboSelection;

    @SerializedName("EstimatedPayoutAmount")
    private double estimatedPayoutAmount;

    @SerializedName("RefId")
    private long refId;

    // 构造函数
    public BetSetting() {
    }

    protected BetSetting(Parcel in) {
        maxStakeAmount = in.readDouble();
        minStakeAmount = in.readDouble();
        noOfCombination = in.readInt();
        comboSelection = in.readInt();
        estimatedPayoutAmount = in.readDouble();
        refId = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(maxStakeAmount);
        dest.writeDouble(minStakeAmount);
        dest.writeInt(noOfCombination);
        dest.writeInt(comboSelection);
        dest.writeDouble(estimatedPayoutAmount);
        dest.writeLong(refId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BetSetting> CREATOR = new Creator<BetSetting>() {
        @Override
        public BetSetting createFromParcel(Parcel in) {
            return new BetSetting(in);
        }

        @Override
        public BetSetting[] newArray(int size) {
            return new BetSetting[size];
        }
    };

    // Getters 和 Setters

    public double getMaxStakeAmount() {
        return maxStakeAmount;
    }

    public void setMaxStakeAmount(double maxStakeAmount) {
        this.maxStakeAmount = maxStakeAmount;
    }

    public double getMinStakeAmount() {
        return minStakeAmount;
    }

    public void setMinStakeAmount(double minStakeAmount) {
        this.minStakeAmount = minStakeAmount;
    }

    public int getNoOfCombination() {
        return noOfCombination;
    }

    public void setNoOfCombination(int noOfCombination) {
        this.noOfCombination = noOfCombination;
    }

    public int getComboSelection() {
        return comboSelection;
    }

    public void setComboSelection(int comboSelection) {
        this.comboSelection = comboSelection;
    }

    public double getEstimatedPayoutAmount() {
        return estimatedPayoutAmount;
    }

    public void setEstimatedPayoutAmount(double estimatedPayoutAmount) {
        this.estimatedPayoutAmount = estimatedPayoutAmount;
    }

    public long getRefId() {
        return refId;
    }

    public void setRefId(long refId) {
        this.refId = refId;
    }
}
