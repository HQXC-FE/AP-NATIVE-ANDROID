package com.xtree.live.ui.main.bet;

import static com.xtree.base.utils.BtDomainUtil.KEY_PLATFORM;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_PM;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_PMXC;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gyf.immersionbar.ImmersionBar;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.live.X9LiveInfo;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.DomainUtil;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.constant.Constants;
import com.xtree.bet.ui.viewmodel.TemplateBtDetailViewModel;
import com.xtree.bet.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.bet.ui.viewmodel.factory.PMAppViewModelFactory;
import com.xtree.bet.ui.viewmodel.fb.FbBtDetailViewModel;
import com.xtree.bet.ui.viewmodel.pm.PmBtDetailViewModel;
import com.xtree.bet.util.MatchDeserializer;
import com.xtree.live.BR;
import com.xtree.live.R;
import com.xtree.live.ui.main.model.constant.DetailLivesType;
import com.xtree.service.WebSocketService;
import com.xtree.service.message.MessageData;
import com.xtree.service.message.MessageType;
import com.xtree.service.message.PushServiceConnection;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.sentry.Sentry;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * Created by Vickers on 2024/11/24
 */
public class LiveMatchDetailActivity extends LiveGSYBaseActivityDetail<StandardGSYVideoPlayer> implements View.OnClickListener {
    private final static String KEY_MATCH = "KEY_MATCH_ID";
    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private ArrayList<String> tabList = new ArrayList<>();
    private FragmentStateAdapter mAdapter;
    private PushServiceConnection pushServiceConnection;
    private Observer<Object> pushObserver;

    private Match mMatch;

    private int tabPos;

    private String mPlatform = SPUtils.getInstance().getString(KEY_PLATFORM);
    private static int mMatchID = -1;

    public Match getmMatch() {
        return mMatch;
    }

    @Override
    public void initView() {
        boolean isLogin = getIntent().getBooleanExtra("isLogin", false);
        Fragment homeFragment = new Fragment();
        Fragment liveBetFragment = (Fragment) ARouter.getInstance().build(RouterFragmentPath.Live.PAGER_LIVE_BET).navigation();
        Fragment liveFragment = new Fragment();
        Fragment rechargeFragment = new Fragment();
        fragmentList.add(homeFragment);
        fragmentList.add(liveBetFragment);
        fragmentList.add(liveFragment);
        fragmentList.add(rechargeFragment);

        mAdapter = new FragmentStateAdapter(getSupportFragmentManager(), getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getItemCount() {
                return fragmentList.size();
            }
        };

        binding.vpMain.setAdapter(mAdapter);
        binding.vpMain.setUserInputEnabled(true); // ViewPager2 左右滑动

        String txtSquare = DetailLivesType.SQUARE.getLabel();
        String txtBetting = DetailLivesType.BET.getLabel();
        String txtPrivate = DetailLivesType.ANCHOR_PRIVATE.getLabel();
        String txtMsgAssistant = DetailLivesType.ANCHOR_ASSISTANT.getLabel();

        tabList.add(txtSquare);
        tabList.add(txtBetting);
        tabList.add(txtPrivate);
        tabList.add(txtMsgAssistant);

        new TabLayoutMediator(binding.tblType, binding.vpMain, (tab, position) -> {
            tab.setText(tabList.get(position));
        }).attach();
        mAdapter.notifyDataSetChanged();

        initVideoPlayer();
        initPushService();
    }

    public static void start(Context context, int matchID) {
        Intent intent = new Intent(context, LiveMatchDetailActivity.class);
        //SPUtils.getInstance().put(KEY_MATCH, new Gson().toJson(matchID));
        //intent.putExtra(KEY_MATCH, match);
        mMatchID = matchID;
        context.startActivity(intent);
    }

    @Override
    public void initData() {
        System.out.println("=============== LiveMatchDetail MatchID ================"+mMatchID);
        viewModel.getMatchDetail(mMatchID);
        //viewModel.getCategoryList(String.valueOf(mMatch.getId()), mMatch.getSportId());
        viewModel.addSubscription();
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_bt_layout_detail;
    }

    @Override
    public int initVariableId() {
        return BR.model;
    }

    @Override
    public TemplateBtDetailViewModel initViewModel() {
        if (!TextUtils.equals(mPlatform, PLATFORM_PM) && !TextUtils.equals(mPlatform, PLATFORM_PMXC)) {
            com.xtree.bet.ui.viewmodel.factory.AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
            return new ViewModelProvider(this, factory).get(FbBtDetailViewModel.class);
        } else {
            PMAppViewModelFactory factory = PMAppViewModelFactory.getInstance(getApplication());
            return new ViewModelProvider(this, factory).get(PmBtDetailViewModel.class);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == com.xtree.bet.R.id.rl_cg) {

        } else if (id == com.xtree.bet.R.id.tv_live) {
            binding.videoPlayer.setVisibility(View.VISIBLE);
            binding.ctlToolbarLeague.setVisibility(View.GONE);
            binding.rlToolbarTime.setVisibility(View.GONE);
            if (mMatch.isVideoStart()) {
                initVideoBuilderMode();
            } else {
                ToastUtils.showLong(getText(com.xtree.bet.R.string.bt_bt_match_not_runing));
            }
        } else if (id == com.xtree.bet.R.id.iv_back) {
            finish();
        }
    }

    private void initPushService() {
        Messenger replyMessenger = new Messenger(new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                MessageType.Output outputType = MessageType.Output.fromCode(msg.what);
                switch (outputType) {
                    case OBTAIN_LINK:
                        if (!TextUtils.isEmpty(SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN))) {
                            //viewModel.getWebsocket();
                        }
                        break;
                    case REMOTE_MSG://后端的消息
                        if (msg.getData() != null) {
                            CfLog.i("receiving class: " + MessageData.class.getName());
                            try {
                                msg.getData().setClassLoader(getClassLoader());
                                //                                getActivity().handleRemoteMessage(msg.getData().getParcelable("data"));
                            } catch (Exception e) {
                                e.printStackTrace();
                                Sentry.captureException(e);
                            }
                        }
                        break;
                }
            }
        });
        pushServiceConnection = new PushServiceConnection(replyMessenger);
        // 绑定 Service
        Intent intent = new Intent(this, WebSocketService.class);
        this.bindService(intent, pushServiceConnection, Context.BIND_AUTO_CREATE);


        //&& !TextUtils.isEmpty(wsToken.getToken())) {
        //                String token = wsToken.getToken();
        long checkInterval = SPUtils.getInstance().getLong(SPKeyGlobal.WS_CHECK_INTERVAL, 30);
        long retryNumber = SPUtils.getInstance().getLong(SPKeyGlobal.WS_RETRY_NUMBER, 3);
        long retryWaitingTime = SPUtils.getInstance().getLong(SPKeyGlobal.WS_RETRY_WAITING_TIME, 300);
        long expireTime = SPUtils.getInstance().getLong(SPKeyGlobal.WS_EXPIRE_TIME, 90);

        //分隔符
        String separator;
        if (DomainUtil.getApiUrl().endsWith("/")) {
            separator = "";
        } else {
            separator = File.separator;
        }
        String url = DomainUtil.getApiUrl() + separator + "wss/?xLiveToken=" + X9LiveInfo.INSTANCE.getToken();

        //协议转换
        if (url.startsWith("https")) {
            url = url.replaceFirst("https", "wss");
        } else if (url.startsWith("http")) {
            url = url.replaceFirst("http", "ws");
        }
        CfLog.e(url);
        Bundle obj = new Bundle();
        obj.putString("url", url);
        obj.putLong("checkInterval", checkInterval);
        obj.putLong("retryNumber", retryNumber);
        obj.putLong("retryWaitingTime", retryWaitingTime);
        obj.putLong("expireTime", expireTime);
        pushServiceConnection.sendMessageToService(MessageType.Input.LINK, obj);
    }

    /**
     * 设置顶部背景图
     */
    private void setTopBg() {
        if (mMatch != null && mMatch.getSportId() != null) {
            binding.ctlBg.setBackgroundResource(Constants.getBgMatchDetailTop(mMatch.getSportId()));
        }
    }
    /**
     * 初始化播放器相关控件
     */
    private void initVideoPlayer() {
        //增加title
        binding.tvLive.setOnClickListener(this);
        binding.tvAnimi.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
    }

    @Override
    public StandardGSYVideoPlayer getGSYVideoPlayer() {
        return binding.videoPlayer;
    }

    @Override
    public GSYVideoOptionBuilder getGSYVideoOptionBuilder() {
         String videoUrl = "";
        if (!mMatch.getVideoUrls().isEmpty()) {
            videoUrl = mMatch.getVideoUrls().get(0);
        }
        String score = "";
        List<Integer> scoreList = mMatch.getScore(Constants.getScoreType());

        if (scoreList != null && scoreList.size() > 1) {
            score = scoreList.get(0) + " - " + scoreList.get(1);
        }
        ImageView thumb = new ImageView(this);
        if (mMatch != null) {
            thumb.setBackgroundResource(Constants.getBgMatchDetailTop(mMatch.getSportId()));
        }
        Map header = new HashMap();
        if (!TextUtils.isEmpty(mMatch.getReferUrl())) {
            header.put("Referer", mMatch.getReferUrl());
        }
        return new GSYVideoOptionBuilder()
                .setThumbImageView(thumb)
                .setUrl(videoUrl)
                .setMapHeadData(header)
                .setCacheWithPlay(false)
                .setShrinkImageRes(com.xtree.bet.R.mipmap.bt_video_shrink)
                .setEnlargeImageRes(com.xtree.bet.R.mipmap.bt_video_enlarge)
                .setVideoTitle(mMatch.getTeamMain() + score + mMatch.getTeamVistor())
                .setIsTouchWiget(false)
                //.setAutoFullWithSize(true)
                .setRotateViewAuto(false)
                .setLockLand(false)
                .setShowFullAnimation(false)//打开动画
                .setNeedLockFull(false)
                .setSeekRatio(1);
    }

    @Override
    public void clickForFullScreen() {

    }

    @Override
    public boolean getDetailOrientationRotateAuto() {
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.addSubscribe(Observable.interval(5, 5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    viewModel.getMatchDetail(mMatch.getId());
                    //viewModel.getCategoryList(String.valueOf(mMatch.getId()), mMatch.getSportId());
                    /*if (!mCategories.isEmpty() && mCategories.size() > tabPos) {
                        viewModel.getCategoryList(String.valueOf(mMatch.getId()), mMatch.getSportId());
                    }*/
                })
        );
    }
}
