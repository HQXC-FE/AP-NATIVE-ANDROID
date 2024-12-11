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
import com.xtree.base.utils.CfLog;
import com.xtree.live.BR;
import com.xtree.live.R;
import com.xtree.live.broadcaster.fragment.LiveShareDialog;
import com.xtree.live.data.factory.AppViewModelFactory;
import com.xtree.live.data.source.request.SearchAssistantRequest;
import com.xtree.live.data.source.request.SendToAssistantRequest;
import com.xtree.live.data.source.response.AnchorSortResponse;
import com.xtree.live.data.source.response.ChatRoomResponse;
import com.xtree.live.data.source.response.SearchAssistantResponse;
import com.xtree.live.databinding.FragmentLiveBroadcasterBinding;
import com.xtree.live.ui.main.adapter.AttentionListAdapter;
import com.xtree.live.ui.main.viewmodel.AttentionListModel;

import java.util.ArrayList;
import java.util.Collections;

import me.xtree.mvvmhabit.base.BaseFragment;

/**
 * 主播列表
 */
@Route(path = RouterFragmentPath.Live.PAGER_LIVE_ATTENTION)
public class AttentionListFragment extends BaseFragment<FragmentLiveBroadcasterBinding, AttentionListModel> implements AttentionListModel.ICallBack {

    private AttentionListAdapter mAdapter;
    private BasePopupView shareView;
    private LiveShareDialog liveShareDialog;
    private AnchorSortResponse anchorSortResponse ;
    private ChatRoomResponse chatRoomResponse;//聊天房列表
    private SearchAssistantResponse searchAssistantResponse ;//搜索主播助理


    @Override
    public void initView() {
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
                                ClipData clipData = ClipData.newPlainText("shareUrl", sharUrl);
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

        CfLog.e("AttentionListFragment ----> callback" );
        viewModel.getAnchorSort();
        /* viewModel.getChatRoomList();*/

     //   viewModel.searchAssistant(new SearchAssistantRequest("20"));
/*
        viewModel.sendAssistant( new SendToAssistantRequest(20 ,"测试",1,"","10001"));
        viewModel.searchAssistant(new SearchAssistantRequest("20"));*/
        viewModel.getChatRoomList();

    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();

        viewModel.anchorSortResponseMutableLiveData.observe(getActivity() , vo ->{
            if (vo != null){
                anchorSortResponse = vo ;
                 mAdapter = new AttentionListAdapter(getContext() , anchorSortResponse);
                 binding.rvMain.setAdapter(mAdapter);
                 mAdapter.notifyDataSetChanged();
            }

        });

        viewModel.chatRoomResponseMutableLiveData.observe(getActivity(), vo -> {
            if (vo != null ) {
                chatRoomResponse = vo;
                CfLog.e("initViewObservable = " + chatRoomResponse.toString());
                /*Collections.sort(anchorSortResponse.data);
                mAdapter = new AttentionListAdapter(getContext(), chatRoomResponse);
                binding.rvMain.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();*/
            }

        });
        //搜索主播助理
        viewModel.searchAssistantResponseMutableLiveData.observe(getActivity() , vo->{
            if (vo != null){
                searchAssistantResponse = vo;
                CfLog.e("searchAssistantResponse = " + searchAssistantResponse.toString());
            }
        });

    }
}
