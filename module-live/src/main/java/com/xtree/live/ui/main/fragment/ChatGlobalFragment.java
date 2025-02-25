package com.xtree.live.ui.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.xtree.live.R;
import com.xtree.live.data.factory.AppViewModelFactory;
import com.xtree.live.databinding.FragmentChatBinding;
import com.xtree.live.ui.main.viewmodel.LiveDetailHomeViewModel;
import com.xtree.live.ui.main.viewmodel.LiveViewModel;

import me.xtree.mvvmhabit.base.BaseFragment;

public class ChatGlobalFragment extends BaseFragment<FragmentChatBinding, LiveDetailHomeViewModel> {


    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_chat;
    }

    @Override
    public int initVariableId() {
        return 0;
    }

    @Override
    public LiveDetailHomeViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(LiveDetailHomeViewModel.class);
    }

    @Override
    public void initView() {

    }



}
