package com.xtree.lottery.data.source;

import com.xtree.base.vo.UserMethodsResponse;
import com.xtree.lottery.data.source.request.LotteryBetRequest;
import com.xtree.lottery.data.source.request.LotteryCopyBetRequest;
import com.xtree.lottery.data.source.response.BalanceResponse;
import com.xtree.lottery.data.source.response.HandicapResponse;
import com.xtree.lottery.data.source.response.MenuMethodsResponse;

import java.util.Map;

import io.reactivex.Flowable;
import me.xtree.mvvmhabit.http.BaseResponse;

/**
 * Created by goldze on 2019/3/26.
 */
public interface HttpDataSource {
    LotteryApiService getApiService();

    /**
     * 彩票投注-用户玩法权限
     */
    Flowable<UserMethodsResponse> getUserMethodsData();

    /**
     * 彩票投注-玩法菜单
     */
    Flowable<MenuMethodsResponse> getMenuMethodsData(String lotteryName);

    /**
     * 盘口玩法-玩法菜单
     */
    Flowable<HandicapResponse> getHandicapData(String lotteryName);

    /**
     * 获取用户余额
     */
    Flowable<BalanceResponse> getUserBalance();

    /**
     * 彩票投注
     */
    Flowable<BaseResponse> bet(LotteryBetRequest betRequest, Map<String, Object> params);

    /**
     * 再来一注
     */
    Flowable<BaseResponse> copyBet(LotteryCopyBetRequest betRequest);
}
