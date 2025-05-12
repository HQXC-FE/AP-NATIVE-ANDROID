package com.xtree.bet.ui.viewmodel.callback;

import static com.xtree.base.utils.BtDomainUtil.KEY_PLATFORM;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_PM;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_PMXC;
import static com.xtree.bet.constant.SPKey.BT_LEAGUE_LIST_CACHE;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.request.UploadExcetionReq;
import com.xtree.base.utils.CfLog;
import com.xtree.base.vo.BaseBean;
import com.xtree.bet.EventInfoByPageListParser;
import com.xtree.bet.R;
import com.xtree.bet.bean.response.im.EventInfoByPageListRsp;
import com.xtree.bet.bean.response.im.EventListRsp;
import com.xtree.bet.bean.response.im.LeagueInfo;
import com.xtree.bet.bean.response.im.MatchEvent;
import com.xtree.bet.bean.response.im.MatchInfo;
import com.xtree.bet.bean.response.im.RecommendedEvent;
import com.xtree.bet.bean.response.im.Sport;
import com.xtree.bet.bean.ui.League;
import com.xtree.bet.bean.ui.LeagueIm;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.MatchIm;
import com.xtree.bet.ui.activity.MainActivity;
import com.xtree.bet.ui.viewmodel.im.ImMainViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.xtree.mvvmhabit.http.BusinessException;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.Utils;

public class IMLeagueListCallBack extends HttpCallBack<EventInfoByPageListRsp> {

    private ImMainViewModel mViewModel;
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
    private int mSearchDatePos;
    private int mOddType;
    private boolean mIsStepSecond;
    private int mFinalType;
    private boolean mNoLiveMatch;
    private League mNoLiveheaderLeague;
    private Map<String, League> mMapSportType = new HashMap<>();
    private Map<String, League> mMapLeague = new HashMap<>();
    private List<League> mLeagueList = new ArrayList<>();
    private List<League> mGoingOnLeagueList = new ArrayList<>();
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

    public IMLeagueListCallBack(ImMainViewModel viewModel, boolean hasCache, boolean isTimerRefresh, boolean isRefresh,
                                int currentPage, int playMethodType, int sportPos, String sportId, int orderBy, List<Long> leagueIds,
                                int searchDatePos, int oddType, List<Long> matchids, int finalType, boolean isStepSecond) {
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
        mSearchDatePos = searchDatePos;
        mOddType = oddType;
        mMatchids = matchids;
        mIsStepSecond = isStepSecond;
        mFinalType = finalType;
        saveLeague();
    }

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

    public void saveLeague() {
        if (!mIsRefresh || mIsStepSecond) {
            mLeagueList = mViewModel.getLeagueList();
            CfLog.d("================= IMLeagueListCallBack mLeagueList =================="+mLeagueList.size());
            mGoingOnLeagueList = mViewModel.getGoingOnLeagueList();
            mMapLeague = mViewModel.getMapLeague();
            mMatchList = mViewModel.getMatchList();
            mMapMatch = mViewModel.getMapMatch();
            mMapSportType = mViewModel.getMapSportType();
            mNoLiveheaderLeague = mViewModel.getNoLiveheaderLeague();
            mLiveMatchList = mViewModel.getLiveMatchList();
            mNoliveMatchList = mViewModel.getNoliveMatchList();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mHasCache && !mIsStepSecond) {
            mViewModel.getUC().getShowDialogEvent().postValue("");
        }
    }

    @Override
    public void onResult(EventInfoByPageListRsp matchListRsp) {
        CfLog.d("================= IMLeagueListCallBack onResult ==================");
        matchListRsp = EventInfoByPageListParser.getEventInfoByPageListRsp(MainActivity.getContext());
        List<MatchInfo> matchInfoList = matchListRsp.getSports().get(0).getEvents();
        for (MatchInfo matchInfo : matchInfoList) {
            matchInfo.setSportId(matchListRsp.getSports().get(0).getSportId());
            matchInfo.setSportName(matchListRsp.getSports().get(0).getSportName());
        }

        mViewModel.getUC().getDismissDialogEvent().call();
        if (mIsRefresh) {
            mNoliveMatchList.clear();
            if (!mIsStepSecond) {
                mLeagueList.clear();
                mMapLeague.clear();
                mMapSportType.clear();
            }
            if (matchListRsp != null && mCurrentPage == matchListRsp.getPage()) {
                mViewModel.loadMoreWithNoMoreData();
            } else {
                mViewModel.finishRefresh(true);
            }
        } else {
            if (matchListRsp != null && mCurrentPage == matchListRsp.getPage()) {
                mViewModel.loadMoreWithNoMoreData();
            } else {
                mViewModel.finishLoadMore(true);
            }
        }
        mNoliveMatchList.addAll(matchInfoList);
        if (TextUtils.isEmpty(mViewModel.mSearchWord)) {
            leagueAdapterList(matchInfoList);
            if (mFinalType == 1) { // 滚球
                mViewModel.leagueLiveListData.postValue(mLeagueList);
            } else {
                mViewModel.leagueNoLiveListData.postValue(mLeagueList);
            }
        } else {
            searchMatch(mViewModel.mSearchWord);
        }

        if (mCurrentPage == 1) {
            SPUtils.getInstance().put(BT_LEAGUE_LIST_CACHE + mPlayMethodType + mSearchDatePos + mSportId, new Gson().toJson(mLeagueList));
        }
        mViewModel.saveLeague(this);
        mHasCache = false;
        mIsStepSecond = false;
    }

    @Override
    public void onError(Throwable t) {
        mViewModel.getUC().getDismissDialogEvent().call();
        if (t instanceof BusinessException) {
            BusinessException error = (BusinessException) t;
            if (error.isHttpError) {
                UploadExcetionReq uploadExcetionReq = new UploadExcetionReq();

                String platform = SPUtils.getInstance().getString(KEY_PLATFORM);
                String domainUrl = null;
                if (TextUtils.equals(platform, PLATFORM_PMXC)) {
                    domainUrl = SPUtils.getInstance().getString(SPKeyGlobal.PMXC_API_SERVICE_URL);
                    uploadExcetionReq.setLogTag("pmzy_url_error");
                } else if (TextUtils.equals(platform, PLATFORM_PM)) {
                    domainUrl = SPUtils.getInstance().getString(SPKeyGlobal.PM_API_SERVICE_URL);
                    uploadExcetionReq.setLogTag("pm_url_error");
                }

                uploadExcetionReq.setLogTag("pm_url_error");
                uploadExcetionReq.setApiUrl(domainUrl);
                uploadExcetionReq.setLogType("" + ((BusinessException) t).code);
                uploadExcetionReq.setMsg(((BusinessException) t).message);
                mViewModel.firstNetworkExceptionData.postValue(uploadExcetionReq);
            } else if (error.code == CodeRule.CODE_401026 || error.code == CodeRule.CODE_401013) {
                mViewModel.getGameTokenApi();
            } else if (error.code == CodeRule.CODE_401038) {
                super.onError(t);
                mViewModel.tooManyRequestsEvent.call();
            } else {
                mViewModel.getLeagueList(mSportPos, mSportId, mOrderBy, mLeagueIds, mMatchids, mPlayMethodType, mSearchDatePos, mOddType, mIsTimerRefresh, mIsRefresh);
            }
        }
    }

    public void searchMatch(String searchWord) {
        mLeagueList.clear();
        mMapLeague.clear();
        mMapSportType.clear();
        mNoLiveheaderLeague = null;
        if (!TextUtils.isEmpty(searchWord)) {
            if (!mLiveMatchList.isEmpty()) {
                List<MatchInfo> matchInfoList = new ArrayList<>();
                for (BaseBean matchInfo : mLiveMatchList) {
                    MatchIm matchIm = new MatchIm((com.xtree.bet.bean.response.im.MatchInfo) matchInfo);
                    if (matchIm.getLeague().getLeagueName().contains(searchWord) || matchIm.getTeamMain().contains(searchWord) || matchIm.getTeamVistor().contains(searchWord)) {
                        matchInfoList.add((MatchInfo) matchInfo);
                    }
                }
                leagueGoingList(matchInfoList);
            }
            if (!mNoliveMatchList.isEmpty()) {
                List<MatchInfo> matchInfoList = new ArrayList<>();
                for (BaseBean matchInfo : mNoliveMatchList) {
                    MatchIm matchFb = new MatchIm((MatchInfo) matchInfo);
                    if (matchFb.getLeague().getLeagueName().contains(searchWord) || matchFb.getTeamMain().contains(searchWord) || matchFb.getTeamVistor().contains(searchWord)) {
                        matchInfoList.add((MatchInfo) matchInfo);
                    }
                }
                leagueAdapterList(matchInfoList);
            }
        } else {
            if (!mLiveMatchList.isEmpty()) {
                List<MatchInfo> matchInfoList = new ArrayList<>();
                for (BaseBean matchInfo : mLiveMatchList) {
                    matchInfoList.add((MatchInfo) matchInfo);
                }
                leagueGoingList(matchInfoList);
            }
            if (!mNoliveMatchList.isEmpty()) {
                List<MatchInfo> matchInfoList = new ArrayList<>();
                for (BaseBean matchInfo : mNoliveMatchList) {
                    matchInfoList.add((MatchInfo) matchInfo);
                }
                leagueAdapterList(matchInfoList);
            }
        }
        mViewModel.leagueNoLiveListData.postValue(mLeagueList);
    }

    /**
     * 新建进行中的联赛分类信息
     *
     * @param liveheaderLeague
     */
    public void buildLiveHeaderLeague(League liveheaderLeague) {
        liveheaderLeague.setHead(true);
        liveheaderLeague.setHeadType(League.HEAD_TYPE_LIVE_OR_NOLIVE);
        liveheaderLeague.setLeagueName(Utils.getContext().getResources().getString(R.string.bt_game_going_on));
        mGoingOnLeagueList.add(liveheaderLeague);
    }

    /**
     * 新建进行中比赛种类头部信息，足球、篮球等
     *
     * @param mapSportType
     * @param match
     */
    public void buildLiveSportHeader(Map<String, League> mapSportType, Match match, League league) {
        League sportHeader = mapSportType.get(match.getSportId());
        if (sportHeader == null) {
            League sportHeaderLeague = league;
            sportHeaderLeague.setHead(true);
            sportHeaderLeague.setHeadType(League.HEAD_TYPE_SPORT_NAME);
            sportHeaderLeague.setLeagueName(match.getSportName());
            sportHeaderLeague.setMatchCount(1);
            mGoingOnLeagueList.add(sportHeaderLeague);
            mapSportType.put(match.getSportId(), sportHeaderLeague);
        } else {
            sportHeader.setMatchCount(1);
        }
    }

    public void leagueGoingList(List<MatchInfo> matchInfoList) {
        if (matchInfoList.isEmpty()) {
            mViewModel.mNoLiveMatch = true;
            return;
        }

        League liveHeaderLeague = new LeagueIm();
        buildLiveHeaderLeague(liveHeaderLeague);

        Map<String, League> mapLeague = new HashMap<>();
        Map<String, League> mapSportType = new HashMap<>();
        for (MatchInfo matchInfo : matchInfoList) {
            Match match = new MatchIm(matchInfo);

            buildLiveSportHeader(mapSportType, match, new LeagueIm());

            League league = mapLeague.get(String.valueOf(matchInfo.competition.getCompetitionId()));
            if (league == null) {
                LeagueInfo leagueInfo = new LeagueInfo();
                //leagueInfo.picUrlthumb = matchInfo.lurl;
                leagueInfo.nameText = matchInfo.sportName;
                leagueInfo.tournamentId = Long.valueOf(matchInfo.competition.getCompetitionId());
                league = new LeagueIm(leagueInfo);
                mapLeague.put(String.valueOf(matchInfo.competition.getCompetitionId()), league);

                mGoingOnLeagueList.add(league);
                //mMapGoingOnLeague.put(String.valueOf(matchInfo.lg.id), league);
            }
            league.getMatchList().add(match);

            if (mMapMatch.get(String.valueOf(match.getId())) == null) {
                mMapMatch.put(String.valueOf(match.getId()), match);
                mMatchList.add(match);
            } else {
                int index = mMatchList.indexOf(mMapMatch.get(String.valueOf(match.getId())));
                if (index > -1) {
                    mMatchList.set(index, match);
                }
            }

        }

    }

    /**
     * @param matchInfoList
     * @return
     */
    public void leagueAdapterList(List<MatchInfo> matchInfoList) {
        CfLog.d("============= IMLeagueListCallBack leagueAdapterList ==============="+matchInfoList.size());
        int noLiveMatchSize = matchInfoList == null ? 0 : matchInfoList.size();
        buildNoLiveHeaderLeague(new LeagueIm(), noLiveMatchSize);

        Map<String, League> mapLeague = new HashMap<>();
        for (MatchInfo matchInfo : matchInfoList) {
            Match match = new MatchIm(matchInfo);

            buildNoLiveSportHeader(match, new LeagueIm());

            League league = mMapLeague.get(String.valueOf(matchInfo.competition.getCompetitionId()));
            if (league == null) {
                LeagueInfo leagueInfo = new LeagueInfo();
                //leagueInfo.picUrlthumb = matchInfo.lurl;
                leagueInfo.nameText = matchInfo.competition.getCompetitionName();
                leagueInfo.tournamentId = Long.valueOf(matchInfo.competition.getCompetitionId());
                CfLog.d("============ IMLeagueListCallBack leagueAdapterList leagueInfo =============="+leagueInfo);
                league = new LeagueIm(leagueInfo);
                mapLeague.put(String.valueOf(matchInfo.competition.getCompetitionId()), league);

                mLeagueList.add(league);
                mMapLeague.put(String.valueOf(matchInfo.competition.getCompetitionId()), league);
            }

            league.getMatchList().add(match);


            if (mMapMatch.get(String.valueOf(match.getId())) == null) {
                mMapMatch.put(String.valueOf(match.getId()), match);
                mMatchList.add(match);
            } else {
                int index = mMatchList.indexOf(mMapMatch.get(String.valueOf(match.getId())));
                if (index > -1) {
                    mMatchList.set(index, match);
                }
            }
        }

    }

    /**
     * 新建未开赛联赛分类信息
     *
     * @param league
     */
    public void buildNoLiveHeaderLeague(League league, int noLiveMatchSize) {
        if (!mGoingOnLeagueList.isEmpty() && mLeagueList.isEmpty()) {
            mLeagueList.addAll(mGoingOnLeagueList);
            mNoLiveheaderLeague = league;
            mNoLiveheaderLeague.setHead(true);
            mNoLiveheaderLeague.setHeadType(League.HEAD_TYPE_LIVE_OR_NOLIVE);
            mNoLiveheaderLeague.setLeagueName(Utils.getContext().getResources().getString(R.string.bt_game_waiting));
            mLeagueList.add(mNoLiveheaderLeague);
            mGoingOnLeagueList.clear();
        } else if (mNoLiveheaderLeague == null) {
            if (noLiveMatchSize > 0) {
                mNoLiveheaderLeague = league;
                mNoLiveheaderLeague.setHead(true);
                mNoLiveheaderLeague.setHeadType(League.HEAD_TYPE_LIVE_OR_NOLIVE);
                if (mViewModel.mNoLiveMatch) {
                    mNoLiveheaderLeague.setLeagueName(Utils.getContext().getResources().getString(R.string.bt_game_waiting));
                } else {
                    mNoLiveheaderLeague.setLeagueName(Utils.getContext().getResources().getString(R.string.bt_all_league));
                }
                mLeagueList.add(mNoLiveheaderLeague);
            }
            mViewModel.mNoLiveMatch = false;
        }
    }

    /**
     * 新建未开赛比赛种类头部信息，足球、篮球等
     *
     * @param match
     * @param league
     */
    public void buildNoLiveSportHeader(Match match, League league) {
        League sportHeader = mMapSportType.get(match.getSportId());
        CfLog.d("=========== IMLeagueListCallBack buildNoLiveSportHeader mMapSportType ========"+mMapSportType);
        if (sportHeader == null) {
            League sportHeaderLeague = league;
            sportHeaderLeague.setHead(true);
            sportHeaderLeague.setHeadType(League.HEAD_TYPE_SPORT_NAME);
            sportHeaderLeague.setLeagueName(match.getSportName());
            CfLog.d("=========== IMLeagueListCallBack buildNoLiveSportHeader match.getSportName()========"+match.getSportName());
            sportHeaderLeague.setMatchCount(1);
            if (!mGoingOnLeagueList.isEmpty() && mLeagueList.isEmpty()) { // 进行中
                mGoingOnLeagueList.add(sportHeaderLeague);
            } else {  // 未开赛
                mLeagueList.add(sportHeaderLeague);
            }
            mMapSportType.put(match.getSportId(), sportHeaderLeague);
        } else {
            sportHeader.setMatchCount(1);
        }
    }
}
