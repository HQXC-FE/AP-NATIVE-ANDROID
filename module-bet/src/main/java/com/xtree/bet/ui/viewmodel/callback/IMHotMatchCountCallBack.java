package com.xtree.bet.ui.viewmodel.callback;

import static com.xtree.base.net.HttpCallBack.CodeRule.CODE_401013;
import static com.xtree.base.net.HttpCallBack.CodeRule.CODE_401026;
import static com.xtree.base.net.HttpCallBack.CodeRule.CODE_401038;

import com.xtree.base.net.HttpCallBack;
import com.xtree.base.utils.CfLog;
import com.xtree.bet.EventInfoByPageListParser;
import com.xtree.bet.bean.response.im.EventInfoByPageListRsp;
import com.xtree.bet.bean.response.im.MatchInfo;
import com.xtree.bet.ui.activity.MainActivity;
import com.xtree.bet.ui.viewmodel.im.IMMainViewModel;

import java.util.ArrayList;
import java.util.List;

import me.xtree.mvvmhabit.http.BusinessException;

public class IMHotMatchCountCallBack extends HttpCallBack<EventInfoByPageListRsp> {

    private IMMainViewModel mViewModel;

    public IMHotMatchCountCallBack(IMMainViewModel viewModel) {
        mViewModel = viewModel;
    }

    @Override
    public void onResult(EventInfoByPageListRsp eventInfoByPageListRsp) {
        List<MatchInfo> mathInfoList = new ArrayList<>();
        //这里需要优化下，遍历还是取出热门赛事数量不划算
        EventInfoByPageListRsp matchListRsp = EventInfoByPageListParser.getEventInfoByPageListRsp(MainActivity.getContext());
        for(MatchInfo matchInfo: matchListRsp.getSports().get(0).getEvents()){
            if(matchInfo.isPopular){
                mathInfoList.add(matchInfo);
            }
        }
        mViewModel.hotMatchCountData.postValue(mathInfoList.size());
    }

    @Override
    public void onError(Throwable t) {
        mViewModel.getUC().getDismissDialogEvent().call();
        if (t instanceof BusinessException) {
            BusinessException error = (BusinessException) t;
            if (error.code == CODE_401026 || error.code == CODE_401013) {
                mViewModel.getGameTokenApi();

            } else if (error.code == CODE_401038) {
                super.onError(t);
                mViewModel.tooManyRequestsEvent.call();
            }
        }
    }
}
