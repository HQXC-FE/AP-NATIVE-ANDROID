package com.xtree.mine.ui.rebateagrt.model;

import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.mine.R;

import me.xtree.mvvmhabit.base.BaseApplication;

/**
 * Created by KAKA on 2024/3/11.
 * Describe: 游戏场馆下级返水列表数据
 */
public class GameSubordinaterebateModel extends BindModel {

    private String userName;
    private String bet;
    private String effectBet;
    private String ratio;
    private String totalMoney;
    private String selfMoney;
    private String subMoney;
    private String type;
    private String pstatus;
    private String statusString = BaseApplication.getInstance().getString(R.string.txt_unreceived);
    private int statusColor = R.color.clr_txt_rebateagrt_default;
    private String createTime;
    private String createDate;
    //场馆类型
    private RebateAreegmentTypeEnum typeEnum;

    public void setTypeEnum(RebateAreegmentTypeEnum typeEnum) {
        this.typeEnum = typeEnum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBet() {
        return bet;
    }

    public void setBet(String bet) {
        this.bet = bet;
    }

    public String getEffectBet() {
        return effectBet;
    }

    public void setEffectBet(String effectBet) {
        this.effectBet = effectBet;
    }

    public String getRatio() {
        if (typeEnum != null) {
            switch (typeEnum) {
                case USER:
                    return ratio + "%";
                default:
                    return ratio + "%";
            }
        }
        return ratio;
    }

    public void setRatio(String ratio) {
        if (ratio == null) {
            this.ratio = "0";
        } else {
            this.ratio = ratio;
        }
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getSelfMoney() {
        return selfMoney;
    }

    public void setSelfMoney(String selfMoney) {
        this.selfMoney = selfMoney;
    }

    public String getSubMoney() {
        return subMoney;
    }

    public void setSubMoney(String subMoney) {
        this.subMoney = subMoney;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
    }

    public String getPstatus() {
        return pstatus;
    }

    public void setPstatus(String pstatus) {
        if (pstatus != null && !pstatus.isEmpty()) {
            this.pstatus = pstatus;
            switch (pstatus) {
                case "1":
                    statusString = BaseApplication.getInstance().getString(R.string.txt_received);
                    statusColor = R.color.clr_txt_rebateagrt_success;
                    break;
                case "4":
                    statusString = BaseApplication.getInstance().getString(R.string.txt_unreceived);
                    statusColor = R.color.clr_txt_rebateagrt_default;
                    break;
            }
        }
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getStatusString() {
        return statusString;
    }

    public int getStatusColor() {
        return BaseApplication.getInstance().getResources().getColor(statusColor);
    }
}
