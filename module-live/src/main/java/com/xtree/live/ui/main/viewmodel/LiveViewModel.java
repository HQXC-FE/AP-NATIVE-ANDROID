package com.xtree.live.ui.main.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.material.tabs.TabLayout;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.net.live.X9LiveInfo;
import com.xtree.base.utils.CfLog;
import com.xtree.live.R;
import com.xtree.live.data.LiveRepository;
import com.xtree.live.data.source.request.FrontLivesRequest;
import com.xtree.live.data.source.request.LiveTokenRequest;
import com.xtree.live.data.source.response.FrontLivesResponse;
import com.xtree.live.data.source.response.LiveTokenResponse;
import com.xtree.live.ui.main.model.anchor.LiveAnchorModel;
import com.xtree.live.ui.main.model.constant.FrontLivesType;
import com.xtree.live.ui.main.model.hot.LiveHotModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.http.BusinessException;
import me.xtree.mvvmhabit.utils.RxUtils;

/**
 * Created by KAKA on 2024/9/9.
 * Describe: 直播门户viewModel
 */
public class LiveViewModel extends BaseViewModel<LiveRepository> implements TabLayout.OnTabSelectedListener {


    private final ArrayList<BindModel> bindModels = new ArrayList<BindModel>() {{
        LiveAnchorModel liveAnchorModel = new LiveAnchorModel(FrontLivesType.ALL.getLabel());
        liveAnchorModel.frontLivesResponseFetchListener = (page, limit, success, error) -> getFrontLives(FrontLivesType.ALL.getValue(), page, limit, success, error);
        liveAnchorModel.setItemType(0);


        LiveHotModel liveFootBallModel = new LiveHotModel(FrontLivesType.FOOTBALL.getLabel());
        liveFootBallModel.frontLivesResponseFetchListener = (page, limit, success, error) -> getFrontLives(FrontLivesType.FOOTBALL.getValue(), page, limit, success, error);
        liveFootBallModel.setItemType(1);

        LiveHotModel liveBasketBallModel = new LiveHotModel(FrontLivesType.BASKETBALL.getLabel());
        liveBasketBallModel.frontLivesResponseFetchListener = (page, limit, success, error) -> getFrontLives(FrontLivesType.BASKETBALL.getValue(), page, limit, success, error);
        liveBasketBallModel.setItemType(1);

        LiveHotModel liveOtherModel = new LiveHotModel(FrontLivesType.OTHER.getLabel());
        liveOtherModel.frontLivesResponseFetchListener = (page, limit, success, error) -> getFrontLives(FrontLivesType.BASKETBALL.getValue(), page, limit, success, error);
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
    public ObservableField<ArrayList<String>> tabs = new ObservableField<>(new ArrayList<>());
    public MutableLiveData<ArrayList<BindModel>> datas = new MutableLiveData<>(new ArrayList<>());
    public MutableLiveData<ArrayList<Integer>> itemType = new MutableLiveData<>();
    private WeakReference<FragmentActivity> mActivity = null;

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
        if (mActivity != null) {
            mActivity.clear();
            mActivity = null;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();

    }

    private void initData() {
        itemType.setValue(typeList);
        datas.setValue(bindModels);
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
}

