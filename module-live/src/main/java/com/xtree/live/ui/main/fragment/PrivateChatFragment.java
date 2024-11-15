package com.xtree.live.ui.main.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.live.BR;
import com.xtree.live.R;
import com.xtree.live.data.factory.AppViewModelFactory;
import com.xtree.live.databinding.FragmentPrivateChatBinding;
import com.xtree.live.ui.main.viewmodel.LiveViewModel;

import me.xtree.mvvmhabit.base.BaseFragment;

/**
 * 直播间助理私聊
 */
@Route(path = RouterFragmentPath.Live.PAGER_LIVE_CHAT_PRIVATE)
public class PrivateChatFragment extends BaseFragment<FragmentPrivateChatBinding, LiveViewModel> {
    private boolean isInput = false;



    @Override
    public void initView() {
      binding.tvSearch.setOnClickListener(v -> {
          if (isInput){
              //发送搜索请求
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
    public LiveViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(LiveViewModel.class);
    }


}
