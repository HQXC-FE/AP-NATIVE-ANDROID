package com.xtree.bet.ui.viewmodel.callback;

import static com.xtree.base.net.HttpCallBack.CodeRule.CODE_14010;
import static com.xtree.base.net.HttpCallBack.CodeRule.CODE_401013;
import static com.xtree.base.net.HttpCallBack.CodeRule.CODE_401026;
import static com.xtree.base.net.HttpCallBack.CodeRule.CODE_401038;
import static com.xtree.bet.constant.SPKey.BT_LEAGUE_LIST_CACHE;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.xtree.base.net.HttpCallBack;
import com.xtree.bet.bean.response.pm.MatchInfo;
import com.xtree.bet.bean.response.pm.MatchLeagueListCacheRsp;
import com.xtree.bet.bean.response.pm.MatchListRsp;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.MatchPm;
import com.xtree.bet.bean.ui.Option;
import com.xtree.bet.bean.ui.OptionList;
import com.xtree.bet.bean.ui.PlayType;
import com.xtree.bet.ui.viewmodel.pm.PMMainViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.xtree.mvvmhabit.http.BusinessException;
import me.xtree.mvvmhabit.utils.SPUtils;

public class PMChampionListCacheCallBack extends HttpCallBack<MatchLeagueListCacheRsp> {

    private PMMainViewModel mViewModel;
    private boolean mHasCache;
    private boolean mIsTimerRefresh;
    private boolean mIsRefresh;
    private int mCurrentPage;
    private List<Long> mMatchids;
    private int mSportPos;
    private String mSportId;
    private int mOrderBy;
    private List<Long> mLeagueIds;
    private int mPlayMethodType;
    private int mOddType;

    public PMChampionListCacheCallBack(PMMainViewModel viewModel, int sportPos, String sportId, int orderBy, List<Long> leagueIds, List<Long> matchids, int playMethodType, int oddType, boolean isTimerRefresh, boolean isRefresh, int currentPage) {
        mViewModel = viewModel;
        mIsTimerRefresh = isTimerRefresh;
        mIsRefresh = isRefresh;
        mPlayMethodType = playMethodType;
        mSportPos = sportPos;
        mSportId = sportId;
        mOrderBy = orderBy;
        mLeagueIds = leagueIds;
        mOddType = oddType;
        mMatchids = matchids;
        mCurrentPage = currentPage;
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (!mIsTimerRefresh && !mHasCache) {
            mViewModel.getUC().getShowDialogEvent().postValue("");
        }
    }

    @Override
    public void onResult(MatchLeagueListCacheRsp matchListRsp) {
        if (mIsTimerRefresh) {
            setChampionOptionOddChange(matchListRsp.data.getData());
            mViewModel.championMatchTimerListData.postValue(mViewModel.mChampionMatchList);
            return;
        }

        if (mIsRefresh) {
            mViewModel.mChampionMatchList.clear();
            mViewModel.mChampionMatchInfoList.clear();
        }

        mViewModel.getUC().getDismissDialogEvent().call();
        if (mIsRefresh) {
            if (matchListRsp != null && mCurrentPage == matchListRsp.getPages()) {
                mViewModel.loadMoreWithNoMoreData();
            } else {
                mViewModel.finishRefresh(true);
            }
        } else {
            if (matchListRsp != null && mCurrentPage == matchListRsp.getPages()) {
                mViewModel.loadMoreWithNoMoreData();
            } else {
                mViewModel.finishLoadMore(true);
            }
        }
        mViewModel.mChampionMatchInfoList.addAll(matchListRsp.data.getData());
        if (TextUtils.isEmpty(mViewModel.mSearchWord)) {
            championLeagueList(matchListRsp.data.getData());
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
        if (t instanceof BusinessException) {
            BusinessException error = (BusinessException) t;
            if (error.code == CODE_401026 || error.code == CODE_401013 ) {
                mViewModel.getGameTokenApi();
            } else if (error.code == CODE_401038) {
                super.onError(t);
                mViewModel.tooManyRequestsEvent.call();
            } else {
                mViewModel.getChampionList(mSportPos, mSportId, mOrderBy, mLeagueIds, mMatchids, mPlayMethodType, mOddType, mIsTimerRefresh, mIsRefresh);
            }
        }
    }

    /**
     * 设置赔率变化
     *
     * @param matchInfoList
     */
    private void setChampionOptionOddChange(List<MatchInfo> matchInfoList) {
        List<Match> newMatchList = new ArrayList<>();
        Map<String, Match> map = new HashMap<>();

        for (Match match : mViewModel.mChampionMatchList) {
            if (!match.isHead()) {
                map.put(String.valueOf(match.getId()), match);
            }
        }

        for (MatchInfo matchInfo : matchInfoList) {
            Match newMatch = new MatchPm(matchInfo);
            newMatchList.add(newMatch);
        }

        List<Option> newOptonList = getChampionMatchOptionList(newMatchList);
        List<Option> oldOptonList = getChampionMatchOptionList(mViewModel.mChampionMatchList);

        for (Option newOption : newOptonList) {
            for (Option oldOption : oldOptonList) {
                if (oldOption != null && newOption != null
                        && oldOption.getRealOdd() != newOption.getRealOdd()
                        && TextUtils.equals(oldOption.getCode(), newOption.getCode())) {
                    newOption.setChange(oldOption.getRealOdd());
                    break;
                }
            }
        }

        for (Match match : newMatchList) {
            Match oldMatch = map.get(String.valueOf(match.getId()));
            if (oldMatch != null) {
                mViewModel.mChampionMatchList.set(mViewModel.mChampionMatchList.indexOf(oldMatch), match);
            }
        }

    }

    private List<Option> getChampionMatchOptionList(List<Match> matchList) {
        List<Option> optionArrayList = new ArrayList<>();
        for (Match match : matchList) {
            if (match.isHead()) {
                continue;
            }

            for (PlayType playType : match.getPlayTypeList()) {
                if (playType.getOptionLists() != null) {
                    for (OptionList optionList : playType.getOptionLists()) {
                        for (Option option : optionList.getOptionList()) {
                            if (option != null) {
                                StringBuffer code = new StringBuffer();
                                code.append(match.getId());
                                if (option != null) {
                                    code.append(option.getId());
                                }
                                option.setCode(code.toString());
                            }
                            optionArrayList.add(option);
                        }
                    }
                }
            }
        }
        return optionArrayList;
    }

    /**
     * @param matchInfoList
     * @return
     */
    private void championLeagueList(List<MatchInfo> matchInfoList) {
        if (!matchInfoList.isEmpty()) {
            Match header = new MatchPm();
            header.setHead(true);
            mViewModel.mChampionMatchList.add(header);
            for (MatchInfo matchInfo : matchInfoList) {
                Match match = new MatchPm(matchInfo);
                mViewModel.mChampionMatchList.add(match);
            }
        }
    }
}
