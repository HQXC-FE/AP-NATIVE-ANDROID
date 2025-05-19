package com.xtree.bet.ui.viewmodel.im;

import static com.xtree.base.utils.BtDomainUtil.KEY_PLATFORM;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_FBXC;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.utils.BtDomainUtil;
import com.xtree.base.utils.ClickUtil;
import com.xtree.base.vo.FBService;
import com.xtree.bet.EventInfoByPageListParser;
import com.xtree.bet.bean.request.im.BaseIMRequest;
import com.xtree.bet.bean.request.im.SelectedEventInfoReq;
import com.xtree.bet.bean.response.im.EventInfoByPageListRsp;
import com.xtree.bet.bean.response.im.EventListRsp;
import com.xtree.bet.bean.response.im.MarketLine;
import com.xtree.bet.bean.response.im.MatchInfo;
import com.xtree.bet.bean.response.im.Sport;
import com.xtree.bet.bean.ui.Category;
import com.xtree.bet.bean.ui.CategoryIm;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.MatchIm;
import com.xtree.bet.bean.ui.Option;
import com.xtree.bet.bean.ui.OptionList;
import com.xtree.bet.bean.ui.PlayType;
import com.xtree.bet.bean.ui.PlayTypeIm;
import com.xtree.bet.constant.IMMarketTag;
import com.xtree.bet.data.BetRepository;
import com.xtree.bet.data.IMApiService;
import com.xtree.bet.ui.viewmodel.TemplateBtDetailViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.sentry.protocol.App;
import me.xtree.mvvmhabit.http.BaseResponse;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.Utils;

/**
 * Created by marquis
 */

public class IMBtDetailViewModel extends TemplateBtDetailViewModel {
    private long mMatchId;
    private String mSportId;

    public IMBtDetailViewModel(@NonNull Application application, BetRepository repository) {
        super(application, repository);
    }

    public void getMatchDetail(long matchId, String sportId) {
        mMatchId = matchId;
        mSportId = sportId;

//        Map<String, String> map = new HashMap<>();
//        map.put("languageType", "CMN");
//        map.put("matchId", String.valueOf(matchId));
//
//        List<Long> eventsId = new ArrayList<>();
//        eventsId.add(mMatchId);
//        SelectedEventInfoReq req = new SelectedEventInfoReq(Long.parseLong(sportId), eventsId, 2, false, true);
//        launchFlow(model.getIMApiService().getSelectedEventInfo(new BaseIMRequest<>(IMApiService.GetSelectedEventInfo, req)), new HttpCallBack<EventInfoByPageListRsp>() {
//            @Override
//            public void onResult(EventInfoByPageListRsp eventListRsp) {
//                super.onResult(eventListRsp);
//                List<Category> categoryList = getCategoryList(eventListRsp);
//                if (categoryList != null && !eventListRsp.getSports().isEmpty()) {
//                    List<MatchInfo> events = eventListRsp.getSports().get(0).getEvents();
//                    if (!events.isEmpty()) {
//                        Match match = new MatchIm(events.get(0));
//                        matchData.postValue(match);
//                    }
//                    categoryListData.postValue(categoryList);
//                }
//            }
//        });


        EventInfoByPageListRsp eventListRsp = EventInfoByPageListParser.getSelectedEventInfo(Utils.getContext());
        List<Category> categoryList = getCategoryList(eventListRsp);
        List<MatchInfo> events = eventListRsp.getSports().get(0).getEvents();
        Match match = new MatchIm(events.get(0));
        matchData.postValue(match);
        categoryListData.postValue(categoryList);

    }

    public void getMatchDetailResult(long matchId) {
    }

    public List<Category> getCategoryList(EventInfoByPageListRsp matchInfo) {
        Map<String, Category> categoryMap = new HashMap<>();
        List<Category> categoryList = new ArrayList<>();
        if (matchInfo.getSports().isEmpty()) {
            return categoryList;
        }
        CategoryIm categoryAll = new CategoryIm(IMMarketTag.getMarketTag("all"));
        categoryMap.put("all", categoryAll);
        categoryList.add(categoryAll);

        Sport sport = matchInfo.getSports().get(0);
        List<MatchInfo> events = sport.getEvents();

        for (MatchInfo event : events) {
            //分配分组，设置GroupName
            IMOrganizedMarkLinesManager.shared.organizedMarkLinesWith(sport, event);
            for (MarketLine marketLine : event.getMarketLines()) {
                marketLine.setOpenParlay(event.openParlay); //里面新增一个是否串关的字段
                PlayType playType = new PlayTypeIm(marketLine, event);
                categoryAll.addPlayTypeList(playType);
                List<String> names = marketLine.getBetTypeGroupName();
                for (String name : names) {
                    if (categoryMap.get(name) == null) {
                        String marketName = IMMarketTag.getMarketTag(name);
                        if (marketName != null) {
                            Category category = new CategoryIm(marketName);
                            categoryMap.put(name, category);
                            categoryList.add(category);
                        }
                    }
                    if (name != null && categoryMap.get(name) != null) {
                        categoryMap.get(name).addPlayTypeList(playType);
                    }
                }

            }
        }

        if (mCategoryMap.isEmpty()) {
            mCategoryMap = categoryMap;
            mCategoryList = categoryList;
        } else {
            if (categoryMap.size() <= mCategoryMap.size()) {
                for (String key : mCategoryMap.keySet()) {
                    Category oldCategory = mCategoryMap.get(key);
                    int index = mCategoryList.indexOf(oldCategory);
                    Category newCategory = categoryMap.get(key);
                    if (index > -1) {
                        mCategoryList.set(index, newCategory);
                        mCategoryMap.put(key, newCategory);
                    }
                }
            }
        }
        return mCategoryList;
    }

    /**
     * 设置赔率变化
     *
     * @param mewMatch
     */
    void setOptionOddChange(Match mewMatch) {
        List<Option> newOptonList = getMatchOptionList(mewMatch);
        List<Option> oldOptonList = getMatchOptionList(mMatch);

        for (Option newOption : newOptonList) {
            for (Option oldOption : oldOptonList) {
                if (oldOption != null && newOption != null && oldOption.getRealOdd() != newOption.getRealOdd() && TextUtils.equals(oldOption.getCode(), newOption.getCode())) {
                    newOption.setChange(oldOption.getRealOdd());
                    break;
                }
            }
        }
    }

    private List<Option> getMatchOptionList(Match match) {
        if (match == null) {
            return new ArrayList<>();
        }
        List<Option> optionArrayList = new ArrayList<>();
        for (PlayType playType : match.getPlayTypeList()) {
            if (playType.getOptionLists() != null) {
                for (OptionList optionList : playType.getOptionLists()) {
                    for (Option option : optionList.getOptionList()) {
                        if (option != null) {
                            StringBuffer code = new StringBuffer();
                            code.append(match.getId());
                            code.append(playType.getPlayType());
                            code.append(playType.getPlayPeriod());
                            code.append(optionList.getId());
                            code.append(option.getOptionType());
                            code.append(option.getId());
                            if (!TextUtils.isEmpty(option.getLine())) {
                                code.append(option.getLine());
                            }
                            option.setCode(code.toString());
                        }
                        optionArrayList.add(option);
                    }
                }
            }
        }
        return optionArrayList;
    }

    public synchronized void getGameTokenApi() {
        if (ClickUtil.doNotRepeatRequests()) {
            return;
        }

        Flowable<BaseResponse<FBService>> flowable;
        String mPlatform = SPUtils.getInstance().getString(KEY_PLATFORM);
        if (TextUtils.equals(mPlatform, PLATFORM_FBXC)) {
            flowable = model.getBaseApiService().getFBXCGameTokenApi();
        } else {
            flowable = model.getBaseApiService().getFBGameTokenApi();
        }

        launchFlow(flowable, new HttpCallBack<FBService>() {
            @Override
            public void onResult(FBService fbService) {
                if (TextUtils.equals(mPlatform, PLATFORM_FBXC)) {
                    SPUtils.getInstance().put(SPKeyGlobal.FBXC_TOKEN, fbService.getToken());
                    SPUtils.getInstance().put(SPKeyGlobal.FBXC_DISABLED, fbService.isDisabled);
                    SPUtils.getInstance().put(SPKeyGlobal.FBXC_API_SERVICE_URL, fbService.getForward().getApiServerAddress());
                    BtDomainUtil.setDefaultFbxcDomainUrl(fbService.getForward().getApiServerAddress());
                    BtDomainUtil.addFbxcDomainUrl(fbService.getForward().getApiServerAddress());
                    BtDomainUtil.setFbxcDomainUrl(fbService.getDomains());
                } else {
                    SPUtils.getInstance().put(SPKeyGlobal.FB_TOKEN, fbService.getToken());
                    SPUtils.getInstance().put(SPKeyGlobal.FB_DISABLED, fbService.isDisabled);
                    SPUtils.getInstance().put(SPKeyGlobal.FB_API_SERVICE_URL, fbService.getForward().getApiServerAddress());
                    BtDomainUtil.setDefaultFbDomainUrl(fbService.getForward().getApiServerAddress());
                    BtDomainUtil.addFbDomainUrl(fbService.getForward().getApiServerAddress());
                    BtDomainUtil.setFbDomainUrl(fbService.getDomains());
                }

                getMatchDetail(mMatchId, mSportId);
            }
        });

//        Disposable disposable = (Disposable) flowable
//                .compose(RxUtils.schedulersTransformer()) //线程调度
//                .compose(RxUtils.exceptionTransformer())
//                .subscribeWith(new HttpCallBack<FBService>() {
//                    @Override
//                    public void onResult(FBService fbService) {
//                        if (TextUtils.equals(mPlatform, PLATFORM_FBXC)) {
//                            SPUtils.getInstance().put(SPKeyGlobal.FBXC_TOKEN, fbService.getToken());
//                            SPUtils.getInstance().put(SPKeyGlobal.FBXC_DISABLED, fbService.isDisabled);
//                            SPUtils.getInstance().put(SPKeyGlobal.FBXC_API_SERVICE_URL, fbService.getForward().getApiServerAddress());
//                            BtDomainUtil.setDefaultFbxcDomainUrl(fbService.getForward().getApiServerAddress());
//                            BtDomainUtil.addFbxcDomainUrl(fbService.getForward().getApiServerAddress());
//                            BtDomainUtil.setFbxcDomainUrl(fbService.getDomains());
//                        } else {
//                            SPUtils.getInstance().put(SPKeyGlobal.FB_TOKEN, fbService.getToken());
//                            SPUtils.getInstance().put(SPKeyGlobal.FB_DISABLED, fbService.isDisabled);
//                            SPUtils.getInstance().put(SPKeyGlobal.FB_API_SERVICE_URL, fbService.getForward().getApiServerAddress());
//                            BtDomainUtil.setDefaultFbDomainUrl(fbService.getForward().getApiServerAddress());
//                            BtDomainUtil.addFbDomainUrl(fbService.getForward().getApiServerAddress());
//                            BtDomainUtil.setFbDomainUrl(fbService.getDomains());
//                        }
//
//                        getMatchDetail(mMatchId);
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//                        //super.onError(t);
//                    }
//                });
//        addSubscribe(disposable);
    }

}
