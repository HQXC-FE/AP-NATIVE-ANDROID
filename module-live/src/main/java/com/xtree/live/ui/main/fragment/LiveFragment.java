package com.xtree.live.ui.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.live.BR;
import com.xtree.live.R;
import com.xtree.live.data.LiveRepository;
import com.xtree.live.data.factory.AppViewModelFactory;
import com.xtree.live.data.source.request.LiveTokenRequest;
import com.xtree.live.data.source.response.LiveTokenResponse;
import com.xtree.live.databinding.FragmentLiveBinding;
import com.xtree.live.ui.main.viewmodel.LiveViewModel;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.RxUtils;

/**
 * Created by KAKA on 2024/9/9.
 * Describe: 直播门户页
 */
@Route(path = RouterFragmentPath.Live.PAGER_LIVE_MAIN)
public class LiveFragment extends BaseFragment<FragmentLiveBinding, LiveViewModel> {

    public LiveFragment() {
        LiveRepository.getInstance().getLiveToken(new LiveTokenRequest())
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new HttpCallBack<LiveTokenResponse>() {
                    @Override
                    public void onResult(LiveTokenResponse data) {
                        if (data.getAppApi() != null && !data.getAppApi().isEmpty()) {
                            LiveRepository.getInstance().setLive(data);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });
    }

    @Override
    public void initView() {

    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_live;
    }

    @Override
    public int initVariableId() {
        return BR.model;
    }

    @Override
    public LiveViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(LiveViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();
        viewModel.initData(requireActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            //刷新数据
            if (binding.tabLayout.getSelectedTabPosition() != -1) {
                viewModel.refresh(binding.tabLayout.getTabAt(binding.tabLayout.getSelectedTabPosition()).getText().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
