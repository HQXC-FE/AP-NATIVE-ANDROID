package com.xtree.bet.ui.viewmodel.callback;

import com.xtree.base.net.HttpCallBack;
import com.xtree.bet.bean.response.fb.FbStatisticalInfoCacheRsp;
import com.xtree.bet.bean.response.fb.MatchTypeInfo;
import com.xtree.bet.bean.response.fb.MatchTypeStatisInfo;
import com.xtree.bet.constant.FBConstants;
import com.xtree.bet.constant.SportTypeItem;
import com.xtree.bet.ui.viewmodel.fb.FBMainViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import me.xtree.mvvmhabit.http.BusinessException;
import me.xtree.mvvmhabit.utils.KLog;

public class FBStatisticalCacheCallBack extends HttpCallBack<FbStatisticalInfoCacheRsp> {

    private FBMainViewModel mViewModel;
    private HashMap<Integer, SportTypeItem> mMatchGames;
    private ConcurrentHashMap<String, List<SportTypeItem>> mSportCountMap;

    public FBStatisticalCacheCallBack(FBMainViewModel viewModel, HashMap<Integer, SportTypeItem> matchGames, ConcurrentHashMap<String, List<SportTypeItem>> sportCountMap) {
        mViewModel = viewModel;
        mMatchGames = matchGames;
        mSportCountMap = sportCountMap;
    }

    @Override
    public synchronized void onResult(FbStatisticalInfoCacheRsp statisticalInfo) {
        if (mMatchGames.isEmpty()) {
            mMatchGames = FBConstants.getMatchGames();
        }
        KLog.i("##### FBStatisticalCacheCallBack statisticalInfo: ", statisticalInfo+" #####");
        for (MatchTypeInfo matchTypeInfo : statisticalInfo.data.sl) {
            //"6", "1", "4", "2", "7"; 只有"今日", "滚球", "早盘", "串关", "冠军"数据才添加，提升效率
            if (matchTypeInfo.ty == 6 || matchTypeInfo.ty == 1 || matchTypeInfo.ty == 4 || matchTypeInfo.ty == 2 || matchTypeInfo.ty == 7) {
                //Map<String, Integer> sslMap = new HashMap<>();
                ArrayList<SportTypeItem> sportTypeItemList = new ArrayList<>();
                if (matchTypeInfo.ty == 6 || matchTypeInfo.ty == 2) {//今日 串关 加热门
                    SportTypeItem item1 = new SportTypeItem();
                    item1.id = 1111;
                    item1.num = 0;
                    sportTypeItemList.add(item1);
                } else if (matchTypeInfo.ty == 1) {//滚球 加全部
                    SportTypeItem item2 = new SportTypeItem();
                    item2.id = 0;
                    item2.num = matchTypeInfo.tc;
                    sportTypeItemList.add(item2);
                }
                for (MatchTypeStatisInfo matchTypeStatisInfo : matchTypeInfo.ssl) {
                    SportTypeItem item = new SportTypeItem();
                    item.id = matchTypeStatisInfo.sid;
                    item.num = matchTypeStatisInfo.c;
                    if (item.num <= 0 || mMatchGames.get(item.id) == null) {
                        continue;
                    }
                    sportTypeItemList.add(item);
                    //sslMap.put(String.valueOf(matchTypeStatisInfo.sid), matchTypeStatisInfo.c);
                }

                mSportCountMap.put(String.valueOf(matchTypeInfo.ty), sportTypeItemList);
                //CfLog.i("num223   " + String.valueOf(6) + "  " + new Gson().toJson(sportCountMap.get(String.valueOf(6))));
            }
        }
        //CfLog.i("num22   " + 6 + "  " + new Gson().toJson(sportCountMap.get(String.valueOf(6))));
        //CfLog.i("num22   " + new Gson().toJson(sportCountMap));
        mViewModel.statisticalData.postValue(mSportCountMap);
    }

    @Override
    public void onError(Throwable t) {
        super.onError(t);
        if (t instanceof BusinessException) {
            BusinessException rError = (BusinessException) t;
            KLog.e("##### FBStatisticalCacheCallBack onError: ", rError+" #####");
        }
    }
}
