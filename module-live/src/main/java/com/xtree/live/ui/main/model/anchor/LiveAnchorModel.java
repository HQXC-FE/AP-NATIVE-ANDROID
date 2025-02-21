package com.xtree.live.ui.main.model.anchor;


import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.alibaba.android.arouter.launcher.ARouter;
import com.drake.brv.BindingAdapter;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.mvvm.recyclerview.BaseDatabindingAdapter;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.live.R;
import com.xtree.live.data.source.response.FrontLivesResponse;
import com.xtree.live.ui.main.bet.LiveMatchDetailActivity;
import com.xtree.live.ui.main.listener.FetchListener;

import java.util.ArrayList;
import java.util.List;

import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * Created by KAKA on 2024/9/9.
 * Describe: 直播TAB数据模型
 */
public class LiveAnchorModel extends BindModel {

    private final ArrayList<BindModel> bindModels = new ArrayList<BindModel>() {{
    }};
    private final int limit = 10;
    private BindingAdapter.BindingViewHolder mHolder;
    public ObservableField<ArrayList<BindModel>> datas = new ObservableField<>(new ArrayList<>());
    public ObservableField<ArrayList<Integer>> itemTypeList = new ObservableField<>(
            new ArrayList<Integer>() {
                {
                    add(R.layout.item_live_anchor);
                }
            });
    public BaseDatabindingAdapter.onBindListener onBindListener = new BaseDatabindingAdapter.onBindListener() {

        @Override
        public void onItemClick(int modelPosition, int layoutPosition, int itemViewType) {
            //跳到直播间投注页
            ToastUtils.show("" + modelPosition, ToastUtils.ShowType.Default);
            Context context = mHolder.getContext();
            //判断当前是否是登录状态
            String token = SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN);
            boolean isLogin = !TextUtils.isEmpty(token);
            if(isLogin){
                String  matchID = ((LiveAnchorItemModel) bindModels.get(modelPosition)).getMatchId();
                int uid = ((LiveAnchorItemModel) bindModels.get(modelPosition)).getUid();
                String vid = ((LiveAnchorItemModel) bindModels.get(modelPosition)).getVid();
                LiveMatchDetailActivity.start(context, matchID,uid,vid);
            }else{
                goLogin();
            }
        }

        @Override
        public void onBind(@NonNull BindingAdapter.BindingViewHolder bindingViewHolder, @NonNull View view, int itemViewType) {
            mHolder = bindingViewHolder;
        }
    };
    public FetchListener<List<FrontLivesResponse>> frontLivesResponseFetchListener;

    public ObservableBoolean enableLoadMore = new ObservableBoolean(true);
    public ObservableField<Object> finishRefresh = new ObservableField<Object>(new Object[]{null});
    public ObservableField<Object> autoRefresh = new ObservableField<Object>(new Object[]{null});
    private int currentPage = 1;
    public OnRefreshLoadMoreListener onRefreshLoadMoreListener = new OnRefreshLoadMoreListener() {


        @Override
        public void onRefresh(@NonNull RefreshLayout refreshLayout) {

            currentPage = 1;
            if (frontLivesResponseFetchListener != null) {
                frontLivesResponseFetchListener.fetch(currentPage, limit, null, frontLivesResponse -> {
                    _fetchFrontLives(currentPage, limit, true, frontLivesResponse);
                }, error -> {
                    finishRefresh.set(new Object());
                });
            }
        }

        @Override
        public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
            currentPage++;
            if (frontLivesResponseFetchListener != null) {
                frontLivesResponseFetchListener.fetch(currentPage, limit, null, frontLivesResponse -> {
                    _fetchFrontLives(currentPage, limit, false, frontLivesResponse);
                }, error -> {
                    finishRefresh.set(new Object());
                });
            }
        }
    };

    public LiveAnchorModel(String tag) {
        //设置标签，用于显示TAB标题
        setTag(tag);
        datas.set(bindModels);
    }

    private void _fetchFrontLives(int page, int limit, boolean isRefresh, List<FrontLivesResponse> result) {
        finishRefresh.set(new Object());
        if (isRefresh) {
            bindModels.clear();
        }
        for (FrontLivesResponse response :
                result) {
            LiveAnchorItemModel itemModel = new LiveAnchorItemModel();
            itemModel.setThumb(response.getThumb());
            itemModel.setIsLive(response.getIsLive());
            itemModel.setTitle(response.getTitle());
            itemModel.setAvatar(response.getAvatar());
            itemModel.setUserNickname(response.getUserNickname());
            itemModel.setHeat("" + response.getHeat());
            itemModel.setMatchId(String.valueOf(response.getMatchId()));
            itemModel.setUid(response.getUid());
            itemModel.setVid(response.getVid());
            bindModels.add(itemModel);
        }
        datas.set(bindModels);
        notifyChange();
    }

    private void goLogin() {
        ARouter.getInstance().build(RouterActivityPath.Mine.PAGER_LOGIN_REGISTER).navigation();
    }

}
