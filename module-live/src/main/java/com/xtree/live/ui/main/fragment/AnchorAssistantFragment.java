package com.xtree.live.ui.main.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.tabs.TabLayoutMediator;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.live.X9LiveInfo;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.live.BR;
import com.xtree.live.R;
import com.xtree.live.data.factory.AppViewModelFactory;
import com.xtree.live.data.source.request.SearchAssistantRequest;
import com.xtree.live.databinding.FragmentAnchorAssistantBinding;
import com.xtree.live.databinding.FragmentChatBinding;
import com.xtree.live.ui.main.adapter.AttentionListAdapter;
import com.xtree.live.ui.main.viewmodel.AttentionListModel;
import com.xtree.live.ui.main.viewmodel.LiveViewModel;
import com.xtree.service.WebSocketService;
import com.xtree.service.message.MessageData;
import com.xtree.service.message.MessageType;
import com.xtree.service.message.PushServiceConnection;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import io.sentry.Sentry;
import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.SPUtils;

/**
 * 主播助理
 */
@Route(path = RouterFragmentPath.Live.PAGER_LIVE_CHAT_ANCHOR)
public class AnchorAssistantFragment extends BaseFragment<FragmentAnchorAssistantBinding, AttentionListModel>  implements AttentionListModel.ICallBack {
    private boolean isInput = false;



    @Override
    public void initView() {
      binding.tvSearch.setOnClickListener(v -> {
          if (isInput){
              //发送搜索请求
              if ( binding.edSearch.getText().toString().trim().length() >0){
                  SearchAssistantRequest request = new SearchAssistantRequest(binding.edSearch.getText().toString().trim());
                  LoadingDialog.show(getActivity());
                  viewModel.searchAssistant(request);
              }

          }

      });
      binding.edSearch.addTextChangedListener(new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence s, int start, int count, int after) {

          }

          @Override
          public void onTextChanged(CharSequence s, int start, int before, int count) {

          }

          @Override
          public void afterTextChanged(Editable s) {
              String inputString = s.toString();
              if (s.length() == 0){
                  isInput = false;
              }else{
                  isInput = true;

              }
          }
      });

    }

    @Override
    public void initData() {
        super.initData();
        //发送获取列表数据请求
        viewModel.setCallBack(this);
        viewModel.initData(requireActivity());
    }
    @Override
    public void callback() {
        //viewModel.getAnchorSort();

    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();

    }
    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_anchor_assistant;
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


}
