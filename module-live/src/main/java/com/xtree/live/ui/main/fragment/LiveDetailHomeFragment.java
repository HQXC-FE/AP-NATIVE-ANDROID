package com.xtree.live.ui.main.fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.live.BR;
import com.xtree.live.R;
import com.xtree.live.data.factory.AppViewModelFactory;
import com.xtree.live.databinding.FragmentLiveDetailHomeBinding;
import com.xtree.live.inter.LiveDetailMainView;
import com.xtree.live.inter.PVid;
import com.xtree.live.inter.UnreadChanged;
import com.xtree.live.message.ChatBarMode;
import com.xtree.live.message.ExpandLiveInfo;
import com.xtree.live.message.MessageMsg;
import com.xtree.live.message.RoomType;
import com.xtree.live.message.SimpleMessageListener;
import com.xtree.live.message.inroom.GlobalRoom;
import com.xtree.live.message.inroom.PrivateRoom;
import com.xtree.live.socket.ChatWebSocketManager;
import com.xtree.live.ui.main.viewmodel.LiveDetailHomeViewModel;
import com.xtree.live.uitl.ActionGetter;
import com.xtree.live.uitl.UnreadUtils;
import com.xtree.live.uitl.WordUtil;

import java.util.ArrayList;

import me.xtree.mvvmhabit.base.BaseFragment;

public class LiveDetailHomeFragment extends BaseFragment<FragmentLiveDetailHomeBinding, LiveDetailHomeViewModel> implements LiveDetailMainView, ExpandLiveInfo, UnreadChanged, PVid {

    private int mUid, matchType, matchId;
    private String mVid, pVid;
    private final String[] mTabList = {"广场", "投注","主播私聊","主播助理"};
    private final int[] tabIcons = {R.drawable.tab_chat_home_selector, R.drawable.tab_chat_home_selector};

    public static LiveDetailHomeFragment newInstance(int anchorId, String vId, String privateVid, int matchType, int matchId) {
        LiveDetailHomeFragment fragment = new LiveDetailHomeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("uid", anchorId);
        bundle.putString("vid", vId);
        bundle.putString("pVid", privateVid);
        bundle.putInt("match_type", matchType);
        bundle.putInt("match_id", matchId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().setTheme(R.style.Theme_MaterialComponents_DayNight_NoActionBar);
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_live_detail_home;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public LiveDetailHomeViewModel initViewModel() {
        com.xtree.live.data.factory.AppViewModelFactory factory = AppViewModelFactory.getInstance(requireActivity().getApplication());
        return new ViewModelProvider(this, factory).get(LiveDetailHomeViewModel.class);
    }

    @Override
    public void initView() {
        if (getArguments() != null) {
            mUid = getArguments().getInt("uid");
            mVid = getArguments().getString("vid");
            pVid = getArguments().getString("pVid");
            matchType = getArguments().getInt("match_type");
            matchId = getArguments().getInt("match_id");
        }
        ChatWebSocketManager.getInstance().registerMessageListener(messageListener);

        initViewPager2();

    }

    private void initViewPager2() {
        ArrayList<String> fragmentTypes = new ArrayList<>();

        fragmentTypes.add("chat_global");//广场
        fragmentTypes.add("bet_fragment");//投注
        fragmentTypes.add("chat_private");//主播私聊
        fragmentTypes.add("chat_list");//聊天 主播助理
        binding.viewpager.setOffscreenPageLimit(fragmentTypes.size());
        binding.viewpager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (fragmentTypes.get(position)) {
                    case "chat_global"://广场
                        GlobalRoom room = new GlobalRoom(RoomType.PAGE_CHAT_GLOBAL, mVid, "");
                        room.uid = mUid;
                        return ChatFragment.newInstance(RoomType.PAGE_CHAT_GLOBAL, mUid, mVid, ChatBarMode.CHATBAR_MODE_NONE, room, "", "");
                    case "chat_private"://主播私聊
                        PrivateRoom pRoom = new PrivateRoom(RoomType.PAGE_CHAT_PRIVATE_ANCHOR, pVid(), "", 0);
                        pRoom.isOnline = 0;
                        return ChatFragment.newInstance(RoomType.PAGE_CHAT_PRIVATE_ANCHOR, mUid, pVid(), ChatBarMode.CHATBAR_MODE_NONE, pRoom, "1", ""+mUid);

                    case "chat_list"://聊天 主播助理
                        return ChatRoomContainerFragment.newInstance(ChatBarMode.CHATBAR_MODE_LOW, "2", "" + mUid);
                    case "bet_fragment"://投注
                        return (Fragment) (BaseFragment) ARouter.getInstance().build(RouterFragmentPath.Live.PAGER_LIVE_BET).navigation();
                }
                return new Fragment();
            }

            @Override
            public int getItemCount() {
                return fragmentTypes.size();
            }

            @Override
            public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
                super.onAttachedToRecyclerView(recyclerView);
                recyclerView.setItemViewCacheSize(fragmentTypes.size());
            }
        });

        binding.viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//        if (binding.tabLayout.getSelectedTabPosition() != -1) {
//            binding.tabLayout.getTabAt(binding.tabLayout.getSelectedTabPosition()).getText().toString();
//        }
        TabLayoutMediator mediator = new TabLayoutMediator(binding.tabLayout, binding.viewpager, false, false, (tab, position) -> {

            // 为每个Tab创建自定义视图
            View customTabView = LayoutInflater.from(requireContext()).inflate(R.layout.tab_item_chat_title, null);
            ImageView tabIcon = customTabView.findViewById(R.id.tab_icon);
            TextView tabText = customTabView.findViewById(R.id.tab_text);
            tabText.setText(mTabList[position]);

            // 设置文本颜色选择器
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tabText.setTextColor(getResources().getColorStateList(R.color.tab_text_color_selector, null));
            } else {
                tabText.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.tab_text_color_selector));
            }

            // 设置自定义视图到Tab
            tab.setCustomView(customTabView);

        });
        mediator.attach();

    }

    @Override
    public void onResume() {
        super.onResume();
//        unreadChanged();
        ChatWebSocketManager.getInstance().start();
    }

    private final SimpleMessageListener messageListener = new SimpleMessageListener() {
        @Override
        public void onReceiveUnreadMessage(MessageMsg message) {
//            if(message.getIsVir() == 1){//虚拟房间
//                if (RoomType.PAGE_CHAT_PRIVATE == message.getRoomType() && AppConfig.isNotificationBeepOn()) {
//                    LogUtil.d(TAG, "-------play new msg----");
//                    MediaUtil.playNotificationBeep(AppManager.getContext(), R.raw.strong_notification);
//                }
//            }
            onGetUnreadCount(UnreadUtils.calculateTotal(), Math.abs(UnreadUtils.getUnreadCount(pVid())));
        }
    };


    @Override
    public void showLiveInfo() {
        ExpandLiveInfo expand = ActionGetter.getExpandLiveInfo(this);
        if (expand != null) expand.showLiveInfo();
        binding.tabLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLiveInfo() {
        ExpandLiveInfo expand = ActionGetter.getExpandLiveInfo(this);
        if (expand != null) expand.hideLiveInfo();
        binding.tabLayout.setVisibility(View.GONE);
    }

    @Override
    public void onGetUnreadCount(int total, int pCount) {
        for (int i = 0; i < binding.tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = binding.tabLayout.getTabAt(i);
            if (tab == null) return;
            if (TextUtils.isEmpty(tab.getText())) return;
            String tabText = tab.getText().toString();
            if (WordUtil.getString(R.string.chat_private_with_host).equals(tabText)) {
                showBadge(tab, pCount);
            } else if (WordUtil.getString(R.string.live_chat).equals(tabText)) {
                showBadge(tab, total);
            }
        }
    }

    @Override
    public String pVid() {
        return pVid;
    }

    @Override
    public void setPVid(String pVid) {
        getArguments().putString("pVid", pVid);
    }

    @Override
    public void unreadChanged() {
        onGetUnreadCount(UnreadUtils.calculateTotal(), UnreadUtils.getUnreadCount(pVid()));
    }

    private void showBadge(TabLayout.Tab tab, int total) {
        BadgeDrawable badge = tab.getOrCreateBadge();
        badge.setMaxCharacterCount(3);
        badge.setBadgeGravity(BadgeDrawable.TOP_END);
        if (total == 0) {
            badge.setVisible(false);
        } else {
            int displayCount = UnreadUtils.showRealUnreadCount(total);
            badge.setBadgeTextColor(Color.TRANSPARENT);
            badge.setVisible(true);
            badge.setNumber(displayCount);
        }
    }

}
