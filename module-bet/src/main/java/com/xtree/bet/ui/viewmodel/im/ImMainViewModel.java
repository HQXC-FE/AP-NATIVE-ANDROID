package com.xtree.bet.ui.viewmodel.im;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.TimeUtils;
import com.xtree.bet.EventInfoByPageListParser;
import com.xtree.bet.bean.request.im.AllSportCountReq;
import com.xtree.bet.bean.request.im.AnnouncementReq;
import com.xtree.bet.bean.request.im.EventInfoByPageRsq;
import com.xtree.bet.bean.request.im.OutrightEventsReq;
import com.xtree.bet.bean.request.pm.PMListReq;
import com.xtree.bet.bean.response.fb.FBAnnouncementInfo;
import com.xtree.bet.bean.response.im.Announcement;
import com.xtree.bet.bean.response.im.ChampionEventsRsp;
import com.xtree.bet.bean.response.im.EventInfoByPageListRsp;
import com.xtree.bet.bean.response.im.GetAnnouncementRsp;
import com.xtree.bet.bean.response.im.LeagueInfo;
import com.xtree.bet.bean.response.im.MatchInfo;
import com.xtree.bet.bean.response.im.MenuInfo;
import com.xtree.bet.bean.response.im.Sport;
import com.xtree.bet.bean.response.im.SportCountRsp;
import com.xtree.bet.bean.ui.League;
import com.xtree.bet.bean.ui.LeagueIm;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.MatchIm;
import com.xtree.bet.constant.IMConstants;
import com.xtree.bet.constant.SportTypeItem;
import com.xtree.bet.data.BetRepository;
import com.xtree.bet.ui.activity.MainActivity;
import com.xtree.bet.ui.viewmodel.MainViewModel;
import com.xtree.bet.ui.viewmodel.SportCacheType;
import com.xtree.bet.ui.viewmodel.TemplateMainViewModel;
import com.xtree.bet.ui.viewmodel.callback.IMChampionListCallBack;
import com.xtree.bet.ui.viewmodel.callback.IMLeagueListCallBack;
import com.xtree.bet.ui.viewmodel.callback.IMListCallBack;
import com.xtree.bet.ui.viewmodel.callback.PMChampionListCacheCallBack;
import com.xtree.bet.ui.viewmodel.callback.PMChampionListCallBack;

import org.reactivestreams.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    private HashMap<Integer, SportTypeItem> mMatchGames = new HashMap<>();

    private int mGoingOnPageSize = 300;
    private int mPageSize = 20;
    /**
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
//        PMListReq pmListReq = new PMListReq();
//        pmListReq.setCuid();
//        pmListReq.setCpn(mCurrentPage);
//        pmListReq.setCps(mGoingOnPageSize);
//
//        String sportIds = "";
//        if (mMenuInfoList.isEmpty()) {
//            hotEmptyMatchCountData.postValue(0);
//            return;
//        } else {
//            for (MenuInfo menuInfo : mMenuInfoList) {
//                if (playMethodType == menuInfo.menuType) {
//                    for (MenuInfo subMenu : menuInfo.subList) {
//                        sportIds += subMenu.menuId + ",";
//                    }
//                }
//            }
//        }
//        pmListReq.setEuid(sportIds);
//        if (leagueIds != null && !leagueIds.isEmpty()) {
//            String leagueids = "";
//            for (Long leagueid : leagueIds) {
//                leagueids += leagueid + ",";
//            }
//            pmListReq.setTid(leagueids.substring(0, leagueids.length() - 1));
//        }
//
//        pmListReq.setType(3);
        // 获取 Flowable 对象

        EventInfoByPageRsq eventInfoByPageRsq = new EventInfoByPageRsq();
        eventInfoByPageRsq.setSportId(1);
        eventInfoByPageRsq.setMarket("2");
        eventInfoByPageRsq.setMatchDay(0);
        eventInfoByPageRsq.setOddsType(3);
        eventInfoByPageRsq.setPage(1);
        eventInfoByPageRsq.setSeason(0);
        eventInfoByPageRsq.setIsCombo(false);
        model.getIMApiService().getEventInfoByPage(eventInfoByPageRsq);

//        EventInfoMbtReq eventInfoMbtReq = new EventInfoMbtReq();
//        eventInfoMbtReq.setSportId(1);
//        eventInfoMbtReq.setMarket(3);
//        eventInfoMbtReq.setMatchDay(0);
//        eventInfoMbtReq.setOddsType(3);
//        eventInfoMbtReq.setPage(1);
//        eventInfoMbtReq.setSeason(0);
//        eventInfoMbtReq.setCombo(false);
//        CfLog.d("==== ImMainViewModel getLeagueList ====");
//        model.getIMApiService().getEventInfoMbt(eventInfoMbtReq);

    }

    @Override
    public void searchMatch(String searchWord, boolean isChampion) {
        mSearchWord = searchWord;
        mIsChampion = isChampion;
        if (!isChampion) {
            if (mImLeagueCallBack != null) {
                mImLeagueCallBack.searchMatch(searchWord);
            }
        } else {
            mChampionMatchList.clear();
            if (!TextUtils.isEmpty(searchWord)) {
                List<MatchInfo> matchInfoList = new ArrayList<>();
                for (MatchInfo matchInfo : mChampionMatchInfoList) {
                    MatchIm matchIm = new MatchIm(matchInfo);
                    if (matchIm.getLeague().getLeagueName().contains(searchWord)) {
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
            //CfLog.d("============ ImMainViewModel getLeagueList mPlayMethodType =============="+mPlayMethodType);
            //CfLog.d("============ ImMainViewModel getLeagueList searchDatePos =============="+searchDatePos);
            //showCache(sportId, mPlayMethodType, searchDatePos);
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
        //pmListReq.setDevice("v2_h5_st");

        if (!dateList.isEmpty()) {
            if (searchDatePos == dateList.size() - 1) {
                String time = TimeUtils.parseTime(dateList.get(searchDatePos), TimeUtils.FORMAT_YY_MM_DD) + " 12:00:00";
                pmListReq.setMd(String.valueOf(0 - TimeUtils.strFormatDate(time, TimeUtils.FORMAT_YY_MM_DD_HH_MM_SS).getTime()));
            } else if (searchDatePos > 0) {
                String time = TimeUtils.parseTime(dateList.get(searchDatePos), TimeUtils.FORMAT_YY_MM_DD) + " 12:00:00";
                pmListReq.setMd(String.valueOf(TimeUtils.strFormatDate(time, TimeUtils.FORMAT_YY_MM_DD_HH_MM_SS).getTime()));
            }
        }

        Flowable flowable = getFlowableMatchesPage(pmListReq);
        if (isStepSecond) {
            flowable = getFlowableNoLiveMatchesPage(pmListReq);
        }
        pmListReq.setCps(mPageSize);
        if (type == 1) {// 滚球
            if (needSecondStep) {
                pmListReq.setCps(mGoingOnPageSize);
                flowable = getFlowableLiveMatches(pmListReq);
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
            createIMListCallback(isTimerRefresh, isRefresh, sportPos, sportId, orderBy, leagueIds, searchDatePos, oddType, matchidList, flowable);
        } else {
            createIMLeagueListCallback(isTimerRefresh, isRefresh, sportPos, sportId, orderBy, leagueIds, searchDatePos, oddType, matchidList, finalType, isStepSecond, flowable);
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

        OutrightEventsReq outrightEventsReq = new OutrightEventsReq();
        outrightEventsReq.setIsCombo(false);
        outrightEventsReq.setMatchDay(0);
        outrightEventsReq.setOddsType("3");
        outrightEventsReq.setOrderBy(2);
        outrightEventsReq.setPage(0);
        outrightEventsReq.setSeason(0);
        outrightEventsReq.setSportId(1);

        Flowable flowable = getOutrightEvents(outrightEventsReq);
        Object callback = new IMChampionListCallBack(this, sportPos, sportId, orderBy, leagueIds, matchids, playMethodType, oddType, isTimerRefresh, isRefresh, mCurrentPage);
        // 1.创建 Disposable，2.并进行订阅
        Disposable disposable = createDisposable(flowable, callback);
        addSubscribe(disposable);
    }

    /**
     * 获取赛事统计数据
     */
    public void statistical(int playMethodType) {
        AllSportCountReq allSportCountReq = new AllSportCountReq();
        Disposable disposable = (Disposable) model.getIMApiService().getAllSportCount(allSportCountReq).compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()).subscribeWith(new HttpCallBack<SportCountRsp>() {
                    @Override
                    public void onResult(SportCountRsp sportCountRsp) {
                        mMenuInfoList.clear();
                        processSportCount(sportCountRsp);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.d("====== ImMainViewModel statistical onError =====");
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
//        Disposable disposable = (Disposable) model.getIMApiService().resultMenuPB(map).compose(RxUtils.schedulersTransformer()) //线程调度
//                .compose(RxUtils.exceptionTransformer()).subscribeWith(new HttpCallBack<List<PMResultBean>>() {
//                    @Override
//                    public void onResult(List<PMResultBean> list) {
//                        List<SportTypeItem> list1 = new ArrayList<>();
//                        for (PMResultBean i : list) {
//                            if (IMConstants.getMatchGames().get(i.getMenuType()) == null || i.getMenuType() == 3001 || i.getMenuType() == 3002 || i.getMenuType() == 3003) {
//                                //体育赛果隐藏英雄联盟、DOTA2、王者荣耀
//                                continue;
//                            }
//                            SportTypeItem item = new SportTypeItem();
//                            item.id = i.getMenuType();
//                            item.menuId = Integer.parseInt(i.getMenuId());
//                            list1.add(item);
//                        }
//                        ConcurrentHashMap<String, List<SportTypeItem>> sportMap = new ConcurrentHashMap<>();
//                        sportMap.put("1", list1);
//                        sportMap.put("2", new ArrayList<>());
//                        resultData.setValue(sportMap);
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//                        super.onError(t);
//
//                    }
//                });
//        addSubscribe(disposable);
    }

    /**
     * 获取赛果信息赛事列表
     */
    public void matchResultPage(String beginTime, String endTime, int playMethodPos, String sportId) {
        EventInfoByPageRsq eventInfoByPageRsq = new EventInfoByPageRsq();
        eventInfoByPageRsq.setSportId(1);
        eventInfoByPageRsq.setMarket("2");
        eventInfoByPageRsq.setMatchDay(0);
        eventInfoByPageRsq.setOddsType(3);
        eventInfoByPageRsq.setPage(1);
        eventInfoByPageRsq.setSeason(0);
        eventInfoByPageRsq.setIsCombo(false);
        Disposable disposable = (Disposable) model.getIMApiService().getEventInfoByPage(eventInfoByPageRsq).compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()).subscribeWith(new HttpCallBack<EventInfoByPageListRsp>() {
                    @Override
                    public void onResult(EventInfoByPageListRsp data) {
                        CfLog.d("================== ImMainViewModel matchResultPage onResult ====");
                        ArrayList<League> leagues = new ArrayList<>();
                        Map<String, League> mapLeague = new HashMap<>();
                        data = EventInfoByPageListParser.getEventInfoByPageListRsp(MainActivity.getContext());
                        List<MatchInfo> matchInfoList = data.getSports().get(0).getEvents();
                        for (MatchInfo matchInfo : matchInfoList) {
                            matchInfo.setSportId(data.getSports().get(0).getSportId());
                            matchInfo.setSportName(data.getSports().get(0).getSportName());
                            CfLog.d("================= IMLeagueListCallBack onResult matchInfo =================="+matchInfo);
                        }
                        List<Sport> matches =  data.getSports();
                        if (matches != null) {
                            matchInfoList = data.getSports().get(0).getEvents();
                        }

                        for (MatchInfo matchInfo : matchInfoList) {
                            Match match = new MatchIm(matchInfo);
                            League league = mapLeague.get(String.valueOf(matchInfo.competition.getCompetitionId()));
                            if (league == null) {
                                LeagueInfo leagueInfo = new LeagueInfo();
                                //leagueInfo.picUrlthumb = matchInfo.lurl; //暂时没有联赛图标,需要通过FTP获取
                                leagueInfo.nameText = matchInfo.competition.getCompetitionName();
                                leagueInfo.tournamentId = matchInfo.competition.getCompetitionId();
                                league = new LeagueIm(leagueInfo);
                                mapLeague.put(String.valueOf(matchInfo.competition.getCompetitionId()), league);
                                leagues.add(league);
                            }
                            league.getMatchList().add(match);

                        }
                        CfLog.d("=============  ImMainViewModel matchResultPage onResult leagues ==============="+leagues.toString());
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
        return IMConstants.getMatchGames();
    }

    /**
     * @param matchInfoList
     * @return
     */
    private void championLeagueList(List<MatchInfo> matchInfoList) {
        if (!matchInfoList.isEmpty()) {
            Match header = new MatchIm();
            header.setHead(true);
            mChampionMatchList.add(header);
            for (MatchInfo matchInfo : matchInfoList) {
                Match match = new MatchIm(matchInfo);
                mChampionMatchList.add(match);
            }
        }
    }

    /**
     * 公告列表集合
     */
    public void getAnnouncement() {
        AnnouncementReq announcementReq = new AnnouncementReq();
        Disposable disposable = (Disposable) model.getIMApiService().getAnnouncement(announcementReq).compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()).subscribeWith(new HttpCallBack<GetAnnouncementRsp>() {
                    @Override
                    public void onResult(GetAnnouncementRsp info) {
                        KLog.i("GetAnnouncementRsp     " + info);
                        List<FBAnnouncementInfo.RecordsDTO> list2 = new ArrayList<>();
                        for (Announcement i : info.getAnnouncement()) {
                            FBAnnouncementInfo.RecordsDTO dto = new FBAnnouncementInfo.RecordsDTO();
                            dto.id = String.valueOf(i.announcementId);
                            dto.ti = "";
                            dto.co = i.announcementDetail.get(0).content;
                            dto.pt = i.postingDate;
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

    private void processSportCount(SportCountRsp sportCountRsp) {
        List<SportCountRsp.CountItem> sportList = sportCountRsp.getSportCount();
        for (String name : PLAY_METHOD_NAMES) {
            if (name.equals("今日")) {
                MenuInfo menuInfo = new MenuInfo();
                menuInfo.menuId = 3;
                menuInfo.menuName = "今日";
                menuInfo.menuType = 3;
                menuInfo.subList = generateMenuInfoListFromSportList(sportList);
                mMenuInfoList.add(menuInfo);
            } else if (name.equals("滚球")) {
                MenuInfo menuInfo = new MenuInfo();
                menuInfo.menuId = 1;
                menuInfo.menuName = "滚球";
                menuInfo.menuType = 1;
                menuInfo.subList = generateMenuInfoListFromSportList(sportList);
                mMenuInfoList.add(menuInfo);
            } else if (name.equals("早盘")) {
                MenuInfo menuInfo = new MenuInfo();
                menuInfo.menuId = 4;
                menuInfo.menuName = "早盘";
                menuInfo.menuType = 4;
                menuInfo.subList = generateMenuInfoListFromSportList(sportList);
                mMenuInfoList.add(menuInfo);
            } else if (name.equals("串关")) {
                MenuInfo menuInfo = new MenuInfo();
                menuInfo.menuId = 11;
                menuInfo.menuName = "串关";
                menuInfo.menuType = 11;
                menuInfo.subList = generateMenuInfoListFromSportList(sportList);
                mMenuInfoList.add(menuInfo);
            } else if (name.equals("冠军")) {
                MenuInfo menuInfo = new MenuInfo();
                menuInfo.menuId = 100;
                menuInfo.menuName = "冠军";
                menuInfo.menuType = 100;
                menuInfo.subList = generateMenuInfoListFromSportList(sportList);
                mMenuInfoList.add(menuInfo);
            }
        }

        if (mMatchGames.isEmpty()) {
            mMatchGames = IMConstants.getMatchGames();
        }

        for (MenuInfo menuInfo : mMenuInfoList) {
            //"3", "1", "4", "11", "100"; 只有"今日", "滚球", "早盘", "串关", "冠军"数据才添加，提升效率
            if (menuInfo.menuType == 3 || menuInfo.menuType == 1 || menuInfo.menuType == 4 || menuInfo.menuType == 11 || menuInfo.menuType == 100) {
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

    /**
     * 将 SportCountRsp.CountItem 列表转换为 MenuInfo 列表
     *
     * @param sportList 原始的体育项列表
     * @return MenuInfo 列表
     */
    public List<MenuInfo> generateMenuInfoListFromSportList(List<SportCountRsp.CountItem> sportList) {
        List<MenuInfo> menuInfoList = new ArrayList<>();
        if (sportList == null || sportList.isEmpty()) return menuInfoList;

        for (SportCountRsp.CountItem item : sportList) {
            MenuInfo menuInfo = new MenuInfo();
            menuInfo.menuId = item.sportId;
            menuInfo.menuType = item.sportId;
            menuInfo.menuName = item.sportName;
            menuInfo.count = item.eventGroupTypes.get(0).todayFECount;
            menuInfoList.add(menuInfo);
        }

        return menuInfoList;
    }

    private Flowable getFlowableMatchesPage(PMListReq pmListReq) {
        EventInfoByPageRsq eventInfoByPageRsq = new EventInfoByPageRsq();
        eventInfoByPageRsq.setSportId(1);
        eventInfoByPageRsq.setMarket("2");
        eventInfoByPageRsq.setMatchDay(0);
        eventInfoByPageRsq.setOddsType(3);
        eventInfoByPageRsq.setPage(1);
        eventInfoByPageRsq.setSeason(0);
        eventInfoByPageRsq.setIsCombo(false);
        Flowable flowable = model.getIMApiService().getEventInfoByPage(eventInfoByPageRsq);
        return flowable;
    }

    private Flowable getFlowableLiveMatches(PMListReq pmListReq) {
            //flowable = model.getIMApiService().liveMatchesPB(pmListReq);
        EventInfoByPageRsq eventInfoByPageRsq = new EventInfoByPageRsq();
        eventInfoByPageRsq.setSportId(1);
        eventInfoByPageRsq.setMarket("2");
        eventInfoByPageRsq.setMatchDay(0);
        eventInfoByPageRsq.setOddsType(3);
        eventInfoByPageRsq.setPage(1);
        eventInfoByPageRsq.setSeason(0);
        eventInfoByPageRsq.setIsCombo(false);
        Flowable flowable = model.getIMApiService().getLiveEventInfo(eventInfoByPageRsq);
        return flowable;
    }

    private Flowable getFlowableNoLiveMatchesPage(PMListReq pmListReq) {
        //Flowable flowable = model.getIMApiService().noLiveMatchesPagePB(pmListReq);
        EventInfoByPageRsq eventInfoByPageRsq = new EventInfoByPageRsq();
        eventInfoByPageRsq.setSportId(1);
        eventInfoByPageRsq.setMarket("2");
        eventInfoByPageRsq.setMatchDay(0);
        eventInfoByPageRsq.setOddsType(3);
        eventInfoByPageRsq.setPage(1);
        eventInfoByPageRsq.setSeason(0);
        eventInfoByPageRsq.setIsCombo(false);
        Flowable flowable = model.getIMApiService().getEventInfoByPage(eventInfoByPageRsq);
        return flowable;
    }

    private Flowable getFlowableMatchBaseInfoByMidsPB(PMListReq pmListReq) {
        Flowable flowable;
        EventInfoByPageRsq eventInfoByPageRsq = new EventInfoByPageRsq();
        eventInfoByPageRsq.setSportId(1);
        eventInfoByPageRsq.setMarket("2");
        eventInfoByPageRsq.setMatchDay(0);
        eventInfoByPageRsq.setOddsType(3);
        eventInfoByPageRsq.setPage(1);
        eventInfoByPageRsq.setSeason(0);
        eventInfoByPageRsq.setIsCombo(false);
        flowable = model.getIMApiService().getEventInfoByPage(eventInfoByPageRsq);

        return flowable;
    }

    // 根据条件设置 HttpCallBack 类型的对象
    public void createIMListCallback(boolean isTimerRefresh, boolean isRefresh, int sportPos, String sportId, int orderBy, List<Long> leagueIds, int searchDatePos, int oddType, List<Long> matchidList, Flowable flowable) {
        // 根据是否使用缓存选择回调类型
        Object callback = new IMListCallBack(this, mHasCache, isTimerRefresh, isRefresh, mPlayMethodType,
                sportPos, sportId, orderBy, leagueIds, searchDatePos, oddType, matchidList);
        // 统一处理 Flowable 的线程调度和异常处理
        Disposable disposable = createDisposable(flowable, callback);
        // 添加订阅管理
        addSubscribe(disposable);
    }

    // 根据条件返回 HttpCallBack 类型的对象
    public void createIMLeagueListCallback(boolean isTimerRefresh, boolean isRefresh,
                                           int sportPos, String sportId, int orderBy, List<Long> leagueIds,
                                           int searchDatePos, int oddType, List<Long> matchidList, int finalType, boolean isStepSecond, Flowable flowable) {
        mImLeagueCallBack = new IMLeagueListCallBack(this, mHasCache, isTimerRefresh, isRefresh, mCurrentPage,
                mPlayMethodType, sportPos, sportId, orderBy, leagueIds, searchDatePos, oddType,
                matchidList, finalType, isStepSecond);
        Object callback = mImLeagueCallBack;
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

    private Flowable getOutrightEvents(OutrightEventsReq outrightEventsReq) {
        outrightEventsReq = new OutrightEventsReq();
        Flowable flowable = model.getIMApiService().getOutrightEvents(outrightEventsReq);
        return flowable;
    }
}
