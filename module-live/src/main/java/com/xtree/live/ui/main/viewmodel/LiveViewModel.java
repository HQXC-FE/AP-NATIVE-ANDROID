package com.xtree.live.ui.main.viewmodel;

import static com.xtree.base.net.FBHttpCallBack.CodeRule.CODE_14010;
import static com.xtree.base.utils.BtDomainUtil.KEY_PLATFORM;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_FBXC;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.alibaba.fastjson.JSON;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonObject;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.net.live.X9LiveInfo;
import com.xtree.base.utils.BtDomainUtil;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.SPUtil;
import com.xtree.base.vo.FBService;
import com.xtree.live.LiveConfig;
import com.xtree.live.R;
import com.xtree.live.SPKey;
import com.xtree.live.chat.RequestUtils;
import com.xtree.live.data.LiveRepository;
import com.xtree.live.data.source.httpnew.LiveRep;
import com.xtree.live.data.source.httpnew.LiveService;
import com.xtree.live.data.source.httpnew.RepositoryManager;
import com.xtree.live.data.source.httpnew.RxLifecycleUtils;
import com.xtree.live.data.source.request.AnchorSortRequest;
import com.xtree.live.data.source.request.FrontLivesRequest;
import com.xtree.live.data.source.request.LiveTokenRequest;
import com.xtree.live.data.source.request.MatchDetailRequest;
import com.xtree.live.data.source.request.RoomInfoRequest;
import com.xtree.live.data.source.request.SubscriptionRequest;
import com.xtree.live.data.source.response.AnchorSortResponse;
import com.xtree.live.data.source.response.BannerResponse;
import com.xtree.live.data.source.response.FrontLivesResponse;
import com.xtree.live.data.source.response.LiveRoomBean;
import com.xtree.live.data.source.response.LiveTokenResponse;
import com.xtree.live.data.source.response.ReviseHotResponse;
import com.xtree.live.data.source.response.fb.Match;
import com.xtree.live.data.source.response.fb.MatchFb;
import com.xtree.live.data.source.response.fb.MatchInfo;
import com.xtree.live.model.GiftBean;
import com.xtree.live.ui.main.listener.FetchListener;
import com.xtree.live.ui.main.model.anchor.LiveAnchorModel;
import com.xtree.live.ui.main.model.banner.LiveBannerItemModel;
import com.xtree.live.ui.main.model.banner.LiveBannerModel;
import com.xtree.live.ui.main.model.constant.FrontLivesType;
import com.xtree.live.ui.main.model.hot.LiveHotModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.sentry.Sentry;
import me.xtree.mvvmhabit.base.BaseApplication;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.http.BaseResponse;
import me.xtree.mvvmhabit.http.BusinessException;
import me.xtree.mvvmhabit.http.ResponseThrowable;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;

/**
 * Created by KAKA on 2024/9/9
 * Describe: 直播门户viewModel
 */
public class LiveViewModel extends BaseViewModel<LiveRepository> implements TabLayout.OnTabSelectedListener {

    private final ArrayList<BindModel> bindModels = new ArrayList<BindModel>() {{
        LiveAnchorModel liveAnchorModel = new LiveAnchorModel(FrontLivesType.ALL.getLabel());
        liveAnchorModel.frontLivesResponseFetchListener = (page, limit, params, success, error) -> getFrontLives(FrontLivesType.ALL.getValue(), page, limit, success, error);
        liveAnchorModel.setItemType(0);

        LiveHotModel liveFootBallModel = new LiveHotModel(FrontLivesType.FOOTBALL.getLabel());
        liveFootBallModel.frontLivesResponseFetchListener = (page, limit, params, success, error) -> getFrontLives(FrontLivesType.FOOTBALL.getValue(), page, limit, success, error);
        liveFootBallModel.matchInfoResponseFetchListener = (page, limit, params, success, error) -> getMatchDetail(params.get("matchId").toString(), success, error);
        liveFootBallModel.setItemType(1);

        LiveHotModel liveBasketBallModel = new LiveHotModel(FrontLivesType.BASKETBALL.getLabel());
        liveBasketBallModel.frontLivesResponseFetchListener = (page, limit, params, success, error) -> getFrontLives(FrontLivesType.BASKETBALL.getValue(), page, limit, success, error);
        liveBasketBallModel.matchInfoResponseFetchListener = (page, limit, params, success, error) -> getMatchDetail(params.get("matchId").toString(), success, error);
        liveBasketBallModel.setItemType(1);

        LiveHotModel liveOtherModel = new LiveHotModel(FrontLivesType.OTHER.getLabel());
        liveOtherModel.frontLivesResponseFetchListener = (page, limit, params, success, error) -> getFrontLives(FrontLivesType.OTHER.getValue(), page, limit, success, error);
        liveOtherModel.matchInfoResponseFetchListener = (page, limit, params, success, error) -> getMatchDetail(params.get("matchId").toString(), success, error);
        liveOtherModel.setItemType(1);

        add(liveAnchorModel);
        add(liveFootBallModel);
        add(liveBasketBallModel);
        add(liveOtherModel);
    }};

    private final ArrayList<Integer> typeList = new ArrayList() {
        {
            add(R.layout.layout_live_anchor);
            add(R.layout.layout_live_hot);
        }
    };
    public SingleLiveData<Object> getWsTokenLiveData = new SingleLiveData<>();
    public LiveBannerModel bannerModel = new LiveBannerModel();
    public ObservableField<ArrayList<String>> tabs = new ObservableField<>(new ArrayList<>());
    public MutableLiveData<ArrayList<BindModel>> datas = new MutableLiveData<>(new ArrayList<>());
    public MutableLiveData<ArrayList<Integer>> itemType = new MutableLiveData<>();

    public MutableLiveData<LiveRoomBean> liveRoomInfo = new MutableLiveData<>();
    public MutableLiveData<LiveRoomBean> liveRoomInfoRefresh = new MutableLiveData<>();
    public MutableLiveData<List<GiftBean>> giftList = new MutableLiveData<>();

    public LiveViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveViewModel(@NonNull Application application, LiveRepository model) {
        super(application, model);
    }


    public void initData(FragmentActivity mActivity) {
        setActivity(mActivity);

        if (X9LiveInfo.INSTANCE.getToken().isEmpty()) {
            model.getLiveToken(new LiveTokenRequest())
                    .compose(RxUtils.schedulersTransformer())
                    .compose(RxUtils.exceptionTransformer())
                    .subscribe(new HttpCallBack<LiveTokenResponse>() {
                        @Override
                        public void onResult(LiveTokenResponse data) {
                            if (data.getAppApi() != null && !data.getAppApi().isEmpty()) {
                                model.setLive(data);
                                initData();
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            super.onError(t);
                        }
                    });

            /*JsonObject json = new JsonObject();
            json.addProperty("fingerprint", X9LiveInfo.INSTANCE.getOaid());
            json.addProperty("device_type", "android");
            json.addProperty("user_id", 10);

            LiveRep.getInstance().getXLiveToken(RequestUtils.getRequestBody(json))
                    .subscribe(new HttpCallBack<LiveTokenResponse>() {
                        @Override
                        public void onResult(LiveTokenResponse data) {
                            if (data.getAppApi() != null && !data.getAppApi().isEmpty()) {
                                model.setLive(data);
                                initData();
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            super.onError(t);
                        }
                    });*/
        } else {
            initData();
        }
    }

    public void setActivity(FragmentActivity mActivity) {
        bannerModel.mActivity = new WeakReference<>(mActivity);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        CfLog.d("选中的" + tab.getText());
        refresh(tab.getText().toString());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bannerModel.mActivity != null) {
            bannerModel.mActivity.clear();
            bannerModel.mActivity = null;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();

    }

    private void initData() {
        itemType.setValue(typeList);
        datas.setValue(bindModels);

        //获取直播配置文件
        model.getReviseHot()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new HttpCallBack<ReviseHotResponse>() {
                    @Override
                    public void onResult(ReviseHotResponse o) {
                        CfLog.d(o.toString());
                    }
                });
    }

    public void refresh(String tag) {
        if (bindModels.isEmpty()) {
            return;
        }
        if (TextUtils.isEmpty(tag)) {
            BindModel bindModel = bindModels.get(0);
            bindModelAutoRefresh(bindModel);
        } else {
            for (BindModel bindModel : bindModels) {
                if (bindModel.getTag() != null && bindModel.getTag().toString().equals(tag)) {
                    bindModelAutoRefresh(bindModel);
                }
            }
        }
        getBannerList();

    }

    private void bindModelAutoRefresh(BindModel bindModel) {
        if (bindModel instanceof LiveAnchorModel) {
            ((LiveAnchorModel) bindModel).autoRefresh.set(new Object());
        } else if (bindModel instanceof LiveHotModel) {
            ((LiveHotModel) bindModel).autoRefresh.set(new Object());
        }
    }

    private void getFrontLives(String type, int page, int limit, Observer<List<FrontLivesResponse>> success, Observer<Object> error) {
        FrontLivesRequest request = new FrontLivesRequest();
        request.setLimit(limit);
        request.setType(type);
        request.setPage(page);
        Disposable disposable = (Disposable) model.getFrontLives(request)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<List<FrontLivesResponse>>() {
                    @Override
                    public void onResult(List<FrontLivesResponse> data) {
                        success.onChanged(data);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        error.onChanged(t);
                    }

                    @Override
                    public void onFail(BusinessException t) {
                        super.onFail(t);
                        error.onChanged(t);
                    }
                });
        addSubscribe(disposable);

    }

    private void getBannerList() {

        Disposable disposable = (Disposable) model.getBannerList()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<List<BannerResponse>>() {
                    @Override
                    public void onResult(List<BannerResponse> data) {
                        if (data == null || data.size() == 0) {
                            Sentry.captureException(new Exception("直播轮播图数据为空"));
                            return;
                        }
                        ArrayList<BindModel> bindModels = new ArrayList<BindModel>();
                        for (BannerResponse bannerResponse : data) {
                            LiveBannerItemModel itemModel = new LiveBannerItemModel();
                            itemModel.backImg.set(bannerResponse.getBackImg());
                            itemModel.foreImg.set(bannerResponse.getForeImg());
                            itemModel.androidUrl.set(bannerResponse.getAndroidUrl());
                            itemModel.params.set(bannerResponse.getParams());
                            bindModels.add(itemModel);
                        }
                        bannerModel.bannerBg.set(((LiveBannerItemModel) bindModels.get(0)).backImg.get());
                        bannerModel.datas.setValue(new ArrayList<>(bindModels));
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);

                    }

                    @Override
                    public void onFail(BusinessException t) {
                        super.onFail(t);
                    }
                });
        addSubscribe(disposable);
    }

    private void getMatchDetail(String matchId, Observer<Match> success, Observer<Object> error) {
        MatchDetailRequest request = new MatchDetailRequest();
        request.setMatchId(matchId);
        Disposable disposable = (Disposable) model.getMatchDetail(request)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<MatchInfo>() {
                    @Override
                    public void onResult(MatchInfo data) {
                        if (TextUtils.isEmpty(SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN))) {
                            success.onChanged(new MatchFb(data.data));
                        } else {
                            success.onChanged(new MatchFb(data));
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        if ((t instanceof ResponseThrowable) && ((ResponseThrowable) t).code == CODE_14010) {
                            getGameTokenApi(matchId, success, error);
                        } else {
                            error.onChanged(t);
                        }
                    }

                    @Override
                    public void onFail(BusinessException t) {
                        super.onFail(t);
                        if (t.code == CODE_14010) {
                            getGameTokenApi(matchId, success, error);
                        } else {
                            error.onChanged(t);
                        }
                    }
                });
        addSubscribe(disposable);
    }

    public void getGameTokenApi(String matchId, Observer<Match> success, Observer<Object> error) {
        Flowable<BaseResponse<FBService>> flowable;
        String mPlatform = SPUtils.getInstance().getString(KEY_PLATFORM);
        if (TextUtils.equals(mPlatform, PLATFORM_FBXC)) {
            flowable = model.getFBXCGameTokenApi();
        } else {
            flowable = model.getFBGameTokenApi();
        }
        Disposable disposable = (Disposable) flowable
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<FBService>() {
                    @Override
                    public void onResult(FBService fbService) {
                        if (TextUtils.equals(mPlatform, PLATFORM_FBXC)) {
                            SPUtils.getInstance().put(SPKeyGlobal.FBXC_TOKEN, fbService.getToken());
                            SPUtils.getInstance().put(SPKeyGlobal.FBXC_API_SERVICE_URL, fbService.getForward().getApiServerAddress());
                            BtDomainUtil.setDefaultFbxcDomainUrl(fbService.getForward().getApiServerAddress());
                            BtDomainUtil.addFbxcDomainUrl(fbService.getForward().getApiServerAddress());
                            BtDomainUtil.setFbxcDomainUrl(fbService.getDomains());
                        } else {
                            SPUtils.getInstance().put(SPKeyGlobal.FB_TOKEN, fbService.getToken());
                            SPUtils.getInstance().put(SPKeyGlobal.FB_API_SERVICE_URL, fbService.getForward().getApiServerAddress());
                            BtDomainUtil.setDefaultFbDomainUrl(fbService.getForward().getApiServerAddress());
                            BtDomainUtil.addFbDomainUrl(fbService.getForward().getApiServerAddress());
                            BtDomainUtil.setFbDomainUrl(fbService.getDomains());
                        }
                        getMatchDetail(matchId, success, error);
                    }

                    @Override
                    public void onError(Throwable t) {
                        error.onChanged(t);
                    }

                    @Override
                    public void onFail(BusinessException t) {
                        error.onChanged(t);
                    }
                });
        addSubscribe(disposable);
    }

    /**
     * 获取直播间详情
     * @param uid
     */
    public void getRoomInfo(int uid){
        String channelCode = X9LiveInfo.INSTANCE.getChannel();
        LiveRep.getInstance().getRoomInfo(uid,channelCode)
                .subscribe(new HttpCallBack<LiveRoomBean>() {
                    @Override
                    public void onResult(LiveRoomBean liveRoomBean) {
                        liveRoomInfo.postValue(liveRoomBean);
                        if(liveRoomBean.getInfo()!=null){
                            X9LiveInfo.INSTANCE.setUid(liveRoomBean.getInfo().getUid());
                            SPUtils.getInstance().put(SPKey.UID,uid);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }

                });

    }
    public void refreshRoomInfo(int uid){
        String channelCode = X9LiveInfo.INSTANCE.getChannel();
        LiveRep.getInstance().getRoomInfo(uid,channelCode)
                .subscribe(new HttpCallBack<LiveRoomBean>() {
                    @Override
                    public void onResult(LiveRoomBean liveRoomBean) {
                        liveRoomInfoRefresh.postValue(liveRoomBean);
                        if(liveRoomBean.getInfo()!=null){
                            X9LiveInfo.INSTANCE.setUid(liveRoomBean.getInfo().getUid());
                            SPUtils.getInstance().put(SPKey.UID,uid);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }

                });

    }

    /**
     * 获取gift列表
     */
    public void getGiftList() {
        LiveRep.getInstance().getGiftList()
                .subscribe(new HttpCallBack<List<GiftBean>>() {
                    @Override
                    public void onResult(List<GiftBean> data) {
                        giftList.postValue(data);
                    }
                });
    }

    public void getAnchorSort() {
        LiveRepository.getInstance().getAnchorSort(new AnchorSortRequest())
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new HttpCallBack<AnchorSortResponse>() {
                    @Override
                    public void onResult(AnchorSortResponse data) {

                        if (data != null) {
                            CfLog.e("getAnchorSort ---->" + data.toString());
                        }
                    }

                });
    }

    public void getWebsocket() {
        SubscriptionRequest request = new SubscriptionRequest();
        request.action = "sub";
        request.vid = "AAABBBCCC";
        Disposable disposable = (Disposable) model.getWebsocket(request)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<Object>() {
                    @Override
                    public void onResult(Object object) {
                        getWsTokenLiveData.setValue(object);
                    }

                    @Override
                    public void onError(Throwable t) {
                        //                        CfLog.e(t.toString());
                    }
                });
        addSubscribe(disposable);
    }

}

