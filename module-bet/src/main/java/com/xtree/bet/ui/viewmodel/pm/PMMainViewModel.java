package com.xtree.bet.ui.viewmodel.pm;

import static com.xtree.base.utils.BtDomainUtil.KEY_PLATFORM;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_PM;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_PMXC;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.utils.TimeUtils;
import com.xtree.bet.bean.request.pm.PMListReq;
import com.xtree.bet.bean.response.SportsCacheSwitchInfo;
import com.xtree.bet.bean.response.fb.FBAnnouncementInfo;
import com.xtree.bet.bean.response.pm.FrontListInfo;
import com.xtree.bet.bean.response.pm.LeagueInfo;
import com.xtree.bet.bean.response.pm.MatchInfo;
import com.xtree.bet.bean.response.pm.MenuInfo;
import com.xtree.bet.bean.response.pm.PMResultBean;
import com.xtree.bet.bean.ui.League;
import com.xtree.bet.bean.ui.LeaguePm;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.MatchPm;
import com.xtree.bet.constant.PMConstants;
import com.xtree.bet.constant.SportTypeItem;
import com.xtree.bet.data.BetRepository;
import com.xtree.bet.ui.viewmodel.MainViewModel;
import com.xtree.bet.ui.viewmodel.SportCacheType;
import com.xtree.bet.ui.viewmodel.TemplateMainViewModel;
import com.xtree.bet.ui.viewmodel.callback.PMChampionListCacheCallBack;
import com.xtree.bet.ui.viewmodel.callback.PMChampionListCallBack;
import com.xtree.bet.ui.viewmodel.callback.PMHotMatchCountCacheCallBack;
import com.xtree.bet.ui.viewmodel.callback.PMHotMatchCountCallBack;
import com.xtree.bet.ui.viewmodel.callback.PMLeagueListCacheCallBack;
import com.xtree.bet.ui.viewmodel.callback.PMLeagueListCallBack;
import com.xtree.bet.ui.viewmodel.callback.PMListCacheCallBack;
import com.xtree.bet.ui.viewmodel.callback.PMListCallBack;

import org.reactivestreams.Subscriber;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import com.xtree.base.http.BusinessException;
import com.xtree.base.utils.KLog;
import com.xtree.base.utils.RxUtils;
import com.xtree.base.utils.SPUtils;

/**
 * Created by marquis
 */

public class PMMainViewModel extends TemplateMainViewModel implements MainViewModel {
    private List<Match> mMatchList = new ArrayList<>();
    public List<Match> mChampionMatchList = new ArrayList<>();
    private Map<String, Match> mMapMatch = new HashMap<>();
    private Map<String, League> mMapLeague = new HashMap<>();
    public List<MatchInfo> mChampionMatchInfoList = new ArrayList<>();
    private Map<String, List<SportTypeItem>> sportCountMap = new HashMap<>();
    private List<MenuInfo> mMenuInfoList = new ArrayList<>();
    private PMLeagueListCallBack mPmLeagueCallBack;
    private PMLeagueListCacheCallBack mPmCacheLeagueCallBack;

    private HashMap<Integer, SportTypeItem> mMatchGames = new HashMap<>();

    private int mGoingOnPageSize = 300;
    private int mPageSize = 20;
    /**
     * 获取赛事列表
     *
     * @param sportId
     * @param orderBy
     * @param leagueIds
     * @param matchidList
     * @param playMethodType
     * @param searchDatePos  查询时间列表中的位置
     * @param oddType        盘口类型
     * @param isTimedRefresh 是否定时刷新 true-是，false-否
     * @param isRefresh      是否刷新 true-是, false-否
     */

    private int mPlayType;

    public PMMainViewModel(@NonNull Application application, BetRepository repository) {
        super(application, repository);
        sportItemData.postValue(new String[]{});
    }

    public void saveLeague(PMListCallBack pmListCallBack) {
        mLeagueList = pmListCallBack.getLeagueList();
        mGoingOnLeagueList = pmListCallBack.getGoingOnLeagueList();
        mMapLeague = pmListCallBack.getMapLeague();
        mMatchList = pmListCallBack.getMatchList();
        mMapMatch = pmListCallBack.getMapMatch();
        mMapSportType = pmListCallBack.getMapSportType();
    }

    public void saveLeague(PMLeagueListCallBack pmListCallBack) {
        mLeagueList = pmListCallBack.getLeagueList();
        mGoingOnLeagueList = pmListCallBack.getGoingOnLeagueList();
        mMapLeague = pmListCallBack.getMapLeague();
        mMatchList = pmListCallBack.getMatchList();
        mMapMatch = pmListCallBack.getMapMatch();
        mMapSportType = pmListCallBack.getMapSportType();
        mNoLiveheaderLeague = pmListCallBack.getNoLiveheaderLeague();
    }

    public void saveLeague(PMListCacheCallBack pmListCallBack) {
        mLeagueList = pmListCallBack.getLeagueList();
        mGoingOnLeagueList = pmListCallBack.getGoingOnLeagueList();
        mMapLeague = pmListCallBack.getMapLeague();
        mMatchList = pmListCallBack.getMatchList();
        mMapMatch = pmListCallBack.getMapMatch();
        mMapSportType = pmListCallBack.getMapSportType();
    }

    public void saveLeague(PMLeagueListCacheCallBack pmListCallBack) {
        mLeagueList = pmListCallBack.getLeagueList();
        mGoingOnLeagueList = pmListCallBack.getGoingOnLeagueList();
        mMapLeague = pmListCallBack.getMapLeague();
        mMatchList = pmListCallBack.getMatchList();
        mMapMatch = pmListCallBack.getMapMatch();
        mMapSportType = pmListCallBack.getMapSportType();
    }

    public Map<String, League> getMapLeague() {
        return mMapLeague;
    }

    public List<Match> getMatchList() {
        return mMatchList;
    }

    public Map<String, Match> getMapMatch() {
        return mMapMatch;
    }

    public void setSportItems(int playMethodPos, int playMethodType) {
        sportItemData.postValue(new String[]{});
    }

    /**
     * 获取热门联赛赛事数量
     *
     * @param leagueIds
     */
    @Override
    public void getHotMatchCount(int playMethodType, List<Long> leagueIds) {

        if (leagueIds.isEmpty()) {
            return;
        }
        PMListReq pmListReq = new PMListReq();
        pmListReq.setCuid();
        pmListReq.setCpn(mCurrentPage);
        pmListReq.setCps(mGoingOnPageSize);

        String sportIds = "";
        if (mMenuInfoList.isEmpty()) {
            hotEmptyMatchCountData.postValue(0);
            return;
        } else {
            for (MenuInfo menuInfo : mMenuInfoList) {
                if (playMethodType == menuInfo.menuType) {
                    for (MenuInfo subMenu : menuInfo.subList) {
                        sportIds += subMenu.menuId + ",";
                    }
                }
            }
        }
        pmListReq.setEuid(sportIds);
        if (leagueIds != null && !leagueIds.isEmpty()) {
            String leagueids = "";
            for (Long leagueid : leagueIds) {
                leagueids += leagueid + ",";
            }
            pmListReq.setTid(leagueids.substring(0, leagueids.length() - 1));
        }

        pmListReq.setType(3);
        // 获取 Flowable 对象
        Flowable flowable = getFlowableMatchesPagePB(pmListReq);
        // 根据是否使用缓存选择不同的回调
        Object callback = isUseCacheApiService()
                ? new PMHotMatchCountCacheCallBack(this)
                : new PMHotMatchCountCallBack(this);

        // 1.创建 Disposable，2.并进行订阅
        Disposable disposable = createDisposable(flowable, callback);
        addSubscribe(disposable);
    }

    @Override
    public void searchMatch(String searchWord, boolean isChampion) {
        mSearchWord = searchWord;
        mIsChampion = isChampion;
        if (!isChampion) {
            if (mPmLeagueCallBack != null) {
                mPmLeagueCallBack.searchMatch(searchWord);
            } else if (mPmCacheLeagueCallBack != null) {
                mPmCacheLeagueCallBack.searchMatch(searchWord);
            }
        } else {
            mChampionMatchList.clear();
            if (!TextUtils.isEmpty(searchWord)) {
                List<MatchInfo> matchInfoList = new ArrayList<>();
                for (MatchInfo matchInfo : mChampionMatchInfoList) {
                    MatchPm matchPm = new MatchPm(matchInfo);
                    if (matchPm.getLeague().getLeagueName().contains(searchWord)) {
                        matchInfoList.add(matchInfo);
                    }
                }
                championLeagueList(matchInfoList);
            } else {
                championLeagueList(mChampionMatchInfoList);
            }
            championMatchListData.postValue(mChampionMatchList);
        }
    }

    public void getLeagueList(int sportPos, String sportId, int orderBy, List<Long> leagueIds, List<Long> matchidList, int playMethodType, int searchDatePos, int oddType, boolean isTimerRefresh, boolean isRefresh) {
        getLeagueList(sportPos, sportId, orderBy, leagueIds, matchidList, playMethodType, searchDatePos, oddType, isTimerRefresh, isRefresh, false);
    }

    /**
     * 获取赛事列表
     *
     * @param sportId
     * @param orderBy
     * @param leagueIds
     * @param matchidList
     * @param playMethodType
     * @param searchDatePos  查询时间列表中的位置
     * @param oddType        盘口类型
     * @param isRefresh      是否刷新 true-是, false-否
     */

    public void getLeagueList(int sportPos, String sportId, int orderBy, List<Long> leagueIds, List<Long> matchidList, int playMethodType, int searchDatePos, int oddType, boolean isTimerRefresh, boolean isRefresh, boolean isStepSecond) {
        int type;
        boolean flag = false;
        if (!isStepSecond) {
            mPlayMethodType = playMethodType;
        }

        if (isRefresh) {
            mCurrentPage = 1;
        } else if (!isTimerRefresh) {
            mCurrentPage++;
        }

        //if (mCurrentPage == 1 && !isTimerRefresh && !isStepSecond) {
            //showCache(sportId, mPlayMethodType, searchDatePos);
        //}

        PMListReq pmListReq = new PMListReq();
        pmListReq.setCuid();
        pmListReq.setEuid(String.valueOf(sportId));
        pmListReq.setMids(matchidList);

        if (isRefresh) {
            type = playMethodType == 3 || (playMethodType == 11 && searchDatePos == 0) ? 1 : playMethodType;
            flag = playMethodType == 3 || (playMethodType == 11 && searchDatePos == 0) ? true : false;
        } else {
            type = playMethodType == 3 || (playMethodType == 11 && searchDatePos == 0) ? 2 : playMethodType;
        }

        if (playMethodType != 2) {
            mPlayType = playMethodType;
        }

        if (type == 2) { // 今日和串关分开两次请求，第一次先获取滚球信息，第二次再获取今日赛事信息，type为2时，代表第二次的请求
            type = 3; // 不带时间查询条件时
            if (searchDatePos > 0) { // 带时间查询条件时
                type = 4;
            }
        }

        if (mMenuInfoList.isEmpty()) {
            pmListReq.setEuid(sportId);
        } else {
            for (MenuInfo menuInfo : mMenuInfoList) {
                boolean isFound = false;
                if (mPlayType == menuInfo.menuType) {
                    String sportIds = "";
                    for (MenuInfo subMenu : menuInfo.subList) {
                        if (TextUtils.equals(sportId, "0") || TextUtils.equals(sportId, "1111")) {
                            sportIds += subMenu.menuId + ",";
                        } else {
                            if (TextUtils.equals(String.valueOf(subMenu.menuId), sportId)) {
                                isFound = true;
                                pmListReq.setEuid(String.valueOf(subMenu.menuId));
                                break;
                            }
                        }
                    }
                    if (TextUtils.equals(sportId, "0") || TextUtils.equals(sportId, "1111")) {
                        isFound = true;
                        pmListReq.setEuid(sportIds.substring(0, sportIds.length() - 1));
                    }
                }
                if (isFound) {
                    break;
                }
            }
        }

        final int finalType = type;
        /**
         * 是否是今日玩法中，先获取滚球所有赛事列表后，第二步获取今日进行中的赛事列表
         */
        final boolean needSecondStep = flag;

        pmListReq.setType(type);
        if (type == 1 && flag) {
            pmListReq.setType(3);
        }

        pmListReq.setSort(orderBy);
        if (leagueIds != null && !leagueIds.isEmpty()) {
            String leagueids = "";
            for (Long leagueid : leagueIds) {
                leagueids += leagueid + ",";
            }
            pmListReq.setTid(leagueids.substring(0, leagueids.length() - 1));
        }
        pmListReq.setCpn(mCurrentPage);
        pmListReq.setDevice("v2_h5_st");

        if (!dateList.isEmpty()) {
            if (searchDatePos == dateList.size() - 1) {
                String time = TimeUtils.parseTime(dateList.get(searchDatePos), TimeUtils.FORMAT_YY_MM_DD) + " 12:00:00";
                pmListReq.setMd(String.valueOf(0 - TimeUtils.strFormatDate(time, TimeUtils.FORMAT_YY_MM_DD_HH_MM_SS).getTime()));
            } else if (searchDatePos > 0) {
                String time = TimeUtils.parseTime(dateList.get(searchDatePos), TimeUtils.FORMAT_YY_MM_DD) + " 12:00:00";
                pmListReq.setMd(String.valueOf(TimeUtils.strFormatDate(time, TimeUtils.FORMAT_YY_MM_DD_HH_MM_SS).getTime()));
            }
        }

        Flowable flowable = getFlowableMatchesPagePB(pmListReq);
        if (isStepSecond) {
            flowable = getFlowableNoLiveMatchesPagePB(pmListReq);
        }
        pmListReq.setCps(mPageSize);
        if (type == 1) {// 滚球
            if (needSecondStep) {
                pmListReq.setCps(mGoingOnPageSize);
                flowable = getFlowableLiveMatchesPB(pmListReq);
            }
        }

        if (isTimerRefresh) {
            flowable = getFlowableMatchBaseInfoByMidsPB(pmListReq);
        }

        if (isRefresh) {
            mNoLiveheaderLeague = null;
        }

        if ((type == 1 && needSecondStep) // 获取今日中的全部滚球赛事列表
                || isTimerRefresh) { // 定时刷新赔率变更
            createPMListCallback(isTimerRefresh, isRefresh, sportPos, sportId, orderBy, leagueIds, searchDatePos, oddType, matchidList, flowable);
        } else {
            createPMLeagueListCallback(isTimerRefresh, isRefresh, sportPos, sportId, orderBy, leagueIds, searchDatePos, oddType, matchidList, finalType, isStepSecond, flowable);
        }
    }

    /**
     * 获取冠军赛事列表
     *
     * @param sportId
     * @param orderBy
     * @param leagueIds
     * @param matchids
     * @param playMethodType
     * @param oddType
     * @param isTimerRefresh
     * @param isRefresh
     */
    public void getChampionList(int sportPos, String sportId, int orderBy, List<Long> leagueIds, List<Long> matchids, int playMethodType, int oddType, boolean isTimerRefresh, boolean isRefresh) {
        if (TextUtils.equals("0", sportId)) {
            championMatchListData.postValue(new ArrayList<>());
            return;
        }

        if (isRefresh) {
            mCurrentPage = 1;
        } else {
            mCurrentPage++;
        }

        if (mCurrentPage == 1 && !isTimerRefresh) {
            showChampionCache(sportId, playMethodType);
        }

        PMListReq pmListReq = new PMListReq();
        pmListReq.setCuid();
        pmListReq.setType(playMethodType);
        pmListReq.setSort(orderBy);
        pmListReq.setCpn(mCurrentPage);
        pmListReq.setCps(300);

        if (mMenuInfoList.isEmpty()) {
            pmListReq.setEuid(sportId);
        } else {
            pmListReq.setEuid(sportId);
        }//再试试断网情况 和弱网情况

        Flowable flowable = getFlowableNoLiveMatchesPagePB(pmListReq);
        Object callback = isUseCacheApiService()
                ? new PMChampionListCacheCallBack(this, sportPos, sportId, orderBy, leagueIds, matchids, playMethodType, oddType, isTimerRefresh, isRefresh, mCurrentPage)
                : new PMChampionListCallBack(this, sportPos, sportId, orderBy, leagueIds, matchids, playMethodType, oddType, isTimerRefresh, isRefresh, mCurrentPage);
        // 1.创建 Disposable，2.并进行订阅
        Disposable disposable = createDisposable(flowable, callback);
        addSubscribe(disposable);
    }

    /**
     * 获取赛事统计数据
     */
    public void statistical(int playMethodType) {
        Map<String, String> map = new HashMap<>();
        String platform = SPUtils.getInstance().getString(KEY_PLATFORM);
        map.put("cuid", SPUtils.getInstance().getString(SPKeyGlobal.PM_USER_ID));
        if (TextUtils.equals(platform, PLATFORM_PMXC)) {
            map.put("cuid", SPUtils.getInstance().getString(SPKeyGlobal.PMXC_USER_ID));
        }
        map.put("sys", "7");
        Disposable disposable = (Disposable) model.getPMApiService().initPB(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<List<MenuInfo>>() {
                    @Override
                    public void onResult(List<MenuInfo> menuInfoList) {
                        mMenuInfoList = menuInfoList;
                        if (mMatchGames.isEmpty()) {
                            mMatchGames = PMConstants.getMatchGames();
                        }
                        for (MenuInfo menuInfo : menuInfoList) {
                            //"3", "1", "4", "11", "100"; 只有"今日", "滚球", "早盘", "串关", "冠军"数据才添加，提升效率
                            if (menuInfo.menuType == 3 || menuInfo.menuType == 1 || menuInfo.menuType == 4 || menuInfo.menuType == 11
                                    || menuInfo.menuType == 100) {
                                ArrayList<SportTypeItem> sportTypeItemList = new ArrayList<>();
                                //Map<String, Integer> sslMap = new HashMap<>();
                                if (menuInfo.menuType == 3 || menuInfo.menuType == 11) {//今日 串关 加热门
                                    SportTypeItem item1 = new SportTypeItem();
                                    item1.id = 1111;
                                    item1.num = 0;
                                    sportTypeItemList.add(item1);
                                } else if (menuInfo.menuType == 1) {//滚球 加全部
                                    SportTypeItem item2 = new SportTypeItem();
                                    item2.id = 0;
                                    item2.num = menuInfo.count;
                                    sportTypeItemList.add(item2);
                                }
                                for (MenuInfo subMenu : menuInfo.subList) {
                                    if (subMenu.count <= 0 || mMatchGames.get(subMenu.menuType) == null) {
                                        continue;
                                    }

                                    SportTypeItem item = new SportTypeItem();
                                    item.id = subMenu.menuType;
                                    item.menuId = subMenu.menuId;
                                    item.num = subMenu.count;
                                    sportTypeItemList.add(item);
                                    sportCountMap.put(String.valueOf(menuInfo.menuType), sportTypeItemList);
                                }

                            }
                        }

                        statisticalData.postValue(sportCountMap);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }
                });
        addSubscribe(disposable);
    }

    /**
     * 获取联赛列表
     */
    public void getOnSaleLeagues(int sportId, int type) {

    }

    /**
     * 获取赛果配置参数
     */
    public void postMerchant() {
        Map<String, String> map = new HashMap<>();
        map.put("languageType", "CMN");
        Disposable disposable = (Disposable) model.getPMApiService().resultMenuPB(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<List<PMResultBean>>() {
                    @Override
                    public void onResult(List<PMResultBean> list) {
                        List<SportTypeItem> list1 = new ArrayList<>();
                        for (PMResultBean i : list) {
                            if (PMConstants.getMatchGames().get(i.getMenuType()) == null
                                    || i.getMenuType() == 3001 || i.getMenuType() == 3002 || i.getMenuType() == 3003) {
                                //体育赛果隐藏英雄联盟、DOTA2、王者荣耀
                                continue;
                            }
                            SportTypeItem item = new SportTypeItem();
                            item.id = i.getMenuType();
                            item.menuId = Integer.parseInt(i.getMenuId());
                            list1.add(item);
                        }
                        ConcurrentHashMap<String, List<SportTypeItem>> sportMap = new ConcurrentHashMap<>();
                        sportMap.put("1", list1);
                        sportMap.put("2", new ArrayList<>());
                        resultData.setValue(sportMap);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    /**
     * 获取赛果信息赛事列表
     */
    public void matchResultPage(String beginTime, String endTime, int playMethodPos, String sportId) {
        Map<String, String> map = new HashMap<>();
        map.put("euid", sportId);
        map.put("type", "28");
        map.put("sort", "2");
        map.put("device", "v2_h5_st");
        map.put("tid", "");
        map.put("md", beginTime);
        String platform = SPUtils.getInstance().getString(KEY_PLATFORM);
        if (TextUtils.equals(platform, PLATFORM_PMXC)) {
            map.put("cuid", SPUtils.getInstance().getString(SPKeyGlobal.PMXC_USER_ID));
        } else {
            map.put("cuid", SPUtils.getInstance().getString(SPKeyGlobal.PM_USER_ID));
        }
        Disposable disposable = (Disposable) model.getPMApiService().matcheResultPB(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<List<MatchInfo>>() {
                    @Override
                    public void onResult(List<MatchInfo> data) {

                        ArrayList<League> leagues = new ArrayList<League>();
                        Map<String, League> mapLeague = new HashMap<>();
                        for (MatchInfo matchInfo : data) {
                            Match match = new MatchPm(matchInfo);
                            League league = mapLeague.get(String.valueOf(matchInfo.tid));
                            if (league == null) {
                                LeagueInfo leagueInfo = new LeagueInfo();
                                leagueInfo.picUrlthumb = matchInfo.lurl;
                                leagueInfo.nameText = matchInfo.tn;
                                leagueInfo.tournamentId = Long.parseLong(matchInfo.tid);
                                league = new LeaguePm(leagueInfo);
                                mapLeague.put(String.valueOf(matchInfo.tid), league);
                                leagues.add(league);
                            }
                            league.getMatchList().add(match);

                        }
                        resultLeagueData.setValue(leagues);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        resultErrorLeagueData.setValue("");
                    }

                    @Override
                    public void onFail(BusinessException t) {
                        super.onFail(t);
                        resultErrorLeagueData.setValue("");
                    }
                });
        addSubscribe(disposable);
    }

    @Override
    public String[] getPlayMethodTypes() {
        return PMConstants.PLAY_METHOD_TYPES;
    }

    @Override
    public HashMap<Integer, SportTypeItem> getMatchGames() {
        return PMConstants.getMatchGames();
    }

    /**
     * @param matchInfoList
     * @return
     */
    private void championLeagueList(List<MatchInfo> matchInfoList) {
        if (!matchInfoList.isEmpty()) {
            Match header = new MatchPm();
            header.setHead(true);
            mChampionMatchList.add(header);
            for (MatchInfo matchInfo : matchInfoList) {
                Match match = new MatchPm(matchInfo);
                mChampionMatchList.add(match);
            }
        }
    }

    /**
     * 公告列表集合
     */
    public void getAnnouncement() {
        Disposable disposable = (Disposable) model.getPMApiService().frontListPB()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<FrontListInfo>() {
                    @Override
                    public void onResult(FrontListInfo info) {
                        KLog.i("FrontListInfo     " + info);
                        List<FBAnnouncementInfo.RecordsDTO> list2 = new ArrayList<>();
                        for (FrontListInfo.NbDTO i : info.nb) {
                            FBAnnouncementInfo.RecordsDTO dto = new FBAnnouncementInfo.RecordsDTO();
                            dto.id = i.id;
                            dto.ti = i.noticeTypeName;
                            dto.co = i.context;
                            dto.pt = i.sendTimeOther;
                            list2.add(dto);
                        }
                        announcementData.postValue(list2);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private Flowable getFlowableMatchesPagePB(PMListReq pmListReq) {
        Flowable flowable;
        if (isUseCacheApiService()) {
            if (getSportCacheType().equals(SportCacheType.PM)) {
                pmListReq.setToken(SPUtils.getInstance().getString(SPKeyGlobal.PM_TOKEN));
                flowable = model.getBaseApiService().pmMatchesPagePB(pmListReq);
            } else {
                pmListReq.setToken(SPUtils.getInstance().getString(SPKeyGlobal.PMXC_TOKEN));
                flowable = model.getBaseApiService().pmxcMatchesPagePB(pmListReq);
            }
        } else {
            flowable = model.getPMApiService().matchesPagePB(pmListReq);
        }

        return flowable;
    }

    private Flowable getFlowableLiveMatchesPB(PMListReq pmListReq) {
        Flowable flowable;
        if (isUseCacheApiService()) {
            if (getSportCacheType().equals(SportCacheType.PM)) {
                pmListReq.setToken(SPUtils.getInstance().getString(SPKeyGlobal.PM_TOKEN));
                flowable = model.getBaseApiService().pmLiveMatchesPB(pmListReq);
            } else {
                pmListReq.setToken(SPUtils.getInstance().getString(SPKeyGlobal.PMXC_TOKEN));
                flowable = model.getBaseApiService().pmxcLiveMatchesPB(pmListReq);
            }
        } else {
            flowable = model.getPMApiService().liveMatchesPB(pmListReq);
        }

        return flowable;
    }

    private Flowable getFlowableNoLiveMatchesPagePB(PMListReq pmListReq) {
        Flowable flowable;
        if (isUseCacheApiService()) {
            if (getSportCacheType().equals(SportCacheType.PM)) {
                pmListReq.setToken(SPUtils.getInstance().getString(SPKeyGlobal.PM_TOKEN));
                flowable = model.getBaseApiService().pmNoLiveMatchesPagePB(pmListReq);
            } else {
                pmListReq.setToken(SPUtils.getInstance().getString(SPKeyGlobal.PMXC_TOKEN));
                flowable = model.getBaseApiService().pmxcNoLiveMatchesPagePB(pmListReq);
            }
        } else {
            flowable = model.getPMApiService().noLiveMatchesPagePB(pmListReq);
        }

        return flowable;
    }

    private Flowable getFlowableMatchBaseInfoByMidsPB(PMListReq pmListReq) {
        Flowable flowable;
        if (isUseCacheApiService()) {
            if (getSportCacheType().equals(SportCacheType.PM)) {
                pmListReq.setToken(SPUtils.getInstance().getString(SPKeyGlobal.PM_TOKEN));
                flowable = model.getBaseApiService().pmGetMatchBaseInfoByMidsPB(pmListReq);
            } else {
                pmListReq.setToken(SPUtils.getInstance().getString(SPKeyGlobal.PMXC_TOKEN));
                flowable = model.getBaseApiService().pmxcGetMatchBaseInfoByMidsPB(pmListReq);
            }
        } else {
            flowable = model.getPMApiService().getMatchBaseInfoByMidsPB(pmListReq);
        }

        return flowable;
    }

    private boolean isUseCacheApiService() {
        SportCacheType sportCacheType = getSportCacheType();
        if (sportCacheType.equals(SportCacheType.PM) || sportCacheType.equals(SportCacheType.PMXC)) {
            return true;
        } else {
            return false;
        }
    }

    //根据返回的类型判断是走缓存请求，还是直连三方请求
    private SportCacheType getSportCacheType() {
        // 获取平台和缓存数据
        String platform = SPUtils.getInstance().getString("KEY_PLATFORM", "");
        String json = SPUtils.getInstance().getString(SPKeyGlobal.SPORT_MATCH_CACHE, "");

        // 如果平台或数据为空，直接返回 NONE
        if (TextUtils.isEmpty(platform) || TextUtils.isEmpty(json)) {
            return SportCacheType.NONE;
        }

        // 解析缓存数据
        Type typeToken = new TypeToken<SportsCacheSwitchInfo>() {
        }.getType();
        SportsCacheSwitchInfo sportCacheSwitchInfo = new Gson().fromJson(json, typeToken);

        // 如果解析结果为空，返回 NONE
        if (sportCacheSwitchInfo == null) {
            return SportCacheType.NONE;
        }

        // 获取用户 ID 和对应的 sportCacheList
        String userID = SPUtils.getInstance().getString(SPKeyGlobal.USER_ID);
        List<Integer> sportCacheList = getSportCacheListByPlatform(platform, sportCacheSwitchInfo);

        // 如果用户列表为空，表示面向全部用户，进行相关检查
        if (sportCacheSwitchInfo.getUsers().isEmpty()) {
            //场馆数据为空
            if (sportCacheList.isEmpty()) {
                return SportCacheType.NONE;
            }
        } else {
            // 如果用户列表不为空，检查当前用户是否在用户列表内
            if (!sportCacheSwitchInfo.getUsers().contains(userID)) {
                return SportCacheType.NONE;
            }
        }

        // 最终检查缓存数据并返回 SportCacheType
        return getSportCacheTypeForPlatform(platform, sportCacheList);
    }

    // 根据平台获取对应的 sportCacheList
    private List<Integer> getSportCacheListByPlatform(String platform, SportsCacheSwitchInfo sportCacheSwitchInfo) {
        if (TextUtils.equals(platform, PLATFORM_PM)) {
            return sportCacheSwitchInfo.getObg();
        } else {
            return sportCacheSwitchInfo.getObgzy();
        }
    }

    // 根据平台返回相应的 SportCacheType
    private SportCacheType getSportCacheTypeForPlatform(String platform, List<Integer> sportCacheList) {
        if (sportCacheList.contains(9)) {
            // 如果缓存数据包含 9，根据平台返回对应的 SportCacheType
            if (TextUtils.equals(platform, PLATFORM_PM)) {
                return SportCacheType.PM;
            } else {
                return SportCacheType.PMXC;
            }
        }
        return SportCacheType.NONE;
    }

    // 根据条件设置 HttpCallBack 类型的对象
    public void createPMListCallback(boolean isTimerRefresh, boolean isRefresh, int sportPos, String sportId, int orderBy, List<Long> leagueIds, int searchDatePos, int oddType, List<Long> matchidList, Flowable flowable) {
        // 根据是否使用缓存选择回调类型
        Object callback = isUseCacheApiService()
                ? new PMListCacheCallBack(this, mHasCache, isTimerRefresh, isRefresh, mPlayMethodType,
                sportPos, sportId, orderBy, leagueIds, searchDatePos, oddType, matchidList)
                : new PMListCallBack(this, mHasCache, isTimerRefresh, isRefresh, mPlayMethodType,
                sportPos, sportId, orderBy, leagueIds, searchDatePos, oddType, matchidList);

        // 统一处理 Flowable 的线程调度和异常处理
        Disposable disposable = createDisposable(flowable, callback);
        // 添加订阅管理
        addSubscribe(disposable);
    }

    // 根据条件返回 HttpCallBack 类型的对象
    public void createPMLeagueListCallback(boolean isTimerRefresh, boolean isRefresh,
                                           int sportPos, String sportId, int orderBy, List<Long> leagueIds,
                                           int searchDatePos, int oddType, List<Long> matchidList, int finalType, boolean isStepSecond, Flowable flowable) {
        mPmLeagueCallBack = new PMLeagueListCallBack(this, mHasCache, isTimerRefresh, isRefresh, mCurrentPage,
                mPlayMethodType, sportPos, sportId, orderBy, leagueIds, searchDatePos, oddType,
                matchidList, finalType, isStepSecond);
        mPmCacheLeagueCallBack = new PMLeagueListCacheCallBack(this, mHasCache, isTimerRefresh, isRefresh, mCurrentPage,
                mPlayMethodType, sportPos, sportId, orderBy, leagueIds, searchDatePos, oddType,
                matchidList, finalType, isStepSecond);
        // 根据是否使用缓存选择回调
        Object callback = isUseCacheApiService()
                ? mPmCacheLeagueCallBack
                : mPmLeagueCallBack;
        // 统一处理 Flowable 的线程调度和异常处理
        Disposable disposable = createDisposable(flowable, callback);
        // 添加订阅管理
        addSubscribe(disposable);
    }

    private Disposable createDisposable(Flowable flowable, Object callBack) {
        return (Disposable) flowable.compose(RxUtils.schedulersTransformer()) // 线程调度
                .compose(RxUtils.exceptionTransformer()) // 异常处理
                .subscribeWith((Subscriber) callBack); // 订阅并返回 Disposable
    }
}
