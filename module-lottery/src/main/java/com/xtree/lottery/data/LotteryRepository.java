package com.xtree.lottery.data;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import com.xtree.base.vo.UserMethodsResponse;
import com.xtree.lottery.data.source.HttpDataSource;
import com.xtree.lottery.data.source.LocalDataSource;
import com.xtree.lottery.data.source.LotteryApiService;
import com.xtree.lottery.data.source.request.LotteryBetRequest;
import com.xtree.lottery.data.source.request.LotteryCopyBetRequest;
import com.xtree.lottery.data.source.response.BalanceResponse;
import com.xtree.lottery.data.source.response.HandicapResponse;
import com.xtree.lottery.data.source.response.MenuMethodsResponse;
import com.xtree.lottery.data.source.vo.BetResult;
import com.xtree.lottery.data.source.vo.SimulatedNumber;

import java.util.Map;

import io.reactivex.Flowable;
import me.xtree.mvvmhabit.base.BaseModel;
import me.xtree.mvvmhabit.http.BaseResponse;
import me.xtree.mvvmhabit.utils.RxUtils;

/**
 * MVVM的Model层，统一模块的数据仓库，包含网络数据和本地数据（一个应用可以有多个Repositor）
 */
public class LotteryRepository extends BaseModel implements HttpDataSource, LocalDataSource {
    private volatile static LotteryRepository INSTANCE = null;
    private final HttpDataSource mHttpDataSource;

    private final LocalDataSource mLocalDataSource;

    private LotteryRepository(@NonNull HttpDataSource httpDataSource,
                              @NonNull LocalDataSource localDataSource) {
        this.mHttpDataSource = httpDataSource;
        this.mLocalDataSource = localDataSource;
    }

    public static LotteryRepository getInstance(HttpDataSource httpDataSource,
                                                LocalDataSource localDataSource) {
        if (INSTANCE == null) {
            synchronized (LotteryRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LotteryRepository(httpDataSource, localDataSource);
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public LotteryApiService getApiService() {
        return mHttpDataSource.getApiService();
    }

    @Override
    public Flowable<UserMethodsResponse> getUserMethodsData() {
        return mHttpDataSource.getUserMethodsData()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer());
    }

    @Override
    public Flowable<MenuMethodsResponse> getMenuMethodsData(String lotteryName) {
        return mHttpDataSource.getMenuMethodsData(lotteryName)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer());
    }

    @Override
    public Flowable<HandicapResponse> getHandicapData(String lotteryName) {
        return mHttpDataSource.getHandicapData(lotteryName)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer());
    }

    @Override
    public Flowable<BalanceResponse> getUserBalance() {
        return mHttpDataSource.getUserBalance()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer());
    }

    @Override
    public Flowable<BaseResponse> bet(LotteryBetRequest betRequest, Map<String, Object> params) {
        return mHttpDataSource.bet(betRequest, params)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer());
    }

    @Override
    public Flowable<BaseResponse<BetResult>> mmcBet(LotteryBetRequest betRequest, Map<String, Object> params) {
        return mHttpDataSource.mmcBet(betRequest, params);
    }

    @Override
    public Flowable<BaseResponse> copyBet(LotteryCopyBetRequest betRequest) {
        return mHttpDataSource.copyBet(betRequest)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer());
    }

    @Override
    public Flowable<BaseResponse<SimulatedNumber>> simulatedNumber(String id) {
        return mHttpDataSource.simulatedNumber(id);
    }
}
