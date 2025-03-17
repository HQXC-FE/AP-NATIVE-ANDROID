package com.xtree.mine.ui.rebateagrt.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentGameDividendagrtBinding;
import com.xtree.mine.ui.rebateagrt.model.RebateAreegmentModel;
import com.xtree.mine.ui.rebateagrt.viewmodel.GameDividendAgrtViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.RxBus;

/**
 * Created by KAKA on 2024/3/14.
 * Describe: 游戏场馆分红契约
 */
@Route(path = RouterFragmentPath.Mine.PAGER_DIVIDEND_AGREEMENT_GAME)
public class GameDividendAgrtFragment extends BaseFragment<FragmentGameDividendagrtBinding, GameDividendAgrtViewModel> {
    @Override
    public void initView() {
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_game_dividendagrt;
    }

    @Override
    public int initVariableId() {
        return BR.model;
    }

    @Override
    public GameDividendAgrtViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(GameDividendAgrtViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();
        viewModel.setActivity(getActivity());
        RebateAreegmentModel rebateAreegmentModel = RxBus.getDefault().getStickyEvent(RebateAreegmentModel.class);
        if (rebateAreegmentModel != null) {
            viewModel.initData(rebateAreegmentModel.type);
        }
        EventBus.getDefault().register(this);
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        //这里其实可以封装到Base
        viewModel.getUC().getSmartRefreshListenerEvent().observe(this, integer -> {
            if (integer == BaseViewModel.ONFINISH_LOAD_MORE) {
                binding.refreshLayout.resetNoMoreData();
                binding.refreshLayout.finishLoadMore();
            } else if (integer == BaseViewModel.ONFINISH_REFRESH) {
                binding.refreshLayout.finishRefresh();
            } else if (integer == BaseViewModel.ON_LOAD_MORE_WITH_NO_MORE_DATA) {
                binding.refreshLayout.finishLoadMoreWithNoMoreData();
                binding.refreshLayout.finishRefreshWithNoMoreData();
            } else if (integer == BaseViewModel.ONFINISH_REFRESH_FAILED) {
                binding.refreshLayout.finishRefresh(false);
            } else if (integer == BaseViewModel.ONFINISH_LOAD_MORE_FAILED) {
                binding.refreshLayout.finishLoadMore(false);
            }
        });

//        //监听创建契约完成消息
//        RxBus.getDefault().toFlowable(String.class).subscribe(new Subscriber<String>() {
//            @Override
//            public void onSubscribe(Subscription s) {
//            }
//
//            @Override
//            public void onNext(String s) {
//                if (s.equals(DividendAgrtCheckDialogFragment.CREATED)) {
//                    viewModel.onResume();
//                }
//                if (s.equals(DividendAgrtSendDialogFragment.SENT)) {
//                    viewModel.onResume();
//                }
//                RxBus.getDefault().removeAllStickyEvents();
//            }
//
//            @Override
//            public void onError(Throwable t) {
//                t.toString();
//            }
//
//            @Override
//            public void onComplete() {
//                Log.e("TAG", "onComplete: " );
//            }
//        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String event) {
        if (event.equals(DividendAgrtCheckDialogFragment.CREATED)) {
            viewModel.onResume();
        }
        if (event.equals(DividendAgrtSendDialogFragment.SENT)) {
            viewModel.onResume();
        }
        EventBus.getDefault().removeStickyEvent(event);
    }
}
