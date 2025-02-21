package com.xtree.live.ui.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.blankj.utilcode.util.DeviceUtils;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.xtree.live.BR;
import com.xtree.live.R;
import com.xtree.live.databinding.FragmentLiveDetailHomeBinding;
import com.xtree.live.message.RoomType;

import java.util.ArrayList;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.base.BaseViewModel;

public class LiveDetailHomeFragment extends BaseFragment<FragmentLiveDetailHomeBinding, BaseViewModel> {

    int

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
        initViewPager2();
    }

    private void initViewPager2() {
        ArrayList<String> fragmentTypes = new ArrayList<>();

        //-1 全部  0足球  1篮球  2 其他  3电竞
        int matchId = matchId();
        int matchType = matchType();
        if (matchType == MatchType.FOOTBALL) {
            fragmentTypes.add("football_status");
            fragmentTypes.add("football_predicate");
        } else if (matchType == MatchType.BASKETBALL) {
            fragmentTypes.add("basketball_status");
            fragmentTypes.add("basketball_predicate");
        } else if (matchType == MatchType.ESPORTS) {
            fragmentTypes.add("esport_predicate");
        }
        fragmentTypes.add("live_ranking");
        fragmentTypes.add("live_preview");
        if(DeviceUtils.isTablet()){
            mBinding.tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        }else {
            mBinding.tabLayout.setTabGravity(TabLayout.GRAVITY_START);
        }
        mBinding.viewpager.setOffscreenPageLimit(fragmentTypes.size());
        mBinding.viewpager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (fragmentTypes.get(position)) {
                    case "chat_global"://广场
                        GlobalRoom room = new GlobalRoom(RoomType.PAGE_CHAT_GLOBAL, vid(), "");
                        room.uid = uid();
                        return ChatFragment.newInstance(RoomType.PAGE_CHAT_GLOBAL, room.uid, room.vid, ChatBarMode.CHATBAR_MODE_NONE,room, "", "");
                    case "chat_private"://主播私聊
                        PrivateRoom pRoom = new PrivateRoom(RoomType.PAGE_CHAT_PRIVATE_ANCHOR, pVid(), "", 0);
                        pRoom.isOnline = 0;
                        return ChatFragment.newInstance(RoomType.PAGE_CHAT_PRIVATE_ANCHOR, uid(), pRoom.vid, ChatBarMode.CHATBAR_MODE_NONE, pRoom,"1", "" + uid());
                    case "chat_list"://聊天
                        return ChatRoomContainerFragment.newInstance(ChatBarMode.CHATBAR_MODE_LOW, "2", "" + uid());
                    case "football_status":
                    case "basketball_status":// 赛况
                        return MatchStatusFragment.newInstanceLive(matchType, matchId);
                    case "football_predicate":// 前瞻
                        return PredicateSportFragment.newInstance(202, matchId);
                    case "basketball_predicate":
                        return PredicateSportFragment.newInstance(201, matchId);
                    case "esport_predicate":
                        return ESportPredicateFragment.newInstance(1, matchId);
                    case "live_ranking":// 排行榜
                        return RankingViewPagerFragment.newInstance(uid());
                    case "live_preview":// 预告
                        return PreviewFragment.newInstance(uid());
                    case "live_activity"://活动
                        return ActivityFragment.newInstance(uid(), matchId());
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
        mBinding.viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });
        mBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

        TabLayoutMediator mediator = new TabLayoutMediator(mBinding.tabLayout, mBinding.viewpager,false, false,  (tab, position) -> {
            String fragmentType = fragmentTypes.get(position);
//            if ("chat_private".equals(fragmentType) || "chat_list".equals(fragmentType) ) {
//                BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
//                badgeDrawable.setVisible(false);
//                if ("chat_list".equals(fragmentType)) {
//                    showBadge(tab, totalUnread());
//                }
//                if ("chat_private".equals(fragmentType)) {
//                    showBadge(tab, privateUnread());
//                }
//            }
            String title = "";
            switch (fragmentType) {
                case "chat_global":
                    title = WordUtil.getString(R.string.chat_global);
                    break;
                case "chat_private":
                    title = WordUtil.getString(R.string.chat_private_with_host);
                    break;
                case "chat_list":
                    title = WordUtil.getString(R.string.live_chat);
                    break;
                case "football_status":
                case "basketball_status":
                    title = WordUtil.getString(R.string.match_status);
                    break;
                case "football_predicate":
                case "esport_predicate":
                case "basketball_predicate":
                    title = WordUtil.getString(R.string.predicte);
                    break;
                case "live_activity":
                    title = WordUtil.getString(R.string.live_activity);
                    break;
                case "live_ranking":
                    title = WordUtil.getString(R.string.live_ranking);
                    break;
                case "live_preview":
                    title = WordUtil.getString(R.string.live_preview);
                    break;

            }
            tab.setText(title);
        });
        mediator.attach();
    }

}
