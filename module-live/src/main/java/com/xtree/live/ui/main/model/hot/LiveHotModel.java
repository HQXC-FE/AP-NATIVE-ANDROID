package com.xtree.live.ui.main.model.hot;

import android.text.TextUtils;
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
import com.xtree.live.data.source.response.fb.Constants;
import com.xtree.live.data.source.response.fb.Match;
import com.xtree.live.ui.main.listener.FetchListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    public FetchListener<List<FrontLivesResponse>> frontLivesResponseFetchListener;
    public FetchListener<Match> matchInfoResponseFetchListener;
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

    public LiveHotModel(String tag) {
        //to do
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
            LiveHotItemModel itemModel = new LiveHotItemModel();
            itemModel.setUserNickname(response.getUserNickname());
            itemModel.setMatchId("" + response.getMatchId());
            bindModels.add(itemModel);
            if (matchInfoResponseFetchListener != null) {
                //matchId：787632
                if (response.getMatchId() > 0) {
                    matchInfoResponseFetchListener.fetch(-1, -1, new HashMap() {{
                        put("matchId", response.getMatchId());
                    }}, match -> {
                        _fetchMatchInfo(itemModel, match);
                    }, error -> {
                    });
                }
            }
        }
        datas.set(bindModels);
        notifyChange();
    }

    private void _fetchMatchInfo(LiveHotItemModel itemModel, Match match) {
        List<Integer> scoreList = match.getScore(Constants.getScoreType());
        if (scoreList != null && scoreList.size() > 1) {
            String scoreMain = String.valueOf(scoreList.get(0));
            String scoreVisitor = String.valueOf(scoreList.get(1));
            itemModel.mainScore.set(scoreMain);
            itemModel.visitorScore.set(scoreVisitor);
        }

        String teamMain = match.getTeamMain();
        String teamTeamVistor = match.getTeamVistor();
        if (!TextUtils.isEmpty(teamMain)) {
            itemModel.mainTeamName.set(teamMain);
        }
        if (!TextUtils.isEmpty(teamTeamVistor)) {
            itemModel.visitorTeamName.set(teamTeamVistor);
        }

        String leagueName = match.getLeague().getLeagueName();
        if (!TextUtils.isEmpty(leagueName)) {
            itemModel.leagueName.set(leagueName);
        }

        String iconMain = match.getIconMain();
        String iconVisitor = match.getIconVisitor();

        if (!TextUtils.isEmpty(iconMain)) {
            itemModel.mainTeamIcon.set(iconMain);
        }

        if (!TextUtils.isEmpty(iconVisitor)) {
            itemModel.visitorTeamIcon.set(iconVisitor);
        }

        itemModel.isGoingOn.set(match.isGoingon() ? 1 : 0);

        // 比赛未开始
        if (!match.isGoingon()) {
            itemModel.stage.set("");
        } else {
            if (TextUtils.equals(Constants.getFbSportId(), match.getSportId()) || TextUtils.equals(Constants.getBsbSportId(), match.getSportId())) { // 足球和篮球
                itemModel.stage.set(match.getStage() + " " + match.getTime());
            } else {
                itemModel.stage.set(match.getStage());
            }
        }
    }
}
