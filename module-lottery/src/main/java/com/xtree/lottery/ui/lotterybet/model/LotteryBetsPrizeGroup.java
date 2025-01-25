package com.xtree.lottery.ui.lotterybet.model;

import com.xtree.base.vo.UserMethodsResponse;

//奖金玩法和投注数据
public class LotteryBetsPrizeGroup {

    public final LotteryBetsModel betModel;
    public final UserMethodsResponse.DataDTO.PrizeGroupDTO prizeGroup;

    public LotteryBetsPrizeGroup(LotteryBetsModel betModel, UserMethodsResponse.DataDTO.PrizeGroupDTO prizeGroup) {
        this.betModel = betModel;
        this.prizeGroup = prizeGroup;
    }
}
