package com.xtree.bet.ui.viewmodel.callback;

import com.xtree.base.net.HttpCallBack;
import com.xtree.bet.bean.response.fb.MatchListRsp;
import com.xtree.bet.ui.viewmodel.fb.FBMainViewModel;

import me.xtree.mvvmhabit.http.ResponseThrowable;
import me.xtree.mvvmhabit.utils.KLog;


public class FBhotMatchCallBack extends HttpCallBack<MatchListRsp> {
    private FBMainViewModel mViewModel;

    public FBhotMatchCallBack(FBMainViewModel viewModel) {
        mViewModel = viewModel;
    }

    @Override
    public void onResult(MatchListRsp matchListRsp) {
        mViewModel.hotMatchCountData.postValue(matchListRsp.getTotal());
    }

    @Override
    public void onError(Throwable t) {
        if (t instanceof ResponseThrowable) {
            ResponseThrowable rError = (ResponseThrowable) t;
            KLog.e("code: " + rError.code);
        }
    }

}
