package com.xtree.live.ui.main.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.material.tabs.TabLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.bet.BR;
import com.xtree.live.R;
import com.xtree.live.data.source.response.LiveRoomBean;
import com.xtree.live.databinding.ActivityLiveDetailBinding;
import com.xtree.live.ui.main.fragment.LiveDetailHomeFragment;
import com.xtree.live.ui.main.viewmodel.LiveViewModel;

import me.xtree.mvvmhabit.base.BaseActivity;
import me.xtree.mvvmhabit.base.BaseFragment;

@Route(path = RouterFragmentPath.Live.LIVE_DETAIL)
public class LiveDetailActivity extends BaseActivity<ActivityLiveDetailBinding, LiveViewModel> implements OnRefreshLoadMoreListener, View.OnClickListener {

    @Autowired(name = "matchID")
    String matchId;
    @Autowired(name = "uid", required = true)
    int uid;
    @Autowired(name = "vid")
    String vid;

    private int mType = -1;
    private int mCurrentPosition;
    LiveDetailHomeFragment mLiveDetailFragment;
    private FragmentManager childFragmentManager;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_live_detail;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initView() {

//        initTab();
        initPresent();
        initObservable();
    }

    private void initPresent() {
        //获取当前直播间详情
        viewModel.getRoomInfo(uid);
    }

    /*private void initTab() {
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
    }*/

   /* private void replaceFragment(int mCurrentPosition) {
        if(childFragmentManager == null){
            childFragmentManager = getCh
        }
        // 顺序是 广场-投注-主播私聊-主播助理
        //对应的fragment分别是 chatFragment bet
        if(mCurrentPosition==1){
            Fragment liveBetFragment = (BaseFragment) ARouter.getInstance().build(RouterFragmentPath.Live.PAGER_LIVE_BET).navigation();
        } else if(mCurrentPosition == 3 ){
            Fragment rechargeFragment = (BaseFragment)new Fragment();
        } else {
            Fragment chatFragment = (BaseFragment) ARouter.getInstance().build(RouterFragmentPath.Live.PAGER_LIVE_CHAT).navigation();
        }


    }*/

    private void initObservable() {
        //首先获取到直播间的信息，这个直播详情也需要这个数据
        viewModel.liveRoomInfo.observe(this, liveRoomBean -> {
            if(liveRoomBean.getInfo()!=null){
                int matchId = liveRoomBean.getInfo().getMatch_id();
                //type -1 全部  直播  热门 足球  篮球 关注 其他
                mType = liveRoomBean.getInfo().getType();
                //privateVid 私有 私密
                mLiveDetailFragment = LiveDetailHomeFragment.newInstance(uid,vid,"",mType,matchId);
                getSupportFragmentManager().beginTransaction().replace(R.id.main_page, mLiveDetailFragment).commitAllowingStateLoss();

            }

        });
    }


    @Override
    public void onClick(View view) {
        binding.buttonBack.setOnClickListener(l->{
            finish();
        });

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

    }
}
