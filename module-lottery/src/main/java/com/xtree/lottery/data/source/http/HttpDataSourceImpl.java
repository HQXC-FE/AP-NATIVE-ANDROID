package com.xtree.lottery.data.source.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xtree.base.vo.UserMethodsResponse;
import com.xtree.lottery.data.source.APIManager;
import com.xtree.lottery.data.source.HttpDataSource;
import com.xtree.lottery.data.source.LotteryApiService;
import com.xtree.lottery.data.source.request.BonusNumbersRequest;
import com.xtree.lottery.data.source.request.LotteryBetRequest;
import com.xtree.lottery.data.source.response.BalanceResponse;
import com.xtree.lottery.data.source.response.BonusNumbersResponse;
import com.xtree.lottery.data.source.response.HandicapResponse;
import com.xtree.lottery.data.source.response.MenuMethodsResponse;

import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import me.xtree.mvvmhabit.http.BaseResponse;
import okhttp3.ResponseBody;

/**
 * Created by goldze on 2019/3/26.
 */
public class HttpDataSourceImpl implements HttpDataSource {
    private static TypeReference<Map<String, Object>> type;
    private LotteryApiService apiService;
    private volatile static HttpDataSourceImpl INSTANCE = null;

    public static HttpDataSourceImpl getInstance(LotteryApiService apiService) {
            if (INSTANCE == null) {
                synchronized (HttpDataSourceImpl.class) {
                    if (INSTANCE == null) {
                        INSTANCE = new HttpDataSourceImpl(apiService);
                        type = new TypeReference<Map<String, Object>>() {
                        };
                    }
                }
            }

        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private HttpDataSourceImpl(LotteryApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public LotteryApiService getApiService() {
        return apiService;
    }

    @Override
    public Flowable<UserMethodsResponse> getUserMethodsData() {
        return apiService.get(APIManager.USER_METHODS_URL).map(new Function<ResponseBody, UserMethodsResponse>() {
            @Override
            public UserMethodsResponse apply(ResponseBody responseBody) throws Exception {
                return JSON.parseObject(responseBody.string(),
                        new TypeReference<UserMethodsResponse>() {});
            }
        });
    }

    @Override
    public Flowable<MenuMethodsResponse> getMenuMethodsData(String lotteryName) {
        return apiService.get(String.format(APIManager.MENU_METHODS_URL, lotteryName)).map(new Function<ResponseBody, MenuMethodsResponse>() {
            @Override
            public MenuMethodsResponse apply(ResponseBody responseBody) throws Exception {
                return JSON.parseObject(responseBody.string(),
                        new TypeReference<MenuMethodsResponse>() {});
            }
        });
    }

    @Override
    public Flowable<HandicapResponse> getHandicapData(String lotteryName) {
        return apiService.get(String.format(APIManager.HANDICAP_METHODS_URL, lotteryName)).map(new Function<ResponseBody, HandicapResponse>() {
            @Override
            public HandicapResponse apply(ResponseBody responseBody) throws Exception {
                return JSON.parseObject(responseBody.string(),
                        new TypeReference<HandicapResponse>() {});
            }
        });
    }

    @Override
    public Flowable<BonusNumbersResponse> getBonusNumbersData(String lotteryId, BonusNumbersRequest request) {
        Map<String, Object> map = JSON.parseObject(JSON.toJSONString(request), type);
        return apiService.get(String.format(APIManager.BONUS_NUMBERS_URL, lotteryId), map).map(new Function<ResponseBody, BonusNumbersResponse>() {
            @Override
            public BonusNumbersResponse apply(ResponseBody responseBody) throws Exception {
                return JSON.parseObject(responseBody.string(),
                        new TypeReference<BonusNumbersResponse>() {
                        });
            }
        });
    }

    @Override
    public Flowable<BalanceResponse> getUserBalance() {
        return apiService.get(APIManager.BALANCE_URL).map(new Function<ResponseBody, BalanceResponse>() {
            @Override
            public BalanceResponse apply(ResponseBody responseBody) throws Exception {
                return JSON.parseObject(responseBody.string(),
                        new TypeReference<BalanceResponse>() {});
            }
        });
    }

    @Override
    public Flowable<BaseResponse> bet(LotteryBetRequest betRequest, Map<String, Object> params) {
        Map<String, Object> map = JSON.parseObject(JSON.toJSONString(betRequest), type);
        if (params != null) {
            map.putAll(params);
        }
        return apiService.post(APIManager.BET_URL, map).map(new Function<ResponseBody, BaseResponse>() {
            @Override
            public BaseResponse apply(ResponseBody responseBody) throws Exception {
                return JSON.parseObject(responseBody.string(),
                        new TypeReference<BaseResponse>() {
                        });
            }
        });
    }

}
