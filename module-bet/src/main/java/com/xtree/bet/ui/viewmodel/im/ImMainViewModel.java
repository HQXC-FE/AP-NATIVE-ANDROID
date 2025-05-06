package com.xtree.bet.ui.viewmodel.im;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.xtree.base.net.HttpCallBack;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.TimeUtils;
import com.xtree.bet.bean.request.im.AllSportCountReq;
import com.xtree.bet.bean.request.im.AnnouncementReq;
import com.xtree.bet.bean.request.im.EventInfoMbtReq;
import com.xtree.bet.bean.request.pm.BtReq;
import com.xtree.bet.bean.request.pm.PMListReq;
import com.xtree.bet.bean.response.fb.FBAnnouncementInfo;
import com.xtree.bet.bean.response.im.SportCountRsp;
import com.xtree.bet.bean.response.pm.FrontListInfo;
import com.xtree.bet.bean.response.pm.LeagueInfo;
import com.xtree.bet.bean.response.pm.MatchInfo;
import com.xtree.bet.bean.response.pm.MenuInfo;
import com.xtree.bet.bean.response.pm.PMResultBean;
import com.xtree.bet.bean.ui.League;
import com.xtree.bet.bean.ui.LeaguePm;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.MatchPm;
import com.xtree.bet.constant.IMConstants;
import com.xtree.bet.constant.PMConstants;
import com.xtree.bet.constant.SportTypeItem;
import com.xtree.bet.data.BetRepository;
import com.xtree.bet.ui.viewmodel.MainViewModel;
import com.xtree.bet.ui.viewmodel.SportCacheType;
import com.xtree.bet.ui.viewmodel.TemplateMainViewModel;
import com.xtree.bet.ui.viewmodel.callback.IMLeagueListCacheCallBack;
import com.xtree.bet.ui.viewmodel.callback.IMLeagueListCallBack;
import com.xtree.bet.ui.viewmodel.callback.IMListCacheCallBack;
import com.xtree.bet.ui.viewmodel.callback.IMListCallBack;

import org.reactivestreams.Subscriber;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.http.BusinessException;
import me.xtree.mvvmhabit.utils.KLog;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;

/**
 * Created by vickers
 */

public class ImMainViewModel extends TemplateMainViewModel implements MainViewModel {
    private List<Match> mMatchList = new ArrayList<>();
    public List<Match> mChampionMatchList = new ArrayList<>();
    private Map<String, Match> mMapMatch = new HashMap<>();
    private Map<String, League> mMapLeague = new HashMap<>();
    public List<MatchInfo> mChampionMatchInfoList = new ArrayList<>();
    private Map<String, List<SportTypeItem>> sportCountMap = new HashMap<>();
    private List<MenuInfo> mMenuInfoList = new ArrayList<>();
    private IMLeagueListCallBack mImLeagueCallBack;
    private IMLeagueListCacheCallBack mPmCacheLeagueCallBack; //后台的缓存接口回调

    private HashMap<Integer, SportTypeItem> mMatchGames = new HashMap<>();

    private int mGoingOnPageSize = 300;
    private int mPageSize = 20;
    /**
     *
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

    public ImMainViewModel(@NonNull Application application, BetRepository repository) {
        super(application, repository);
        sportItemData.postValue(new String[]{});
    }

    public void saveLeague(IMListCallBack imListCallBack) {
        mLeagueList = imListCallBack.getLeagueList();
        mGoingOnLeagueList = imListCallBack.getGoingOnLeagueList();
        mMapLeague = imListCallBack.getMapLeague();
        mMatchList = imListCallBack.getMatchList();
        mMapMatch = imListCallBack.getMapMatch();
        mMapSportType = imListCallBack.getMapSportType();
    }

    public void saveLeague(IMLeagueListCallBack imListCallBack) {
        mLeagueList = imListCallBack.getLeagueList();
        mGoingOnLeagueList = imListCallBack.getGoingOnLeagueList();
        mMapLeague = imListCallBack.getMapLeague();
        mMatchList = imListCallBack.getMatchList();
        mMapMatch = imListCallBack.getMapMatch();
        mMapSportType = imListCallBack.getMapSportType();
        mNoLiveheaderLeague = imListCallBack.getNoLiveheaderLeague();
    }

    public void saveLeague(IMListCacheCallBack imListCallBack) {
        mLeagueList = imListCallBack.getLeagueList();
        mGoingOnLeagueList = imListCallBack.getGoingOnLeagueList();
        mMapLeague = imListCallBack.getMapLeague();
        mMatchList = imListCallBack.getMatchList();
        mMapMatch = imListCallBack.getMapMatch();
        mMapSportType = imListCallBack.getMapSportType();
    }

    public void saveLeague(IMLeagueListCacheCallBack imListCallBack) {
        mLeagueList = imListCallBack.getLeagueList();
        mGoingOnLeagueList = imListCallBack.getGoingOnLeagueList();
        mMapLeague = imListCallBack.getMapLeague();
        mMatchList = imListCallBack.getMatchList();
        mMapMatch = imListCallBack.getMapMatch();
        mMapSportType = imListCallBack.getMapSportType();
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
//        Object callback = isUseCacheApiService()
//                ? new PMHotMatchCountCacheCallBack(this)
//                : new PMHotMatchCountCallBack(this);

        // 1.创建 Disposable，2.并进行订阅
//        Disposable disposable = createDisposable(flowable, callback);
//        addSubscribe(disposable);
    }

    @Override
    public void searchMatch(String searchWord, boolean isChampion) {
        mSearchWord = searchWord;
        mIsChampion = isChampion;
        if (!isChampion) {
            if (mImLeagueCallBack != null) {
                mImLeagueCallBack.searchMatch(searchWord);
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

        if (mCurrentPage == 1 && !isTimerRefresh && !isStepSecond) {
            showCache(sportId, mPlayMethodType, searchDatePos);
        }

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

//        EventInfoMbtReq eventInfoMbtReq = new EventInfoMbtReq();
//        eventInfoMbtReq.setSportId(1);
//        eventInfoMbtReq.setMarket(3);
//        eventInfoMbtReq.setMatchDay(0);
//        eventInfoMbtReq.setOddsType(3);
//        eventInfoMbtReq.setPage(1);
//        eventInfoMbtReq.setSeason(0);
//        eventInfoMbtReq.setCombo(false);
//        CfLog.d("==== ImMainViewModel getLeagueList ====");
//        getFlowableLiveMatches(eventInfoMbtReq);

        Flowable flowable = getFlowableMatchesPagePB(pmListReq);
        if (isStepSecond) {
            flowable = getFlowableNoLiveMatchesPagePB(pmListReq);
        }
        CfLog.d("==== ImMainViewModel getLeagueList ====");
//        if (type == 1) {// 滚球
//            if (needSecondStep) {
//                EventInfoMbtReq eventInfoMbtReq = new EventInfoMbtReq();
//                eventInfoMbtReq.setSportId(1);
//                eventInfoMbtReq.setMarket(3);
//                eventInfoMbtReq.setMatchDay(0);
//                eventInfoMbtReq.setOddsType(3);
//                eventInfoMbtReq.setPage(1);
//                eventInfoMbtReq.setSeason(0);
//                eventInfoMbtReq.setCombo(false);
//                CfLog.d("==== ImMainViewModel getLeagueList ====");
//                flowable = getFlowableLiveMatches(eventInfoMbtReq);
//            }
//        }

        if (isTimerRefresh) {
            flowable = getFlowableMatchBaseInfoByMidsPB(pmListReq);
        }

        if (isRefresh) {
            mNoLiveheaderLeague = null;
        }

        if ((type == 1 && needSecondStep) // 获取今日中的全部滚球赛事列表
                || isTimerRefresh) { // 定时刷新赔率变更
            //createPMListCallback(isTimerRefresh, isRefresh, sportPos, sportId, orderBy, leagueIds, searchDatePos, oddType, matchidList, flowable);
        } else {
            //createPMLeagueListCallback(isTimerRefresh, isRefresh, sportPos, sportId, orderBy, leagueIds, searchDatePos, oddType, matchidList, finalType, isStepSecond, flowable);
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

//        Flowable flowable = getFlowableNoLiveMatchesPagePB(pmListReq);
//        Object callback = isUseCacheApiService()
//                ? new PMChampionListCacheCallBack(this, sportPos, sportId, orderBy, leagueIds, matchids, playMethodType, oddType, isTimerRefresh, isRefresh, mCurrentPage)
//                : new PMChampionListCallBack(this, sportPos, sportId, orderBy, leagueIds, matchids, playMethodType, oddType, isTimerRefresh, isRefresh, mCurrentPage);
        // 1.创建 Disposable，2.并进行订阅
//        Disposable disposable = createDisposable(flowable, callback);
//        addSubscribe(disposable);
    }

    /**
     * 获取赛事统计数据
     */
    public void statistical(int playMethodType) {
        getFlowableLiveMatches();
        AllSportCountReq allSportCountReq = new AllSportCountReq();
        Disposable disposable = (Disposable) model.getIMApiService().getAllSportCount(allSportCountReq)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<SportCountRsp>() {
                    @Override
                    public void onResult(SportCountRsp sportCountRsp) {
                        List<SportCountRsp.CountItem> sportList = sportCountRsp.getSportCount();
                        mMenuInfoList.clear();
                        for (SportCountRsp.CountItem item : sportList) {
                            System.out.println("SportId: " + item.sportId);
                            for (String name : PLAY_METHOD_NAMES) {
                                MenuInfo menuInfo = new MenuInfo();
                                menuInfo.menuId = item.sportId;
                                menuInfo.menuName = item.sportName;
                                if(name.equals("今日")){
                                    menuInfo.menuType = 3;
                                    if(!item.eventGroupTypes.isEmpty()){
                                        menuInfo.count = item.eventGroupTypes.get(0).todayFECount+item.eventGroupTypes.get(0).rbFECount;
                                    }
                                }else if (name.equals("滚球")){
                                    menuInfo.menuType = 1;
                                    if(!item.eventGroupTypes.isEmpty()){
                                        menuInfo.count = item.eventGroupTypes.get(0).rbFECount;
                                    }
                                }else if (name.equals("早盘")) {
                                    menuInfo.menuType = 4;
                                    if(!item.eventGroupTypes.isEmpty()){
                                        menuInfo.count = item.eventGroupTypes.get(0).earlyFECount;
                                    }
                                }else if (name.equals("串关")) {
                                    menuInfo.menuType = 11;
                                    if(!item.eventGroupTypes.isEmpty()) {
                                        menuInfo.count = item.eventGroupTypes.get(0).count;
                                    }
                                }else if (name.equals("冠军")) {
                                    menuInfo.menuType = 100;
                                    if(!item.eventGroupTypes.isEmpty()) {
                                        menuInfo.count = item.eventGroupTypes.get(0).orCount;
                                    }
                                }
                                mMenuInfoList.add(menuInfo);
                            }
                        }

                        if (mMatchGames.isEmpty()) {
                            mMatchGames = IMConstants.getMatchGames();
                        }
                        for (MenuInfo menuInfo : mMenuInfoList) {
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
        EventInfoMbtReq eventInfoMbtReq = new EventInfoMbtReq();
        eventInfoMbtReq.setSportId(1);
        eventInfoMbtReq.setMarket(3);
        eventInfoMbtReq.setMatchDay(0);
        eventInfoMbtReq.setOddsType(3);
        eventInfoMbtReq.setPage(1);
        eventInfoMbtReq.setSeason(0);
        eventInfoMbtReq.setCombo(false);
        CfLog.d("==== ImMainViewModel getLeagueList ====");
        getFlowableLiveMatches();
        Disposable disposable = (Disposable) model.getIMApiService().getEventInfoMbt(eventInfoMbtReq)
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
        return IMConstants.PLAY_METHOD_TYPES;
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
        AnnouncementReq  announcementReq = new AnnouncementReq();
//        Disposable disposable = (Disposable) model.getIMApiService().getAnnouncement(announcementReq)
//                .compose(RxUtils.schedulersTransformer()) //线程调度
//                .compose(RxUtils.exceptionTransformer())
//                .subscribeWith(new HttpCallBack<FrontListInfo>() {
//                    @Override
//                    public void onResult(FrontListInfo info) {
//                        KLog.i("FrontListInfo     " + info);
//                        List<FBAnnouncementInfo.RecordsDTO> list2 = new ArrayList<>();
//                        for (FrontListInfo.NbDTO i : info.nb) {
//                            FBAnnouncementInfo.RecordsDTO dto = new FBAnnouncementInfo.RecordsDTO();
//                            dto.id = i.id;
//                            dto.ti = i.noticeTypeName;
//                            dto.co = i.context;
//                            dto.pt = i.sendTimeOther;
//                            list2.add(dto);
//                        }
//                        announcementData.postValue(list2);
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//                        super.onError(t);
//                    }
//                });
//        addSubscribe(disposable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private Flowable getFlowableMatchesPagePB(PMListReq pmListReq) {
        Flowable flowable = model.getPMApiService().matchesPagePB(pmListReq);
        return flowable;
    }

    public Flowable getFlowableLiveMatches() {
        EventInfoMbtReq eventInfoMbtReq = new EventInfoMbtReq();
        eventInfoMbtReq.setSportId(1);
        eventInfoMbtReq.setMarket(3);
        eventInfoMbtReq.setMatchDay(0);
        eventInfoMbtReq.setOddsType(3);
        eventInfoMbtReq.setPage(1);
        eventInfoMbtReq.setSeason(0);
        eventInfoMbtReq.setCombo(false);
        CfLog.d("==== ImMainViewModel getLeagueList ====");
        Flowable flowable = model.getIMApiService().getEventInfoMbt(eventInfoMbtReq);
        return flowable;
    }

    private Flowable getFlowableNoLiveMatchesPagePB(PMListReq pmListReq) {
        Flowable flowable = model.getPMApiService().noLiveMatchesPagePB(pmListReq);
        return flowable;
    }

    private Flowable getFlowableMatchBaseInfoByMidsPB(PMListReq pmListReq) {
        Flowable flowable = model.getPMApiService().getMatchBaseInfoByMidsPB(pmListReq);
        return flowable;
    }


}
