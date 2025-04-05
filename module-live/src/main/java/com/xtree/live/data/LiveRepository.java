package com.xtree.live.data;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import com.xtree.base.net.RetrofitClient;
import com.xtree.bet.bean.request.fb.FBListReq;
import com.xtree.bet.bean.response.HotLeagueInfo;
import com.xtree.live.data.source.http.LiveClient;
import com.xtree.base.vo.FBService;
import com.xtree.live.data.source.ApiService;
import com.xtree.live.data.source.HttpDataSource;
import com.xtree.live.data.source.LocalDataSource;
import com.xtree.live.data.source.http.HttpDataSourceImpl;
import com.xtree.live.data.source.local.LocalDataSourceImpl;
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
import com.xtree.live.data.source.response.LiveTokenResponse;
import com.xtree.live.data.source.response.ReviseHotResponse;
import com.xtree.live.data.source.response.SearchAssistantResponse;
import com.xtree.live.data.source.response.SendToAssistantResponse;
import com.xtree.live.data.source.response.fb.MatchInfo;
import com.xtree.live.model.AccumulatedRechargeRes;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import me.xtree.mvvmhabit.base.BaseModel;
import me.xtree.mvvmhabit.http.BaseResponse;
import retrofit2.http.Body;

/**
 * MVVM的Model层，统一模块的数据仓库，包含网络数据和本地数据（一个应用可以有多个Repositor）
 */
public class LiveRepository extends BaseModel implements HttpDataSource, LocalDataSource {
    private volatile static LiveRepository INSTANCE = null;
    private final HttpDataSource mHttpDataSource;

    private final LocalDataSource mLocalDataSource;

    private LiveRepository(@NonNull HttpDataSource httpDataSource,
                           @NonNull LocalDataSource localDataSource) {
        this.mHttpDataSource = httpDataSource;
        this.mLocalDataSource = localDataSource;
    }

    public static LiveRepository getInstance(HttpDataSource httpDataSource,
                                             LocalDataSource localDataSource) {
        if (INSTANCE == null) {
            synchronized (LiveRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LiveRepository(httpDataSource, localDataSource);
                }
            }
        }
        return INSTANCE;
    }

    public static LiveRepository getInstance() {
        if (INSTANCE == null) {
            synchronized (LiveRepository.class) {
                if (INSTANCE == null) {
                    //网络API服务
                    ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
                    ApiService liveService = LiveClient.getInstance().create(ApiService.class);
                    //网络数据源
                    HttpDataSource httpDataSource = HttpDataSourceImpl.getInstance(apiService, liveService);
                    //本地数据源
                    LocalDataSource localDataSource = LocalDataSourceImpl.getInstance();

                    INSTANCE = new LiveRepository(httpDataSource, localDataSource);
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
    public ApiService getApiService() {
        return mHttpDataSource.getApiService();
    }

    @Override
    public ApiService getLiveService() {
        return mHttpDataSource.getLiveService();
    }

    @Override
    public void setLive(LiveTokenResponse liveData) {
        mHttpDataSource.setLive(liveData);
    }

    @Override
    public Flowable<BaseResponse<LiveTokenResponse>> getLiveToken(LiveTokenRequest request) {
        return mHttpDataSource.getLiveToken(request);
    }

    @Override
    public Flowable<BaseResponse<List<BannerResponse>>> getBannerList() {
        return mHttpDataSource.getBannerList();
    }

    @Override
    public Flowable<BaseResponse<List<FrontLivesResponse>>> getFrontLives(FrontLivesRequest request) {
        return mHttpDataSource.getFrontLives(request);
    }

    /**
     * 获取主播列表
     * @param request type ;//1：热门， 2：推荐
     *                platform ;//终端类型 	1： PC; 2：H5；3：android；4：ios；
     *                side;//投放位置 	1：足球；2：篮球；3：电竞；4：首页；5：直播间；6：其它；
     *                listRows ;//每页数量,默认6
     *                page;//页码默认1
     * @return
     */
    @Override
    public Flowable<BaseResponse<AnchorSortResponse>> getAnchorSort(AnchorSortRequest request) {
        return mHttpDataSource.getAnchorSort(request);
    }

    /**
     *聊天房列表
     * @param request
     * @return
     */
    @Override
    public Flowable<BaseResponse<ChatRoomResponse>> getChatRoomList(ChatRoomListRequest request) {
        return mHttpDataSource.getChatRoomList(request);
    }
    /**
     *搜索主播助手
     * @param request
     * @return
     */
    @Override
    public Flowable<BaseResponse<SearchAssistantResponse>> getSearchAssistant(SearchAssistantRequest request) {
        return mHttpDataSource.getSearchAssistant(request);
    }

    @Override
    public Flowable<BaseResponse<SendToAssistantResponse>> sendToAssistant(SendToAssistantRequest request) {
        return mHttpDataSource.sendToAssistant(request);
    }

    @Override
    public Flowable<BaseResponse<FBService>> getFBGameTokenApi() {
        return mHttpDataSource.getFBGameTokenApi();
    }

    @Override
    public Flowable<BaseResponse<FBService>> getFBXCGameTokenApi() {
        return mHttpDataSource.getFBXCGameTokenApi();
    }

    @Override
    public Flowable<BaseResponse<MatchInfo>> getMatchDetail(MatchDetailRequest request) {
        return mHttpDataSource.getMatchDetail(request);
    }

    @Override
    public Flowable<Object> getWebsocket(SubscriptionRequest request) {
        return mHttpDataSource.getWebsocket(request);
    }

    @Override
    public Flowable<BaseResponse<ReviseHotResponse>> getReviseHot() {
        return mHttpDataSource.getReviseHot();
    }

    @Override
    public Flowable<BaseResponse<AccumulatedRechargeRes>> getAmount() {
        return mHttpDataSource.getAmount();
    }

    @Override
    public Flowable<BaseResponse<HotLeagueInfo>> getSettings(Map<String, String> filters) {
        return mHttpDataSource.getSettings(filters);
    }

    @Override
    public Flowable<BaseResponse<MatchInfo>> getFBList(@Body FBListReq fbListReq){
        return mHttpDataSource.getFBList(fbListReq);
    }
}
