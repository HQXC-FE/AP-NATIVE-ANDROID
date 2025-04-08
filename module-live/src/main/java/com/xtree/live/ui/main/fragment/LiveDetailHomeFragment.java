package com.xtree.live.ui.main.fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.blankj.utilcode.util.DeviceUtils;
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
    private ViewPagerAdapter adapter;

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
        if(DeviceUtils.isTablet()){
            binding.tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        }else {
            binding.tabLayout.setTabGravity(TabLayout.GRAVITY_START);
        }

        adapter = new ViewPagerAdapter(requireActivity());
        // 添加三个指定的Fragment，并设置对应的标题
        GlobalRoom room = new GlobalRoom(RoomType.PAGE_CHAT_GLOBAL, mVid, "");
        room.uid = mUid;
        ChatFragment chatFragment1 = ChatFragment.newInstance(RoomType.PAGE_CHAT_GLOBAL, 0, mVid, ChatBarMode.CHATBAR_MODE_NONE, room, "", "");

        PrivateRoom pRoom = new PrivateRoom(RoomType.PAGE_CHAT_PRIVATE_ANCHOR, pVid(), "", 0);
        pRoom.isOnline = 0;
        ChatFragment chatFragment2 = ChatFragment.newInstance(RoomType.PAGE_CHAT_PRIVATE_ANCHOR, mUid, pVid(), ChatBarMode.CHATBAR_MODE_NONE, pRoom, "1", ""+mUid);

        ChatRoomContainerFragment chatFragment3 = ChatRoomContainerFragment.newInstance(ChatBarMode.CHATBAR_MODE_LOW, "2", "" + mUid);

        Fragment betFragment= (Fragment) ARouter.getInstance().build(RouterFragmentPath.Live.PAGER_LIVE_BET).navigation();

        adapter.addFragment(chatFragment1, "广场");
        adapter.addFragment(betFragment, "投注");
        adapter.addFragment(chatFragment2, "主播私聊");
        adapter.addFragment(chatFragment3, "主播助理");

        binding.viewpager.setAdapter(adapter);

        binding.viewpager.setOffscreenPageLimit(fragmentTypes.size());
        binding.viewpager.setUserInputEnabled(false);

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

        new TabLayoutMediator(binding.tabLayout, binding.viewpager, (tab, position) -> {
            tab.setText(mTabList[position]);
        }).attach();

    }

    @Override
    public void onResume() {
        super.onResume();
        ChatWebSocketManager.getInstance().start();
    }

    private final SimpleMessageListener messageListener = new SimpleMessageListener() {
        @Override
        public void onReceiveUnreadMessage(MessageMsg message) {

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

    @Override
    public void onDestroyView() {
        ChatWebSocketManager.getInstance().unregisterMessageListener(messageListener);
        super.onDestroyView();
    }

    @Override
    public boolean onBackPressed() {
        return super.onBackPressed();
    }
}
