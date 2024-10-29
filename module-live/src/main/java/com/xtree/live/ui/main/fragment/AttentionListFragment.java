package com.xtree.live.ui.main.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.live.BR;
import com.xtree.live.R;
import com.xtree.live.broadcaster.fragment.LiveShareDialog;
import com.xtree.live.data.factory.AppViewModelFactory;
import com.xtree.live.databinding.FragmentLiveBroadcasterBinding;
import com.xtree.live.ui.main.adapter.BroadcasterAdapter;
import com.xtree.live.ui.main.viewmodel.AttentionListModel;

import java.util.ArrayList;

import me.xtree.mvvmhabit.base.BaseFragment;

/**
 * 主播列表
 */
@Route(path = RouterFragmentPath.Live.PAGER_LIVE_ATTENTION)
public class AttentionListFragment extends BaseFragment<FragmentLiveBroadcasterBinding, AttentionListModel> implements AttentionListModel.ICallBack {
    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private ArrayList<String> tabList = new ArrayList<>();
    private BroadcasterAdapter mAdapter;
    private BasePopupView shareView;
    private LiveShareDialog liveShareDialog ;



    @Override
    public void initView() {
        mAdapter = new BroadcasterAdapter(getContext());
        binding.rvMain.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        binding.ivwShare.setOnClickListener(v -> {
            if (shareView == null) {

                shareView = new XPopup.Builder(getContext())
                        .dismissOnTouchOutside(false)
                        .dismissOnBackPressed(false)
                        .asCustom(new LiveShareDialog(getContext(), "'", new LiveShareDialog.ILiveShareCallback() {
                            @Override
                            public void onClickShare(String sharUrl) {
                                //辅助到剪切版
                               ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clipData = ClipData.newPlainText("shareUrl",sharUrl);
                                clipboardManager.setPrimaryClip(clipData);
                                shareView.dismiss();
                            }

                        }));
                shareView.show();
            }

        });
        binding.ivwBack.setOnClickListener(v -> {
            getActivity().finish();
        });
/*
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
                });*/
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_live_broadcaster;
    }

    @Override
    public int initVariableId() {
        return BR.model;
    }

    @Override
    public AttentionListModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(AttentionListModel.class);
    }
    @Override
    public void initData() {
        super.initData();
        viewModel.setCallBack(this);
        viewModel.initData(requireActivity());
    }

    @Override
    public void callback() {
        viewModel.getAnchorSort();

    }
}
