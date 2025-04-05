package com.xtree.live.ui.main.viewmodel;

import static com.xtree.base.net.FBHttpCallBack.CodeRule.CODE_14010;
import static com.xtree.base.utils.BtDomainUtil.KEY_PLATFORM;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_FBXC;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonObject;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.net.live.X9LiveInfo;
import com.xtree.base.utils.BtDomainUtil;
import com.xtree.base.utils.CfLog;
import com.xtree.base.vo.FBService;
import com.xtree.live.chat.RequestUtils;
import com.xtree.live.data.LiveRepository;
import com.xtree.live.data.source.httpnew.LiveRep;
import com.xtree.live.data.source.request.FrontLivesRequest;
import com.xtree.live.data.source.request.LiveTokenRequest;
import com.xtree.live.data.source.request.MatchDetailRequest;
import com.xtree.live.data.source.response.FrontLivesResponse;
import com.xtree.live.data.source.response.LiveTokenResponse;
import com.xtree.live.data.source.response.ReviseHotResponse;
import com.xtree.live.data.source.response.fb.Match;
import com.xtree.live.data.source.response.fb.MatchFb;
import com.xtree.live.data.source.response.fb.MatchInfo;
import com.xtree.live.ui.main.model.banner.LiveBannerModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.http.BaseResponse;
import me.xtree.mvvmhabit.http.BusinessException;
import me.xtree.mvvmhabit.http.ResponseThrowable;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * Created by Vickers on 2024/9/9.
 * Describe: 直播间
 */
public class LiveDetailViewModel extends BaseViewModel<LiveRepository> implements TabLayout.OnTabSelectedListener {
    public SingleLiveData<Object> getWsTokenLiveData = new SingleLiveData<>();


    public LiveBannerModel bannerModel = new LiveBannerModel();
    public ObservableField<ArrayList<String>> tabs = new ObservableField<>(new ArrayList<>());
    public MutableLiveData<ArrayList<BindModel>> datas = new MutableLiveData<>(new ArrayList<>());
    public MutableLiveData<ArrayList<Integer>> itemType = new MutableLiveData<>();
    private WeakReference<FragmentActivity> mActivity = null;

    public LiveDetailViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveDetailViewModel(@NonNull Application application, LiveRepository model) {
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
            json.addProperty("channel_code", "xc");
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
        this.mActivity = new WeakReference<>(mActivity);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        CfLog.d("选中的" + tab.getText());
        ToastUtils.showShort("================= 选中的\" + tab.getText() ==============="+ tab.getText());
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
//        if (mActivity != null) {
//            mActivity.clear();
//            mActivity = null;
//        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();

    }

    private void initData() {
//        itemType.setValue(typeList);
//        datas.setValue(bindModels);

        //获取直播配置文件
        model.getReviseHot()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new HttpCallBack<ReviseHotResponse>() {
                    @Override
                    public void onResult(ReviseHotResponse o) {

                    }
                });
    }

    public void refresh(String tag) {
//        System.out.println("============= LiveDeatailModel refresh bindModels ================="+bindModels.size());
//        if (bindModels.isEmpty()) {
//            return;
//        }
//        if (TextUtils.isEmpty(tag)) {
//            BindModel bindModel = bindModels.get(0);
//            bindModelAutoRefresh(bindModel);
//        } else {
//            for (BindModel bindModel : bindModels) {
//                if (bindModel.getTag() != null && bindModel.getTag().toString().equals(tag)) {
//                    bindModelAutoRefresh(bindModel);
//                }
//            }
//        }

    }

//    private void bindModelAutoRefresh(BindModel bindModel) {
//        if (bindModel instanceof LiveBetModel) {
//            ((LiveBetModel) bindModel).autoRefresh.set(new Object());
//        } else if (bindModel instanceof LiveBetModel) {
//            ((LiveBetModel) bindModel).autoRefresh.set(new Object());
//        }
//    }

    private void getFrontLives(String type, int page, int limit, Observer<List<FrontLivesResponse>> success, Observer<Object> error) {
        System.out.println("================= LiveDetailViewModel getFrontLives ===================");
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
                        System.out.println("================= LiveDetailViewModel getFrontLives onResult ===================");
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


}

