package com.xtree.live.data.source.http;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.FBRetrofitClient;
import com.xtree.base.net.live.X9LiveInfo;
import com.xtree.base.utils.AESUtil;
import com.xtree.base.utils.CfLog;
import com.xtree.base.vo.FBService;
import com.xtree.bet.bean.request.fb.FBListReq;
import com.xtree.bet.bean.response.HotLeagueInfo;
import com.xtree.bet.bean.response.fb.MatchListRsp;
import com.xtree.live.LiveConfig;
import com.xtree.live.data.source.APIManager;
import com.xtree.live.data.source.ApiService;
import com.xtree.live.data.source.HttpDataSource;
import com.xtree.live.data.source.request.AnchorSortRequest;
import com.xtree.live.data.source.request.ChatRoomListRequest;
import com.xtree.live.data.source.request.FrontLivesRequest;
import com.xtree.live.data.source.request.LiveTokenRequest;
import com.xtree.live.data.source.request.MatchDetailRequest;
import com.xtree.live.data.source.request.SearchAssistantRequest;
import com.xtree.live.data.source.request.SendToAssistantRequest;
import com.xtree.live.data.source.request.SubscriptionRequest;
import com.xtree.live.data.source.response.AnchorSortResponse;
import com.xtree.live.data.source.response.BannerResponse;
import com.xtree.live.data.source.response.ChatRoomResponse;
import com.xtree.live.data.source.response.FrontLivesResponse;
import com.xtree.live.data.source.response.LiveRoomBean;
import com.xtree.live.data.source.response.LiveTokenResponse;
import com.xtree.live.data.source.response.ReviseHotResponse;
import com.xtree.live.data.source.response.SearchAssistantResponse;
import com.xtree.live.data.source.response.SendToAssistantResponse;
import com.xtree.live.data.source.response.fb.MatchInfo;
import com.xtree.live.message.MessageRecord;
import com.xtree.live.message.SystemMessageRecord;
import com.xtree.live.model.AccumulatedRechargeRes;
import com.xtree.live.ui.main.model.chat.AnchorChatHistoryRequest;
import com.xtree.live.ui.main.model.chat.GetChatHistroyRequest;
import com.xtree.live.ui.main.model.chat.GetRoomInfoRequest;
import com.xtree.live.ui.main.model.chat.LiveThiredLoginRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import me.xtree.mvvmhabit.http.BaseResponse;
import me.xtree.mvvmhabit.utils.SPUtils;
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

    private HttpDataSourceImpl(ApiService apiService, ApiService liveService) {
        this.apiService = apiService;
        this.liveService = liveService;
    }

    public static HttpDataSourceImpl getInstance(ApiService apiService, ApiService liveService) {
        if (INSTANCE == null) {
            synchronized (HttpDataSourceImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpDataSourceImpl(apiService, liveService);
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
        //Todo 这边我不确定会有多少疙 api 所以战时设定成第一位
        X9LiveInfo.INSTANCE.setWebApi(liveData.getWebApi().get(0));
        X9LiveInfo.INSTANCE.setAppApi(liveData.getAppApi().get(0));
        LiveConfig.setLiveToken(liveData.getXLiveToken());
        LiveConfig.setChannelCode(liveData.getChannelCode());
        LiveConfig.setVisitorId(liveData.getVisitorId());
        LiveConfig.setAppApi(liveData.getAppApi().get(0));


        //抓包可去掉证书replace("https", "http")
        LiveClient.setApi(liveData.getAppApi().get(0));
        /*LiveClient.setApi("http://zhibo-apps.oxldkm.com");*/
        CfLog.e("setLive ---> " + liveData.toString());
        liveService = LiveClient.getInstance().create(ApiService.class);
    }

    @Override
    public Flowable<BaseResponse<LiveTokenResponse>> getLiveToken(LiveTokenRequest request) {
        Map<String, Object> map = JSON.parseObject(JSON.toJSONString(request), type);
        return apiService.get(APIManager.X9_TOKEN_URL, map).map(responseBody -> JSON.parseObject(responseBody.string(),
                new TypeReference<BaseResponse<LiveTokenResponse>>() {
                }));
    }

    @Override
    public Flowable<BaseResponse<List<BannerResponse>>> getBannerList() {
        return liveService.get(APIManager.GET_BANNERLIST, new HashMap<String, Object>() {{
            put("banner_type", 3);
        }}).map(responseBody -> JSON.parseObject(responseBody.string(),
                new TypeReference<BaseResponse<List<BannerResponse>>>() {
                }));
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

    public Flowable<BaseResponse<LiveTokenResponse>> getXLiveToken(LiveThiredLoginRequest request){
        Map<String, Object> map = JSON.parseObject(JSON.toJSONString(request), type);
        return liveService.get(APIManager.live_third_login, map).map(new Function<ResponseBody, BaseResponse<LiveTokenResponse>>() {
            @Override
            public BaseResponse<LiveTokenResponse> apply(ResponseBody responseBody) throws Exception {
                return JSON.parseObject(responseBody.string(),
                        new TypeReference<BaseResponse<LiveTokenResponse>>() {

                        });
            }
        });
    }

    public Flowable<BaseResponse<LiveRoomBean>> getRoomInfo(GetRoomInfoRequest request){
        Map<String, Object> map = JSON.parseObject(JSON.toJSONString(request), type);
        return liveService.get(APIManager.GET_ROOM_INFO, map).map(new Function<ResponseBody, BaseResponse<LiveRoomBean>>() {
            @Override
            public BaseResponse<LiveRoomBean> apply(ResponseBody responseBody) throws Exception {
                return JSON.parseObject(responseBody.string(),
                        new TypeReference<BaseResponse<LiveRoomBean>>() {

                        });
            }
        });
    }

    public Flowable<BaseResponse<List<MessageRecord>>> getChatHistory(GetChatHistroyRequest request){
        Map<String, Object> map = JSON.parseObject(JSON.toJSONString(request), type);
        return liveService.get(APIManager.GET_CHAT_HISTORY, map).map(responseBody -> JSON.parseObject(responseBody.string(),
                new TypeReference<BaseResponse<List<MessageRecord>>>() {
                }));
    }
    public Flowable<BaseResponse<List<MessageRecord>>> getAnchorChatHistory(AnchorChatHistoryRequest request){
        Map<String, Object> map = JSON.parseObject(JSON.toJSONString(request), type);
        return liveService.get(APIManager.GET_ANCHOR_CHAT_HISTORY, map).map(responseBody -> JSON.parseObject(responseBody.string(),
                new TypeReference<BaseResponse<List<MessageRecord>>>() {
                }));
    }

    public Flowable<BaseResponse<LiveRoomBean>> getLiveDetail(GetRoomInfoRequest request){
        Map<String, Object> map = JSON.parseObject(JSON.toJSONString(request), type);
        return liveService.get(APIManager.GET_LIVE_DETAIL, map).map(new Function<ResponseBody, BaseResponse<LiveRoomBean>>() {
            @Override
            public BaseResponse<LiveRoomBean> apply(ResponseBody responseBody) throws Exception {
                return JSON.parseObject(responseBody.string(),
                        new TypeReference<BaseResponse<LiveRoomBean>>() {

                        });
            }
        });
    }

    public Flowable<BaseResponse<List<SystemMessageRecord>>> getLiveInRoomLog(GetChatHistroyRequest request){
        Map<String, Object> map = JSON.parseObject(JSON.toJSONString(request), type);
        return liveService.get(APIManager.GET_LIVE_IN_ROOM_LOG, map).map(responseBody -> JSON.parseObject(responseBody.string(),
                new TypeReference<BaseResponse<List<SystemMessageRecord>>>() {
                }));
    }

    /**
     * 聊天房列表
     *
     * @param request
     * @return
     */
    @Override
    public Flowable<BaseResponse<ChatRoomResponse>> getChatRoomList(ChatRoomListRequest request) {
        Map<String, Object> map = JSON.parseObject(JSON.toJSONString(request), type);
        return liveService.get(APIManager.CHAT_ROOM_LIST_API, map).map(new Function<ResponseBody, BaseResponse<ChatRoomResponse>>() {
            @Override
            public BaseResponse<ChatRoomResponse> apply(ResponseBody responseBody) throws Exception {
                return JSON.parseObject(responseBody.string(),
                        new TypeReference<BaseResponse<ChatRoomResponse>>() {

                        });
            }
        });
    }

    /**
     * 搜索主播助手
     *
     * @param request
     * @return
     */
    @Override
    public Flowable<BaseResponse<SearchAssistantResponse>> getSearchAssistant(SearchAssistantRequest request) {
        Map<String, Object> map = JSON.parseObject(JSON.toJSONString(request), type);
        return liveService.post(APIManager.CHAT_SEARCH_ASSISTANT_API, map).map(new Function<ResponseBody, BaseResponse<SearchAssistantResponse>>() {
            @Override
            public BaseResponse<SearchAssistantResponse> apply(ResponseBody responseBody) throws Exception {
                return JSON.parseObject(responseBody.string(),
                        new TypeReference<BaseResponse<SearchAssistantResponse>>() {

                        });
            }
        });
    }

    /**
     * 助手私聊
     *
     * @param request
     * @return
     */
    @Override
    public Flowable<BaseResponse<SendToAssistantResponse>> sendToAssistant(SendToAssistantRequest request) {
        Map<String, Object> map = JSON.parseObject(JSON.toJSONString(request), type);
        return liveService.post(APIManager.CHAT_SEND_TO_ASSISTANT_API, map).map(new Function<ResponseBody, BaseResponse<SendToAssistantResponse>>() {
            @Override
            public BaseResponse<SendToAssistantResponse> apply(ResponseBody responseBody) throws Exception {
                return JSON.parseObject(responseBody.string(),
                        new TypeReference<BaseResponse<SendToAssistantResponse>>() {

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
        return apiService.post(APIManager.FB_GET_TOKEN, new HashMap<String, Object>() {{
            put("cachedToken", 1);
        }}).map(responseBody -> JSON.parseObject(responseBody.string(),
                new TypeReference<BaseResponse<FBService>>() {

                }));
    }

    @Override
    public Flowable<BaseResponse<FBService>> getFBXCGameTokenApi() {
        return apiService.post(APIManager.FBXC_GET_TOKEN, new HashMap<String, Object>() {{
            put("cachedToken", 1);
        }}).map(responseBody -> JSON.parseObject(responseBody.string(),
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
        if (!TextUtils.isEmpty(SPUtils.getInstance().getString(SPKeyGlobal.FBXC_API_SERVICE_URL)) && !TextUtils.isEmpty(SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN))) {
            if (TextUtils.isEmpty(FBRetrofitClient.baseUrl)) {
                fbService = FBRetrofitClient.getInstance().create(ApiService.class);
            }
            return fbService.postJson(APIManager.GET_MATCH_DETAIL, map).map(responseBody -> JSON.parseObject(responseBody.string(),
                    new TypeReference<BaseResponse<MatchInfo>>() {
                    }));
        } else {
            return apiService.post(APIManager.NO_AUTH_FORWARD, new HashMap<String, Object>() {{
                put("api", APIManager.NO_AUth_GET_MATCH_DETAIL);
            }}, map).map(responseBody -> JSON.parseObject(responseBody.string(),
                    new TypeReference<BaseResponse<MatchInfo>>() {

                    }));
        }

    }

    @Override
    public Flowable<Object> getWebsocket(SubscriptionRequest request) {
        Map<String, Object> map = JSON.parseObject(JSON.toJSONString(request), type);
        return apiService.get(APIManager.LIVE_CHAT, map).map(responseBody -> JSON.parseObject(responseBody.string(),
                new TypeReference<BaseResponse<MatchInfo>>() {
                }));
    }

    @Override
    public Flowable<BaseResponse<AccumulatedRechargeRes>> getAmount() {

        return apiService.get(APIManager.accumulated_recharge)
                .map(new Function<ResponseBody, BaseResponse<AccumulatedRechargeRes>>() {
                         @Override
                         public BaseResponse<AccumulatedRechargeRes> apply(ResponseBody responseBody) throws Exception {
                             return null;
                         }
                     }
                );
    }

    @Override
    public Flowable<BaseResponse<HotLeagueInfo>> getSettings(Map<String, String> filters) {
        Map<String, Object> map = JSON.parseObject(JSON.toJSONString(filters), type);
        return apiService.get(APIManager.SETTINGS, map).map(responseBody -> JSON.parseObject(responseBody.string(),
                new TypeReference<BaseResponse<HotLeagueInfo>>() {
                }));
    }


    @Override
    public Flowable<BaseResponse<MatchListRsp>> getFBList(FBListReq fbListReq) {
        Map<String, Object> map = JSON.parseObject(JSON.toJSONString(fbListReq), type);
        if (!TextUtils.isEmpty(SPUtils.getInstance().getString(SPKeyGlobal.FBXC_API_SERVICE_URL)) && !TextUtils.isEmpty(SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN))) {
            if (TextUtils.isEmpty(FBRetrofitClient.baseUrl)) {
                fbService = FBRetrofitClient.getInstance().create(ApiService.class);
            }
            return fbService.postJson(APIManager.GET_LIST, map).map(responseBody -> JSON.parseObject(responseBody.string(),
                    new TypeReference<BaseResponse<MatchListRsp>>() {
                    }));
        }
        return null;
    }

}
