package com.xtree.live.ui.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.xtree.live.R;
import com.xtree.live.data.factory.AppViewModelFactory;
import com.xtree.live.databinding.FragmentQuickReplayBinding;
import com.xtree.live.inter.OnEmojiGifClickedObserver;
import com.xtree.live.ui.main.adapter.QuickMessageAdapter;
import com.xtree.live.ui.main.viewmodel.LiveDetailHomeViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.xtree.mvvmhabit.BR;
import me.xtree.mvvmhabit.base.BaseFragment;

public class QuickReplyFragment extends BaseFragment<FragmentQuickReplayBinding, LiveDetailHomeViewModel> {


    private QuickMessageAdapter msgAdapter;
    private String quickMsg;

    private List<String> messageList = new ArrayList<>(Arrays.asList(
            "财运亨通一路发发发发发发发发😂😂",
            "客队必胜，客队牛通",
            "财运亨通一路发发发发发发发发😂😂",
            "收米，收米",
            "👍进球喊我下，我等收米",
            "我还没上车，啊哈😛",
            "比赛第一收钱第二，我预测这边稳了！！🐔"
    ));

    public static Fragment newInstance() {
        return new QuickReplyFragment();
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_quick_replay;
    }

    @Override
    public int initVariableId() {
        return BR.m;
    }

    @Override
    public LiveDetailHomeViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(LiveDetailHomeViewModel.class);
    }

    @Override
    public void initView() {

        binding.recycleView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (msgAdapter == null) {
            msgAdapter = new QuickMessageAdapter(messageList);
        }
        binding.recycleView.setAdapter(msgAdapter);
//        msgAdapter.setList(messageList);

        msgAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
               /* msgAdapter.setClickPosition(position);
                //当前选中的快捷回复短语
                quickMsg = msgAdapter.getData().get(position);
                OnEmojiGifClickedObserver observer = getEmojiGifClickedObserver(QuickReplyFragment.this);
                if (observer != null) {
                    observer.onQuickReplyClicked(quickMsg);
                }*/
            }
        });
    }

    public void updateMessages(List<String> newMessages) {
        messageList.clear();
        messageList.addAll(newMessages);
        msgAdapter.notifyDataSetChanged();
    }

    public @Nullable OnEmojiGifClickedObserver getEmojiGifClickedObserver(Fragment fragment) {
        Fragment parentFragment = fragment.getParentFragment();
        if (parentFragment != null) {
            if (parentFragment instanceof OnEmojiGifClickedObserver) {
                return (OnEmojiGifClickedObserver) parentFragment;
            } else {
                return getEmojiGifClickedObserver(parentFragment);
            }
        } else {
            if (fragment.requireActivity() instanceof OnEmojiGifClickedObserver) {
                return (OnEmojiGifClickedObserver) fragment.requireActivity();
            } else {
                return null;
            }
        }
    }

}
