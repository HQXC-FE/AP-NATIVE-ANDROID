package com.xtree.bet.ui.viewmodel.callback;

import static com.xtree.bet.constant.SPKey.BT_LEAGUE_LIST_CACHE;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.vo.BaseBean;
import com.xtree.bet.bean.response.fb.FbMatchListCacheRsp;
import com.xtree.bet.bean.ui.League;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.ui.viewmodel.fb.FBMainViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.xtree.mvvmhabit.http.ResponseThrowable;
import me.xtree.mvvmhabit.utils.KLog;
import me.xtree.mvvmhabit.utils.SPUtils;

public class FBChampionListCacheCallBack extends HttpCallBack<FbMatchListCacheRsp> {
    private FBMainViewModel mViewModel;
    private boolean mHasCache;
    private boolean mIsTimerRefresh;
    private boolean mIsRefresh;
    private int mCurrentPage;
    private int mPlayMethodType;
    private int mSportPos;
    private String mSportId;
    private int mOrderBy;
    private List<Long> mLeagueIds;
    private int mSearchDatePos;
    private int mOddType;
    private List<Long> mMatchids;
    /**
     * 是否获取今日中未开赛比赛列表
     */
    private boolean mIsStepSecond;
    private Map<String, League> mMapSportType = new HashMap<>();
    private List<League> mLeagueList = new ArrayList<>();
    private List<League> mGoingOnLeagueList = new ArrayList<>();
    private Map<String, League> mMapLeague = new HashMap<>();
    private Map<String, Match> mMapMatch = new HashMap<>();
    private List<Match> mMatchList = new ArrayList<>();

    /**
     * 正在进行中的比赛
     */
    private List<BaseBean> mLiveMatchList = new ArrayList<>();
    /**
     * 未开始的比赛
     */
    private List<BaseBean> mNoliveMatchList = new ArrayList<>();
    private League mNoLiveheaderLeague;

    public Map<String, League> getMapSportType() {
        return mMapSportType;
    }

    public List<League> getLeagueList() {
        return mLeagueList;
    }

    public List<League> getGoingOnLeagueList() {
        return mGoingOnLeagueList;
    }

    public Map<String, League> getMapLeague() {
        return mMapLeague;
    }

    public Map<String, Match> getMapMatch() {
        return mMapMatch;
    }

    public List<Match> getMatchList() {
        return mMatchList;
    }

    public League getNoLiveheaderLeague() {
        return mNoLiveheaderLeague;
    }

    public List<BaseBean> getLiveMatchList() {
        return mLiveMatchList;
    }

    public List<BaseBean> getNoliveMatchList() {
        return mNoliveMatchList;
    }


    public FBChampionListCacheCallBack(FBMainViewModel viewModel, boolean hasCache, boolean isTimerRefresh, boolean isRefresh,
                                       int currentPage, int playMethodType, int sportPos, String sportId,
                                       int orderBy, List<Long> leagueIds, int oddType, List<Long> matchids) {
        mViewModel = viewModel;
        mHasCache = hasCache;
        mIsTimerRefresh = isTimerRefresh;
        mIsRefresh = isRefresh;
        mCurrentPage = currentPage;
        mPlayMethodType = playMethodType;
        mSportPos = sportPos;
        mSportId = sportId;
        mOrderBy = orderBy;
        mLeagueIds = leagueIds;
        mOddType = oddType;
        mMatchids = matchids;
        //saveLeague();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mIsTimerRefresh && !mHasCache) {
            mViewModel.getUC().getShowDialogEvent().postValue("");
        }
    }

    @Override
    public void onResult(FbMatchListCacheRsp matchListRsp) {
        if (mIsTimerRefresh) {
            mViewModel.setChampionOptionOddChange(matchListRsp.data.records);
            mViewModel.championMatchTimerListData.postValue(mViewModel.mChampionMatchList);
            return;
        }

        mViewModel.getUC().getDismissDialogEvent().call();
        if (mIsRefresh) {
            mViewModel.mChampionMatchList.clear();
            mViewModel.mChampionMatchInfoList.clear();
            mViewModel.mChampionMatchMap.clear();
            if (matchListRsp != null && mCurrentPage == matchListRsp.data.getPages()) {
                mViewModel.loadMoreWithNoMoreData();
            } else {
                mViewModel.finishRefresh(true);
            }
        } else {
            if (matchListRsp != null && mCurrentPage == matchListRsp.data.getPages()) {
                mViewModel.loadMoreWithNoMoreData();
            } else {
                mViewModel.finishLoadMore(true);
            }
        }
        mViewModel.mChampionMatchInfoList.addAll(matchListRsp.data.records);
        if (TextUtils.isEmpty(mViewModel.mSearchWord)) {
            mViewModel.championLeagueList(matchListRsp.data.records);
            mViewModel.championMatchListData.postValue(mViewModel.mChampionMatchList);
        } else {
            mViewModel.searchMatch(mViewModel.mSearchWord, true);
        }
        if (mCurrentPage == 1) {
            SPUtils.getInstance().put(BT_LEAGUE_LIST_CACHE + mPlayMethodType + mSportId, new Gson().toJson(mViewModel.mChampionMatchList));
        }
        mHasCache = false;
    }

    @Override
    public void onError(Throwable t) {
        mViewModel.getUC().getDismissDialogEvent().call();
        if (t instanceof ResponseThrowable) {
            ResponseThrowable rError = (ResponseThrowable) t;
            KLog.e("##### FBChampionListCacheCallBack onError: ", rError+" #####");
            if (((ResponseThrowable) t).code == HttpCallBack.CodeRule.CODE_14010) {
                mViewModel.getGameTokenApi();
            } else {
                mViewModel.getChampionList(mSportPos, mSportId, mOrderBy, mLeagueIds, mMatchids, mPlayMethodType, mOddType, mIsTimerRefresh, mIsRefresh);
            }
        }
    }


}
