package com.xtree.bet.ui.viewmodel.callback;

import com.xtree.base.net.HttpCallBack;
import com.xtree.bet.bean.response.fb.MatchListRsp;
import com.xtree.bet.ui.viewmodel.fb.FBMainViewModel;

import com.xtree.base.http.BusinessException;
import com.xtree.base.utils.KLog;


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
        if (t instanceof BusinessException) {
            BusinessException rError = (BusinessException) t;
            KLog.e("code: " + rError.code);
        }
    }

}
