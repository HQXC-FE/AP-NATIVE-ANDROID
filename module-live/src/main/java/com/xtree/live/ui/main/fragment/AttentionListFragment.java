package com.xtree.live.ui.main.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.tabs.TabLayoutMediator;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.widget.MsgDialog;
import com.xtree.live.BR;
import com.xtree.live.R;
import com.xtree.live.broadcaster.fragment.LiveShareDialog;
import com.xtree.live.data.factory.AppViewModelFactory;
import com.xtree.live.databinding.FragmentChatBinding;
import com.xtree.live.databinding.FragmentLiveBroadcasterBinding;
import com.xtree.live.ui.main.adapter.BroadcasterAdapter;
import com.xtree.live.ui.main.model.anchorList.AttentionListModel;
import com.xtree.live.ui.main.viewmodel.LiveViewModel;

import java.util.ArrayList;

import me.xtree.mvvmhabit.base.BaseFragment;

/**
 * 主播列表
 */
@Route(path = RouterFragmentPath.Live.PAGER_LIVE_ATTENTION)
public class AttentionListFragment extends BaseFragment<FragmentLiveBroadcasterBinding, LiveViewModel> {
    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private ArrayList<String> tabList = new ArrayList<>();
    private BroadcasterAdapter mAdapter;
    private  ArrayList<AttentionListModel> attentionListModels = new ArrayList<AttentionListModel>();
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
    public LiveViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(LiveViewModel.class);
    }
}
