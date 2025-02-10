package com.xtree.lottery.ui.lotterybet.model;

import com.xtree.base.vo.UserMethodsResponse;

//奖金玩法和投注数据
public class LotteryBetsPrizeGroup {

    public final LotteryBetsModel betModel;
    public final UserMethodsResponse.DataDTO.PrizeGroupDTO prizeGroup;
    public final boolean isPrize;//六合彩需要奖金组展示赔率

    public LotteryBetsPrizeGroup(LotteryBetsModel betModel, UserMethodsResponse.DataDTO.PrizeGroupDTO prizeGroup, boolean isPrize) {
        this.betModel = betModel;
        this.prizeGroup = prizeGroup;
        this.isPrize = isPrize;
    }
}
