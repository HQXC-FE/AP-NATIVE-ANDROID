package com.xtree.bet.ui.viewmodel.callback;

import com.xtree.base.net.HttpCallBack;
import com.xtree.bet.bean.response.fb.FbMatchListCacheRsp;
import com.xtree.bet.ui.viewmodel.fb.FBMainViewModel;

import me.xtree.mvvmhabit.http.ResponseThrowable;
import me.xtree.mvvmhabit.utils.KLog;

public class FBhotMatchCacheCallBack extends HttpCallBack<FbMatchListCacheRsp> {
    private FBMainViewModel mViewModel;

    public FBhotMatchCacheCallBack(FBMainViewModel viewModel) {
        mViewModel = viewModel;
    }

    @Override
    public void onResult(FbMatchListCacheRsp matchListRsp) {
        mViewModel.hotMatchCountData.postValue(matchListRsp.data.getTotal());
    }

    @Override
    public void onError(Throwable t) {
        if (t instanceof ResponseThrowable) {
            ResponseThrowable rError = (ResponseThrowable) t;
            KLog.e("code: " + rError.code);
        }
    }

}
