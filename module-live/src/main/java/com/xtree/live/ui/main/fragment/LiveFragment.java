package com.xtree.live.ui.main.fragment;

import static com.xtree.base.utils.BtDomainUtil.PLATFORM_FBXC;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.net.live.X9LiveInfo;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.util.MatchDeserializer;
import com.xtree.live.BR;
import com.xtree.live.R;
import com.xtree.live.chat.RequestUtils;
import com.xtree.live.data.LiveRepository;
import com.xtree.live.data.factory.AppViewModelFactory;
import com.xtree.live.data.source.httpnew.LiveRep;
import com.xtree.live.data.source.request.LiveTokenRequest;
import com.xtree.live.data.source.response.LiveTokenResponse;
import com.xtree.live.databinding.FragmentLiveBinding;
import com.xtree.live.ui.main.viewmodel.LiveViewModel;

import java.util.ArrayList;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;

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
        CfLog.d("================ LiveFragment ==============");
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
        Gson gson = new GsonBuilder().serializeNulls().registerTypeAdapter(Match.class, new MatchDeserializer()).create();
        Match mMatch = gson.fromJson(SPUtils.getInstance().getString("KEY_MATCH_ID"), Match.class);
        //System.out.println("============== LiveFragment mMatch.getSportId() =================="+mMatch.getId());
        //LiveMatchDetailActivity.start(getContext(),mMatch.getId());

//        FloatingWindowActivity.start(getContext());
          viewModel.getHotLeague(PLATFORM_FBXC);
    }
}
