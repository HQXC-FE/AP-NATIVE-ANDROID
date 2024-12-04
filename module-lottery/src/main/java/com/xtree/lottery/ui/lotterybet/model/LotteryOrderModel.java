package com.xtree.lottery.ui.lotterybet.model;

import androidx.databinding.ObservableField;

import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.lottery.data.source.request.LotteryBetRequest;
import com.xtree.lottery.ui.lotterybet.data.LotteryMoneyData;

/**
 * Created by KAKA on 2024/5/15.
 * Describe: 投注订单数据模型
 */
public class LotteryOrderModel extends BindModel {
    public LotteryBetRequest.BetOrderData betOrderData;
    public ObservableField<String> betMoney = new ObservableField<>("1倍");
    private String prizeLabel;
    private LotteryMoneyData moneyData;

    public LotteryBetRequest.BetOrderData getBetOrderData() {
        return betOrderData;
    }

    public void setBetOrderData(LotteryBetRequest.BetOrderData betOrderData) {
        this.betOrderData = betOrderData;
    }

    public String getPrizeLabel() {
        return prizeLabel;
    }

    public void setPrizeLabel(String prizeLabel) {
        this.prizeLabel = prizeLabel;
    }

    public LotteryMoneyData getMoneyData() {
        return moneyData;
    }

    public void setMoneyData(LotteryMoneyData moneyData) {
        this.moneyData = moneyData;
    }
}
