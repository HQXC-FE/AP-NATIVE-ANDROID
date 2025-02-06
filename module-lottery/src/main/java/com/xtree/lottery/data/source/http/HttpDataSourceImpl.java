package com.xtree.lottery.data.source.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xtree.base.vo.UserMethodsResponse;
import com.xtree.lottery.data.source.APIManager;
import com.xtree.lottery.data.source.HttpDataSource;
import com.xtree.lottery.data.source.LotteryApiService;
import com.xtree.lottery.data.source.request.LotteryBetRequest;
import com.xtree.lottery.data.source.request.LotteryCopyBetRequest;
import com.xtree.lottery.data.source.response.BalanceResponse;
import com.xtree.lottery.data.source.response.HandicapResponse;
import com.xtree.lottery.data.source.response.MenuMethodsResponse;

import java.util.HashMap;
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
    private volatile static HttpDataSourceImpl INSTANCE = null;
    private LotteryApiService apiService;

    private HttpDataSourceImpl(LotteryApiService apiService) {
        this.apiService = apiService;
    }

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
                        new TypeReference<UserMethodsResponse>() {
                        });
            }
        });
    }

    @Override
    public Flowable<MenuMethodsResponse> getMenuMethodsData(String lotteryName) {
        return apiService.get(String.format(APIManager.MENU_METHODS_URL, lotteryName)).map(new Function<ResponseBody, MenuMethodsResponse>() {
            @Override
            public MenuMethodsResponse apply(ResponseBody responseBody) throws Exception {
                return JSON.parseObject(responseBody.string(),
                        new TypeReference<MenuMethodsResponse>() {
                        });
            }
        });
    }

    @Override
    public Flowable<HandicapResponse> getHandicapData(String lotteryName) {
        return apiService.get(String.format(APIManager.HANDICAP_METHODS_URL, lotteryName)).map(new Function<ResponseBody, HandicapResponse>() {
            @Override
            public HandicapResponse apply(ResponseBody responseBody) throws Exception {
                return JSON.parseObject(responseBody.string(),
                        new TypeReference<HandicapResponse>() {
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
                        new TypeReference<BalanceResponse>() {
                        });
            }
        });
    }

    @Override
    public Flowable<BaseResponse> bet(LotteryBetRequest betRequest, Map<String, Object> params) {
        Map<String, Object> map = JSON.parseObject(JSON.toJSONString(betRequest), type);
        if (params != null) {
            map.putAll(params);
        }
        return apiService.post(APIManager.BET_URL, map, "application/vnd.sc-api.v1.json").map(new Function<ResponseBody, BaseResponse>() {
            @Override
            public BaseResponse apply(ResponseBody responseBody) throws Exception {
                return JSON.parseObject(responseBody.string(),
                        new TypeReference<BaseResponse>() {
                        });
            }
        });
    }

    @Override
    public Flowable<BaseResponse> copyBet(LotteryCopyBetRequest betRequest) {
        Map<String, Object> map = JSON.parseObject(JSON.toJSONString(betRequest), type);
        return apiService.post(APIManager.COPY_BET_URL, new HashMap<String, Object>() {{
            put("client", "m");
        }}, map, "application/vnd.sc-api.v1.json").map(new Function<ResponseBody, BaseResponse>() {
            @Override
            public BaseResponse apply(ResponseBody responseBody) throws Exception {
                return JSON.parseObject(responseBody.string(),
                        new TypeReference<BaseResponse>() {
                        });
            }
        });
    }

}
