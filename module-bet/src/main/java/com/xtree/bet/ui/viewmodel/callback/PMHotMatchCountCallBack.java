package com.xtree.bet.ui.viewmodel.callback;

import static com.xtree.base.net.HttpCallBack.CodeRule.CODE_401013;
import static com.xtree.base.net.HttpCallBack.CodeRule.CODE_401026;
import static com.xtree.base.net.HttpCallBack.CodeRule.CODE_401038;

import com.xtree.base.net.HttpCallBack;
import com.xtree.bet.bean.response.pm.MatchListRsp;
import com.xtree.bet.ui.viewmodel.pm.PMMainViewModel;


import me.xtree.mvvmhabit.http.BusinessException;

public class PMHotMatchCountCallBack extends HttpCallBack<MatchListRsp> {

    private PMMainViewModel mViewModel;

    public PMHotMatchCountCallBack(PMMainViewModel viewModel) {
        mViewModel = viewModel;
    }

    @Override
    public void onResult(MatchListRsp matchListRsp) {
        mViewModel.hotMatchCountData.postValue(matchListRsp.data.size());
    }

    @Override
    public void onError(Throwable t) {
        mViewModel.getUC().getDismissDialogEvent().call();
        if (t instanceof BusinessException) {
            BusinessException error = (BusinessException) t;
            if (error.code == CODE_401026 || error.code == CODE_401013 || error.code == CODE_401013) {
                mViewModel.getGameTokenApi();

            } else if (error.code == CODE_401038) {
                super.onError(t);
                mViewModel.tooManyRequestsEvent.call();
            }
        }
    }
}
