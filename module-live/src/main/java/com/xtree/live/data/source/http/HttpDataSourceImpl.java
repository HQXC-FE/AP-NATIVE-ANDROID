package com.xtree.live.data.source.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xtree.base.net.live.LiveClient;
import com.xtree.base.net.live.X9LiveInfo;
import com.xtree.base.utils.AESUtil;
import com.xtree.base.vo.FBService;
import com.xtree.live.data.source.APIManager;
import com.xtree.live.data.source.ApiService;
import com.xtree.live.data.source.HttpDataSource;
import com.xtree.live.data.source.request.AnchorSortRequest;
import com.xtree.live.data.source.request.AttentionRequest;
import com.xtree.live.data.source.request.FrontLivesRequest;
import com.xtree.live.data.source.request.LiveTokenRequest;
import com.xtree.live.data.source.request.MatchDetailRequest;
import com.xtree.live.data.source.response.AnchorSortResponse;
import com.xtree.live.data.source.response.FrontLivesResponse;
import com.xtree.live.data.source.response.LiveTokenResponse;
import com.xtree.live.data.source.response.ReviseHotResponse;
import com.xtree.live.data.source.response.fb.MatchInfo;
import com.xtree.live.ui.main.model.anchorList.AttentionListModel;

import java.util.HashMap;
import java.util.List;
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
    private ApiService apiService;
    private ApiService liveService;
    private ApiService fbService;

    private HttpDataSourceImpl(ApiService apiService, ApiService liveService, ApiService fbService) {
        this.apiService = apiService;
        this.liveService = liveService;
        this.fbService = fbService;
    }

    public static HttpDataSourceImpl getInstance(ApiService apiService, ApiService liveService, ApiService fbService) {
        if (INSTANCE == null) {
            synchronized (HttpDataSourceImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpDataSourceImpl(apiService, liveService, fbService);
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
    public ApiService getApiService() {
        return apiService;
    }

    @Override
    public ApiService getLiveService() {
        return liveService;
    }

    @Override
    public void setLive(LiveTokenResponse liveData) {

        X9LiveInfo.INSTANCE.setChannel(liveData.getChannelCode());
        X9LiveInfo.INSTANCE.setToken(liveData.getXLiveToken());
        X9LiveInfo.INSTANCE.setVisitor(liveData.getVisitorId());

        //抓包可去掉证书replace("https", "http")
        LiveClient.setApi(liveData.getAppApi().get(0));
        liveService = LiveClient.getInstance().create(ApiService.class);
    }

    @Override
    public Flowable<BaseResponse<LiveTokenResponse>> getLiveToken(LiveTokenRequest request) {
        Map<String, Object> map = JSON.parseObject(JSON.toJSONString(request), type);
        return apiService.get(APIManager.X9_TOKEN_URL, map).map(new Function<ResponseBody, BaseResponse<LiveTokenResponse>>() {
            @Override
            public BaseResponse<LiveTokenResponse> apply(ResponseBody responseBody) throws Exception {
                return JSON.parseObject(responseBody.string(),
                        new TypeReference<BaseResponse<LiveTokenResponse>>() {
                        });
            }
        });
    }

    /**
     * 获取正在直播中的主播列表
     *
     * @param request
     * @return
     */
    @Override
    public Flowable<BaseResponse<List<FrontLivesResponse>>> getFrontLives(FrontLivesRequest request) {
        Map<String, Object> map = JSON.parseObject(JSON.toJSONString(request), type);
        return liveService.get(APIManager.FRONT_LIVES, map).map(responseBody -> JSON.parseObject(responseBody.string(),
                new TypeReference<BaseResponse<List<FrontLivesResponse>>>() {
                }));
    }

    @Override
    public Flowable<BaseResponse<AttentionListModel>> getAttention(AttentionRequest request) {
        Map<String, Object> map = JSON.parseObject(JSON.toJSONString(request), type);
        return liveService.get(APIManager.ATTENTION_API, map).map(new Function<ResponseBody, BaseResponse<AttentionListModel>>() {
            @Override
            public BaseResponse<AttentionListModel> apply(ResponseBody responseBody) throws Exception {
                return JSON.parseObject(responseBody.string(),
                        new TypeReference<BaseResponse<AttentionListModel>>() {

                        });
            }
        });
    }

    /**
     * 获取主播列表
     *
     * @param request
     * @return
     */
    @Override
    public Flowable<BaseResponse<AnchorSortResponse>> getAnchorSort(AnchorSortRequest request) {
        Map<String, Object> map = JSON.parseObject(JSON.toJSONString(request), type);
        return liveService.get(APIManager.ANCHOR_SORT_API, map).map(new Function<ResponseBody, BaseResponse<AnchorSortResponse>>() {
            @Override
            public BaseResponse<AnchorSortResponse> apply(ResponseBody responseBody) throws Exception {
                return JSON.parseObject(responseBody.string(),
                        new TypeReference<BaseResponse<AnchorSortResponse>>() {

                        });
            }
        });
    }

    @Override
    public Flowable<BaseResponse<ReviseHotResponse>> getReviseHot() {
        return liveService.get(APIManager.ReviseHot_URL).map(new Function<ResponseBody, BaseResponse<ReviseHotResponse>>() {
            @Override
            public BaseResponse<ReviseHotResponse> apply(ResponseBody responseBody) throws Exception {
                BaseResponse<String> response = JSON.parseObject(responseBody.string(),
                        new TypeReference<BaseResponse<String>>() {
                        });

                BaseResponse<ReviseHotResponse> reviseHotResponse = new BaseResponse<ReviseHotResponse>();
                reviseHotResponse.setCode(response.getCode());
                reviseHotResponse.setMessage(response.getMessage());
                if (response.getCode() == 0 && response.getData() != null) {
                    String json = AESUtil.decryptLiveData(response.getData(), "PZI8BvcUw7yg2st3");
                    ReviseHotResponse reviseHot = JSON.parseObject(json, ReviseHotResponse.class);
                    reviseHotResponse.setData(reviseHot);
                }
                return reviseHotResponse;
            }
        });
    }

    @Override
    public Flowable<BaseResponse<FBService>> getFBGameTokenApi() {
        return apiService.post(APIManager.FB_GET_TOKEN, new HashMap<>()).map(responseBody -> JSON.parseObject(responseBody.string(),
                new TypeReference<BaseResponse<FBService>>() {

                }));
    }

    @Override
    public Flowable<BaseResponse<FBService>> getFBXCGameTokenApi() {
        return apiService.post(APIManager.FBXC_GET_TOKEN, new HashMap<>()).map(responseBody -> JSON.parseObject(responseBody.string(),
                new TypeReference<BaseResponse<FBService>>() {

                }));
    }

    /**
     * 获取赛事详情
     *
     * @param request
     * @return
     */
    @Override
    public Flowable<BaseResponse<MatchInfo>> getMatchDetail(MatchDetailRequest request) {
        Map<String, Object> map = JSON.parseObject(JSON.toJSONString(request), type);
        return fbService.postJson(APIManager.GET_MATCHDETAIL, map).map(responseBody -> JSON.parseObject(responseBody.string(),
                new TypeReference<BaseResponse<MatchInfo>>() {
                }));
    }


}
