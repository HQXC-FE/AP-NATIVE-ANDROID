package com.xtree.live.ui.main.model.hot;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.drake.brv.BindingAdapter;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.xtree.base.mvvm.recyclerview.BaseDatabindingAdapter;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.live.R;
import com.xtree.live.data.source.response.FrontLivesResponse;
import com.xtree.live.ui.main.listener.FetchListener;

import java.util.ArrayList;

import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * Created by KAKA on 2024/9/9.
 * Describe: 热门TAB数据模型
 */
public class LiveHotModel extends BindModel {

    private final ArrayList<BindModel> bindModels = new ArrayList<BindModel>() {{

    }};
    private final int limit = 10;
    public ObservableField<ArrayList<BindModel>> datas = new ObservableField<>(new ArrayList<>());
    public ObservableField<ArrayList<Integer>> itemTypeList = new ObservableField<>(
            new ArrayList<Integer>() {
                {
                    add(R.layout.item_live_hot);
                }
            });
    public ObservableBoolean enableLoadMore = new ObservableBoolean(true);
    public ObservableField<Object> finishRefresh = new ObservableField<Object>();
    public ObservableField<Object> autoRefresh = new ObservableField<Object>();
    public FetchListener<FrontLivesResponse> frontLivesResponseFetchListener;
    public BaseDatabindingAdapter.onBindListener onBindListener = new BaseDatabindingAdapter.onBindListener() {

        @Override
        public void onItemClick(int modelPosition, int layoutPosition, int itemViewType) {

            ToastUtils.show("" + modelPosition, ToastUtils.ShowType.Default);
        }

        @Override
        public void onBind(@NonNull BindingAdapter.BindingViewHolder bindingViewHolder, @NonNull View view, int itemViewType) {

        }
    };
    private int currentPage = 1;
    public OnRefreshLoadMoreListener onRefreshLoadMoreListener = new OnRefreshLoadMoreListener() {


        @Override
        public void onRefresh(@NonNull RefreshLayout refreshLayout) {

            currentPage = 1;
            if (frontLivesResponseFetchListener != null) {
                frontLivesResponseFetchListener.fetch(currentPage, limit, frontLivesResponse -> {
                    _fetchFrontLives(currentPage, limit, true, frontLivesResponse);
                }, error -> {
                    _fetchFrontLives(currentPage, limit, false, null);
                    finishRefresh.set(new Object());
                });
            }
        }

        @Override
        public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
            currentPage++;
            if (frontLivesResponseFetchListener != null) {
                frontLivesResponseFetchListener.fetch(currentPage, limit, frontLivesResponse -> {
                    _fetchFrontLives(currentPage, limit, false, frontLivesResponse);
                }, error -> {
                    _fetchFrontLives(currentPage, limit, false, null);
                    finishRefresh.set(new Object());
                });
            }
        }
    };

    public LiveHotModel(String tag) {
        //to do
        //设置标签，用于显示TAB标题
        setTag(tag);

        datas.set(bindModels);
    }

    private void _fetchFrontLives(int page, int limit, boolean isRefresh, FrontLivesResponse result) {
        finishRefresh.set(new Object());

        LiveHotItemModel itemModel = new LiveHotItemModel();
        itemModel.setText("热门TXT");

        bindModels.add(itemModel);
        bindModels.add(itemModel);
        bindModels.add(itemModel);
        bindModels.add(itemModel);
        bindModels.add(itemModel);
        bindModels.add(itemModel);
        datas.set(bindModels);
        notifyChange();
    }

}
