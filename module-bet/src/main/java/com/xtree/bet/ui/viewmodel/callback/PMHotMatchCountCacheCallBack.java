package com.xtree.bet.ui.viewmodel.callback;

import static com.xtree.base.net.HttpCallBack.CodeRule.CODE_401013;
import static com.xtree.base.net.HttpCallBack.CodeRule.CODE_401026;
import static com.xtree.base.net.HttpCallBack.CodeRule.CODE_401038;

import com.xtree.base.net.HttpCallBack;
import com.xtree.bet.bean.response.pm.MatchLeagueListCacheRsp;
import com.xtree.bet.ui.viewmodel.pm.PMMainViewModel;

import me.xtree.mvvmhabit.http.BusinessException;

public class PMHotMatchCountCacheCallBack extends HttpCallBack<MatchLeagueListCacheRsp> {

    private PMMainViewModel mViewModel;

    public PMHotMatchCountCacheCallBack(PMMainViewModel viewModel) {
        mViewModel = viewModel;
    }

    @Override
    public void onResult(MatchLeagueListCacheRsp matchListRsp) {
        mViewModel.hotMatchCountData.postValue(matchListRsp.data.getData().size());
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
