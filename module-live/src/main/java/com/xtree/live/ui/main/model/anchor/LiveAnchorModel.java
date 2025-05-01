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
import com.xtree.live.data.source.response.fb.Constants;
import com.xtree.live.data.source.response.fb.Match;
import com.xtree.live.ui.main.activity.LiveDetailActivity;
import com.xtree.live.ui.main.listener.FetchListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.xtree.mvvmhabit.base.BackPressed;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * Created by KAKA on 2024/9/9.
 * Describe: 直播TAB数据模型
 */
public class LiveAnchorModel extends BindModel implements BackPressed {

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

    public FetchListener<Match> matchInfoResponseFetchListener;

    public BaseDatabindingAdapter.onBindListener onBindListener = new BaseDatabindingAdapter.onBindListener() {

        @Override
        public void onItemClick(int modelPosition, int layoutPosition, int itemViewType) {
            //跳到直播间投注页
            ToastUtils.show("" + modelPosition, ToastUtils.ShowType.Default);
            Context context = mHolder.getContext();
            //判断当前是否是登录状态
            String token = SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN);
            boolean isLogin = !TextUtils.isEmpty(token);
            if (isLogin) {
                String matchID = ((LiveAnchorItemModel) bindModels.get(modelPosition)).getMatchId();
                int uid = ((LiveAnchorItemModel) bindModels.get(modelPosition)).getUid();
                String vid = ((LiveAnchorItemModel) bindModels.get(modelPosition)).getVid();
                LiveDetailActivity.forward(context, uid, vid, matchID);
            } else {
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

            if (matchInfoResponseFetchListener != null) {

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

    private void _fetchMatchInfo(LiveAnchorItemModel itemModel, Match match) {
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

    private void goLogin() {
        ARouter.getInstance().build(RouterActivityPath.Mine.PAGER_LOGIN_REGISTER).navigation();
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
