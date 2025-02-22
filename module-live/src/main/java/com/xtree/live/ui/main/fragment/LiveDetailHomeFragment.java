package com.xtree.live.ui.main.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.live.BR;
import com.xtree.live.R;
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
import com.xtree.live.uitl.ActionGetter;
import com.xtree.live.uitl.UnreadUtils;
import com.xtree.live.uitl.WordUtil;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.base.BaseViewModel;

public class LiveDetailHomeFragment extends BaseFragment<FragmentLiveDetailHomeBinding, BaseViewModel> implements LiveDetailMainView, ExpandLiveInfo, UnreadChanged, PVid {

    private int mUid, matchType, matchId;
    private String mVid, pVid;
    private int mCurrentPosition;

    private FragmentManager childFragmentManager;

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
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_live_detail_home;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
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
        initTab();

    }

    @Override
    public void onResume() {
        super.onResume();
        unreadChanged();
    }

    private void initTab() {
        String[] tabTitles = getResources().getStringArray(R.array.live_title);
        for (int i = 0; i < tabTitles.length; i++) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(tabTitles[i]).setTag(i));
        }

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mCurrentPosition = tab.getPosition();
                replaceFragment(mCurrentPosition);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void replaceFragment(int mCurrentPosition) {
        if (childFragmentManager == null) {
            childFragmentManager = getChildFragmentManager();
        }
        FragmentTransaction fragmentTransaction = childFragmentManager.beginTransaction();

        // 顺序是 广场-投注-主播私聊-主播助理
        //对应的fragment分别是 chatFragment bet
        if (mCurrentPosition == 1) {
            Fragment liveBetFragment = (BaseFragment) ARouter.getInstance().build(RouterFragmentPath.Live.PAGER_LIVE_BET).navigation();
            fragmentTransaction.replace(R.id.home_content, liveBetFragment);
        } else if (mCurrentPosition == 3) {
            Fragment rechargeFragment = (BaseFragment) new Fragment();
            fragmentTransaction.replace(R.id.home_content, rechargeFragment);
        } else if (mCurrentPosition == 2) {// 主播私聊
            PrivateRoom pRoom = new PrivateRoom(RoomType.PAGE_CHAT_PRIVATE_ANCHOR, pVid(), "", 0);
            pRoom.isOnline = 0;

            Fragment chatFragment = (BaseFragment) ARouter.getInstance()
                    .build(RouterFragmentPath.Live.PAGER_LIVE_CHAT)
                    .withInt("roomType", RoomType.PAGE_CHAT_PRIVATE_ANCHOR)
                    .withInt("uid", mUid)
                    .withString("vid", pVid())
                    .withInt("chatBarMode", ChatBarMode.CHATBAR_MODE_NONE)
                    .withParcelable("roomInfo", pRoom)
                    .withString("pm_source_type", "1")
                    .withString("pm_source_type_str", "" + mUid)
                    .navigation();
            fragmentTransaction.replace(R.id.home_content, chatFragment);
        } else {//广场
            GlobalRoom room = new GlobalRoom(RoomType.PAGE_CHAT_GLOBAL, mVid, "");
            room.uid = mUid;
            Fragment chatFragment = (BaseFragment) ARouter.getInstance().
                    build(RouterFragmentPath.Live.PAGER_LIVE_CHAT)
                    .withInt("roomType", RoomType.PAGE_CHAT_PRIVATE_ANCHOR)
                    .withInt("uid", mUid)
                    .withString("vid", mVid)
                    .withInt("chatBarMode", ChatBarMode.CHATBAR_MODE_NONE)
                    .withParcelable("roomInfo", room)
                    .withString("pm_source_type", "")
                    .withString("pm_source_type_str", "")
                    .navigation();

            fragmentTransaction.replace(R.id.home_content, chatFragment);
        }


    }

    private final SimpleMessageListener messageListener = new SimpleMessageListener(){
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
        if(expand != null)expand.showLiveInfo();
        binding.tabLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLiveInfo() {
        ExpandLiveInfo expand = ActionGetter.getExpandLiveInfo(this);
        if(expand != null)expand.hideLiveInfo();
        binding.tabLayout.setVisibility(View.GONE);
    }

    @Override
    public void onGetUnreadCount(int total, int pCount) {
        for (int i = 0; i < binding.tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = binding.tabLayout.getTabAt(i);
            if(tab ==null)return;
            if(TextUtils.isEmpty(tab.getText()))return;
            String tabText = tab.getText().toString();
            if(WordUtil.getString(R.string.chat_private_with_host).equals(tabText)){
                showBadge(tab, pCount);
            }else if(WordUtil.getString(R.string.live_chat).equals(tabText)){
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
