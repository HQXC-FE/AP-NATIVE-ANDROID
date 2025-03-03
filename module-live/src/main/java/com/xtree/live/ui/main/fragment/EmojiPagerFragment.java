package com.xtree.live.ui.main.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayoutMediator;
import com.xtree.live.BR;
import com.xtree.live.LiveConfig;
import com.xtree.live.R;
import com.xtree.live.data.factory.AppViewModelFactory;
import com.xtree.live.databinding.FragmentChatBinding;
import com.xtree.live.databinding.FragmentEmojiPaperBinding;
import com.xtree.live.inter.ChatView;
import com.xtree.live.inter.OnEmojiGifClickedObserver;
import com.xtree.live.message.ExpandLiveInfo;
import com.xtree.live.ui.main.viewmodel.LiveDetailHomeViewModel;
import com.xtree.live.uitl.WordUtil;

import java.util.ArrayList;
import java.util.List;

import io.github.rockerhieu.emojicon.EmojiRecyclerFragment;
import io.github.rockerhieu.emojicon.OnEmojiconBackspaceListener;
import io.github.rockerhieu.emojicon.OnEmojiconClickedListener;
import me.xtree.mvvmhabit.base.BaseFragment;

public class EmojiPagerFragment extends BaseFragment<FragmentEmojiPaperBinding, LiveDetailHomeViewModel> {

    public static EmojiPagerFragment newInstance(ArrayList<String> tabList) {

        Bundle args = new Bundle();
        args.putStringArrayList("tabList", tabList);
        EmojiPagerFragment fragment = new EmojiPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private List<String> mTabList;

    private @NonNull List<String> tabList() {
        List<String> tabList = getArguments().getStringArrayList("tabList");
        if (tabList == null) tabList = new ArrayList<>();
        return tabList;
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_emoji_paper;
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

    private Handler mHandler;

    @Override
    public void initView() {

        mTabList = tabList();
        if(mHandler == null){
            mHandler = new Handler(Looper.getMainLooper()){
                @Override
                public void handleMessage(@NonNull Message msg) {
                    super.handleMessage(msg);
                    //删除
                    if(msg.what == -1){
                        OnEmojiconBackspaceListener observer = getEmojiconBackspaceClickedListener(EmojiPagerFragment.this);
                        if(observer != null)observer.onEmojiconBackspaceClicked( binding.deleteEmojicon);
                        sendEmptyMessageDelayed(-1, 100);
                    }
                }
            };
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        binding.deleteEmojicon.setOnClickListener(v -> {
            mHandler.removeMessages(-1);
        });
        binding.deleteEmojicon.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                mHandler.sendEmptyMessage(-1);
                return false;
            }
            return false;
        });

        binding.viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                resetBackPressIcon(position);
            }
        });
        binding.viewpager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                if (WordUtil.getString(R.string.emoji_text).equals(mTabList.get(position)))
                    return EmojiRecyclerFragment.newInstance(true);
                if (WordUtil.getString(R.string.emoji_gif).equals(mTabList.get(position)))
                    return EmojiGifFragment.newInstance();
                return new Fragment();
            }

            @Override
            public int getItemCount() {
                return mTabList.size();
            }
        });
        LayoutInflater layoutInflater = getLayoutInflater();
        TabLayoutMediator mediator = new TabLayoutMediator(binding.tabLayout, binding.viewpager, (tab, position) -> {
            View tabItem = layoutInflater.inflate(R.layout.tabview_customer_tab, null, false);
            String tabText = mTabList.get(position);
            int visible = LiveConfig.isLogin() && WordUtil.getString(R.string.emoji_gif).equals(tabText) ? View.VISIBLE : View.INVISIBLE;
            tabItem.findViewById(R.id.emoji_tab_vip).setVisibility(visible);
            tab.setCustomView(tabItem);
            tab.setText(tabText);
        });
        mediator.attach();

        int currentIndex = binding.viewpager.getCurrentItem();
        if(mTabList.size()> 0){
            resetBackPressIcon(currentIndex);
        }
    }

    private void resetBackPressIcon(int position) {
        if(WordUtil.getString(R.string.emoji_text).equals(mTabList.get(position))){
            binding.deleteEmojicon.setVisibility(View.VISIBLE);
        }else {
            binding.deleteEmojicon.setVisibility(View.GONE);
        }
    }

    public @Nullable OnEmojiconBackspaceListener getEmojiconBackspaceClickedListener(Fragment fragment) {
        Fragment parentFragment = fragment.getParentFragment();
        if (parentFragment != null) {
            if (parentFragment instanceof OnEmojiconBackspaceListener) {
                return (OnEmojiconBackspaceListener) parentFragment;
            } else {
                return getEmojiconBackspaceClickedListener(parentFragment);
            }
        } else {
            if (fragment.requireActivity() instanceof OnEmojiGifClickedObserver) {
                return (OnEmojiconBackspaceListener) fragment.requireActivity();
            } else {
                return null;
            }
        }
    }

    @Override
    public void onDestroyView() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroyView();
    }

}
