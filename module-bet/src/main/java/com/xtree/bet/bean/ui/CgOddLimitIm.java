package com.xtree.bet.bean.ui;

import android.os.Parcel;
import android.text.TextUtils;

import com.xtree.bet.bean.response.fb.BtConfirmOptionInfo;
import com.xtree.bet.bean.response.fb.CgOddLimitInfo;
import com.xtree.bet.bean.response.im.BetSetting;
import com.xtree.bet.bean.response.im.WagerSelectionInfo;
import com.xtree.bet.manager.BtCarManager;

import java.util.HashMap;
import java.util.Map;

/**
 * 杏彩体育（IM）
 */
public class CgOddLimitIm implements CgOddLimit {
    /**
     * 比赛场数
     */
    private int matchCount;

    /**
     * 下注金额
     */
    private double btCount;
    //    private CgOddLimitInfo cgOddLimitInfo;
    private BetSetting betSetting;
    private WagerSelectionInfo wagerSelectionInfo;

    private final Map<String, String> cgName = new HashMap<>();

    public CgOddLimitIm(BetSetting betSetting, WagerSelectionInfo wagerSelectionInfo, int matchCount) {
        this.betSetting = betSetting;
        this.wagerSelectionInfo = wagerSelectionInfo;
        this.matchCount = matchCount;
        cgName.put("0", "单关");
        cgName.put("1", "3串4");
        cgName.put("2", "4串11");
        cgName.put("3", "5串26");
        cgName.put("4", "6串57");
        cgName.put("5", "7串120");
        cgName.put("6", "8串247");
        cgName.put("7", "9串502");
        cgName.put("8", "10串1013");
        cgName.put("9", "2串1");
        cgName.put("10", "3串1");
        cgName.put("11", "4串1");
        cgName.put("12", "5串1");
        cgName.put("13", "6串1");
        cgName.put("14", "7串1");
        cgName.put("15", "8串1");
        cgName.put("16", "9串1");
        cgName.put("17", "10串1");
        cgName.put("18", "所有连串过关");
    }

    @Override
    public int getCgCount() {
        if (betSetting != null) {
            return betSetting.getNoOfCombination();
        } else {
            return 1;
        }
    }

    @Override
    public String getCgName() {
        if (betSetting == null) {
            return "";
        } else {
            return cgName.getOrDefault(String.valueOf(betSetting.getComboSelection()), "");
        }

    }

    @Override
    public String getCgType() {
        return String.valueOf(betSetting.getComboSelection());
    }

    @Override
    public double getDMin() {
        if (betSetting == null) {
            return 5;
        }
        return betSetting.getMinStakeAmount();
    }

    @Override
    public double getDMax() {
        if (betSetting == null) {
            return 5;
        }
        return betSetting.getMaxStakeAmount();
    }

    @Override
    public double getCMin() {
        if (betSetting == null) {
            return 5;
        }
        return betSetting.getMinStakeAmount();
    }

    @Override
    public double getCMax() {
        if (betSetting == null) {
            return 5;
        }
        return betSetting.getMaxStakeAmount();
    }

    @Override
    public double getDOdd() {
        if (wagerSelectionInfo == null) {
            return 0;
        }
        return wagerSelectionInfo.getOdds();
    }

    @Override
    public double getCOdd() {
        if (wagerSelectionInfo == null) {
            return 0;
        }
        return wagerSelectionInfo.getOdds();
    }

    @Override
    public double getWin(double amount) {
        if (betSetting == null) return amount;
        return betSetting.getEstimatedPayoutAmount() * amount;
    }

    @Override
    public int getBtCount() {
        if(betSetting == null || TextUtils.isEmpty(getCgName())){
            return 1;
        }
        String cgName = getCgName();
        if(TextUtils.equals("1", getCgType())){
            return 1;
        }
//        String[] ints = cgName.split("串");
//        int btCount = Integer.valueOf(ints[1]);
//        if(btCount == 1){
//            btCount = calculate(BtCarManager.size(), Integer.valueOf(ints[0]));
//        }

        return 1;
    }

    /**
     * 设置投注金额
     *
     * @return
     */
    @Override
    public void setBtAmount(double count) {
        btCount = count;
    }

    /**
     * 获取投注金额
     *
     * @return
     */
    @Override
    public double getBtAmount() {
        return btCount;
    }

    /**
     * 获取总投注金额
     *
     * @return
     */
    @Override
    public double getBtTotalAmount() {
        return btCount * getBtCount();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.matchCount);
        dest.writeDouble(this.btCount);
        dest.writeParcelable(this.betSetting, flags);
        dest.writeParcelable(this.wagerSelectionInfo, flags);
    }

    public void readFromParcel(Parcel source) {
        this.matchCount = source.readInt();
        this.btCount = source.readDouble();
        this.betSetting = source.readParcelable(BetSetting.class.getClassLoader());
        this.wagerSelectionInfo = source.readParcelable(WagerSelectionInfo.class.getClassLoader());
    }

    protected CgOddLimitIm(Parcel in) {
        this.matchCount = in.readInt();
        this.btCount = in.readDouble();
        this.betSetting = in.readParcelable(BetSetting.class.getClassLoader());
        this.wagerSelectionInfo = in.readParcelable(WagerSelectionInfo.class.getClassLoader());
    }

    public static final Creator<CgOddLimitIm> CREATOR = new Creator<CgOddLimitIm>() {
        @Override
        public CgOddLimitIm createFromParcel(Parcel source) {
            return new CgOddLimitIm(source);
        }

        @Override
        public CgOddLimitIm[] newArray(int size) {
            return new CgOddLimitIm[size];
        }
    };
}
