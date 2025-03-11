package com.xtree.live.ui.main.fragment;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.xtree.live.BR;
import com.xtree.live.R;
import com.xtree.live.data.factory.AppViewModelFactory;
import com.xtree.live.databinding.FragmentEmojiPaperBinding;
import com.xtree.live.inter.OnEmojiGifClickedObserver;
import com.xtree.live.ui.main.viewmodel.LiveDetailHomeViewModel;
import com.xtree.live.uitl.WordUtil;

import io.github.rockerhieu.emojicon.EmojiRecyclerFragment;
import io.github.rockerhieu.emojicon.OnEmojiconBackspaceListener;
import me.xtree.mvvmhabit.base.BaseFragment;

public class EmojiPagerFragment extends BaseFragment<FragmentEmojiPaperBinding, LiveDetailHomeViewModel> {

    public static EmojiPagerFragment newInstance() {
        return new EmojiPagerFragment();
    }

    private final String[] mTabList = {"快捷回复", "表情"};
    private final int[] tabIcons = {R.drawable.tab_quicktext_selector, R.drawable.tab_emoji_selector};

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

        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(@NonNull Message msg) {
                    super.handleMessage(msg);
                    //删除
                    if (msg.what == -1) {
                        OnEmojiconBackspaceListener observer = getEmojiconBackspaceClickedListener(EmojiPagerFragment.this);
                        if (observer != null)
                            observer.onEmojiconBackspaceClicked(binding.deleteEmojicon);
                        sendEmptyMessageDelayed(-1, 100);
                    }
                }
            };
        }

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
                if (WordUtil.getString(R.string.emoji_text).equals(mTabList[position]))
                    return EmojiRecyclerFragment.newInstance(true);
                if (WordUtil.getString(R.string.emoji_gif).equals(mTabList[position]))
                    return QuickReplyFragment.newInstance();
                return new Fragment();
            }

            @Override
            public int getItemCount() {
                return mTabList.length;
            }
        });

        new TabLayoutMediator(binding.tabLayout, binding.viewpager, (tab, position) -> {
            // 为每个Tab创建自定义视图
            View customTabView = LayoutInflater.from(requireContext()).inflate(R.layout.tab_item_layout, null);
            ImageView tabIcon = customTabView.findViewById(R.id.tab_icon);
            TextView tabText = customTabView.findViewById(R.id.tab_text);

            // 设置图标和文本
            tabIcon.setImageResource(tabIcons[position]);
            tabText.setText(mTabList[position]);

            // 设置文本颜色选择器
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tabText.setTextColor(getResources().getColorStateList(R.color.tab_text_color_selector, null));
            } else {
                tabText.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.tab_text_color_selector));
            }
            // 设置背景选择器
            customTabView.setBackground(getResources().getDrawable(R.drawable.tab_background_selector, null));

            // 设置自定义视图到Tab
            tab.setCustomView(customTabView);
        }).attach();

        // 添加Tab选择监听器以手动更新状态
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                updateTabAppearance(tab, true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                updateTabAppearance(tab, false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // 不需要额外操作
            }
        });

        // 默认选择第一个Tab
        updateTabAppearance(binding.tabLayout.getTabAt(0), true);

    }

    private void updateTabAppearance(TabLayout.Tab tab, boolean isSelected) {
        if (tab != null && tab.getCustomView() != null) {
            View customView = tab.getCustomView();
            ImageView tabIcon = customView.findViewById(R.id.tab_icon);
            TextView tabText = customView.findViewById(R.id.tab_text);

            // 更新UI状态
            tabIcon.setSelected(isSelected);
            tabText.setSelected(isSelected);
            customView.setSelected(isSelected);

            // 调整选中状态的额外样式
            if (isSelected) {
                tabText.setTextSize(16);  // 选中时文字稍大
            } else {
                tabText.setTextSize(14);  // 未选中时文字常规大小
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.deleteEmojicon.setOnClickListener(v -> {
            mHandler.removeMessages(-1);
        });
        binding.deleteEmojicon.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mHandler.sendEmptyMessage(-1);
                return false;
            }
            return false;
        });

    }

    private void resetBackPressIcon(int position) {
        if (WordUtil.getString(R.string.emoji_text).equals(mTabList[position])) {
            binding.deleteEmojicon.setVisibility(View.VISIBLE);
        } else {
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
