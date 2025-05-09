package com.xtree.lottery.ui.lotterybet.data;

import com.xtree.lottery.ui.view.model.LotteryMoneyModel;

/**
 * Created by KAKA on 2024/5/15.
 * Describe: 投注金额设置数据
 */
public class LotteryMoneyData {
    private LotteryMoneyModel moneyModel;
    private long factor = 1;

    public LotteryMoneyData(LotteryMoneyModel moneyModel, long factor) {
        this.moneyModel = moneyModel;
        this.factor = factor;
    }

    public LotteryMoneyModel getMoneyModel() {
        return moneyModel;
    }

    public void setMoneyModel(LotteryMoneyModel moneyModel) {
        this.moneyModel = moneyModel;
    }

    public long getFactor() {
        return factor;
    }

    public void setFactor(int factor) {
        this.factor = factor;
    }
}
