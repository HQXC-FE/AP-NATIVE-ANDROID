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
import com.xtree.base.utils.CfLog;
import com.xtree.bet.bean.response.fb.MatchInfo;
import com.xtree.bet.bean.response.fb.MatchListRsp;
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
    public FetchListener<MatchListRsp> matchListResponseFetchListener;
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
            bindModels.clear(); //刷新后重新添加数据并清理之前数据
            //这里需要加载热门赛事数据
            if (matchListResponseFetchListener != null) {
              matchListResponseFetchListener.fetch(currentPage, limit, null, matchListResponse -> {
                  CfLog.d("=================== matchListResponseFetchListener matchListResponse.records ======================="+matchListResponse.records.size());
                  for (MatchInfo match : matchListResponse.records) {
                      CfLog.d("=============== MatchInfo ==============="+match.toString());
                  }
                  _fetchMatchList(currentPage, limit,true,matchListResponse.records);
                }, error -> {
                    finishRefresh.set(new Object());
                });
            }
            //这里需要加载直播间的数据
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
            //这里需要加载热门赛事数据
            if (matchListResponseFetchListener != null) {
                matchListResponseFetchListener.fetch(currentPage, limit, null, matchListResponse -> {
                    CfLog.d("=================== matchListResponseFetchListener matchListResponse 2222======================="+matchListResponse);
                    // _fetchFrontLives(currentPage, limit, true, frontLivesResponse);
                    for (MatchInfo match : matchListResponse.records) {
                        CfLog.d("=============== MatchInfo ==============="+match.toString());
                    }
                    _fetchMatchList(currentPage, limit,true,matchListResponse.records);
                }, error -> {
                    finishRefresh.set(new Object());
                });
            }
            //这里需要加载直播间的数据
            if (frontLivesResponseFetchListener != null) {
                frontLivesResponseFetchListener.fetch(currentPage, limit, null, frontLivesResponse -> {
                    _fetchFrontLives(currentPage, limit, true, frontLivesResponse);
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
        CfLog.d("================ _fetchFrontLives bindModels ===================="+bindModels.size());
        datas.set(bindModels);
        notifyChange();
    }

    private void _fetchMatchList(int page, int limit, boolean isRefresh, List<MatchInfo> records) {
        CfLog.d("================ _fetchMatchList bindModels ===================="+bindModels.size());
        finishRefresh.set(new Object());
        CfLog.d("================ _fetchMatchList 22222 ====================");
        for (MatchInfo matchInfo :
                records) {
            LiveHotItemModel itemModel = new LiveHotItemModel();
            itemModel.setUserNickname("测试热门");
            itemModel.setMatchId("" + "838747738");
            bindModels.add(itemModel);
//            if (matchInfoResponseFetchListener != null) {
//                //matchId：787632
//                if (response.getMatchId() > 0) {
//                    matchInfoResponseFetchListener.fetch(-1, -1, new HashMap() {{
//                        put("matchId", response.getMatchId());
//                    }}, match -> {
//                        _fetchMatchInfo(itemModel, match);
//                    }, error -> {
//                    });
//                }
//            }
            _fetchMatchListInfo(itemModel,matchInfo);
        }
        CfLog.d("================ _fetchMatchList bindModels ===================="+bindModels.size());
        datas.set(bindModels);
        notifyChange();
    }

    private void _fetchMatchListInfo(LiveHotItemModel itemModel, MatchInfo matchInfo) {
//        List<Integer> scoreList = matchInfo.getScore(Constants.getScoreType());
//        if (scoreList != null && scoreList.size() > 1) {
//            String scoreMain = String.valueOf(scoreList.get(0));
//            String scoreVisitor = String.valueOf(scoreList.get(1));
//            itemModel.mainScore.set(scoreMain);
//            itemModel.visitorScore.set(scoreVisitor);
//        }
        CfLog.d("================ _fetchMatchListInfo ====================");
        String teamMain = matchInfo.ts.get(0).na;
        String teamTeamVistor = matchInfo.ts.get(1).na;
        if (!TextUtils.isEmpty(teamMain)) {
            itemModel.mainTeamName.set(teamMain);
        }
        if (!TextUtils.isEmpty(teamTeamVistor)) {
            itemModel.visitorTeamName.set(teamTeamVistor);
        }

        String leagueName = matchInfo.lg.na;
        if (!TextUtils.isEmpty(leagueName)) {
            itemModel.leagueName.set(leagueName);
        }

//        String iconMain = match.getIconMain();
//        String iconVisitor = match.getIconVisitor();
//
//        if (!TextUtils.isEmpty(iconMain)) {
//            itemModel.mainTeamIcon.set(iconMain);
//        }
//
//        if (!TextUtils.isEmpty(iconVisitor)) {
//            itemModel.visitorTeamIcon.set(iconVisitor);
//        }

        //itemModel.isGoingOn.set(match.isGoingon() ? 1 : 0);

//        // 比赛未开始
//        if (!match.isGoingon()) {
//            itemModel.stage.set("");
//        } else {
//            if (TextUtils.equals(Constants.getFbSportId(), match.getSportId()) || TextUtils.equals(Constants.getBsbSportId(), match.getSportId())) { // 足球和篮球
//                itemModel.stage.set(match.getStage() + " " + match.getTime());
//            } else {
//                itemModel.stage.set(match.getStage());
//            }
//        }
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
