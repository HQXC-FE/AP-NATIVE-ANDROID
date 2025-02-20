package com.xtree.live.ui.main.bet;

import static com.xtree.base.utils.BtDomainUtil.KEY_PLATFORM;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_FBXC;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_PM;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_PMXC;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.BtDomainUtil;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.utils.TimeUtils;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.constant.Constants;
import com.xtree.bet.ui.fragment.BtDetailOptionFragment;
import com.xtree.bet.ui.viewmodel.TemplateBtDetailViewModel;
import com.xtree.bet.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.bet.ui.viewmodel.factory.PMAppViewModelFactory;
import com.xtree.bet.ui.viewmodel.fb.FbBtDetailViewModel;
import com.xtree.bet.ui.viewmodel.pm.PmBtDetailViewModel;
import com.xtree.bet.weight.BaseDetailDataView;
import com.xtree.live.R;
import com.xtree.live.ui.main.model.constant.DetailLivesType;
import com.xtree.service.message.PushServiceConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * Created by Vickers on 2024/11/24
 */
public class LiveMatchDetailActivity extends LiveGSYBaseActivityDetail<StandardGSYVideoPlayer> implements View.OnClickListener {
    private final static String KEY_MATCH = "KEY_MATCH_ID";

    private BaseDetailDataView mScoreDataView;

    private BtDetailOptionFragment fragment;

    private Match mMatch;

    private static Long mMatchID = -1L;

    private int tabPos;

    private String mPlatform = SPUtils.getInstance().getString(KEY_PLATFORM);

    private ArrayList<Fragment> fragmentList = new ArrayList<>();

    private ArrayList<String> tabList = new ArrayList<>();

    private FragmentStateAdapter mAdapter;

    private PushServiceConnection pushServiceConnection;
    private LiveFloatingWindows mLiveFloatingWindows;

    //private BettingNetFloatingWindows mBettingNetFloatingWindows;

    public Match getmMatch() {
        return mMatch;
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_bt_layout_detail;
    }

    @Override
    public int initVariableId() {
        return com.xtree.bet.BR.viewModel;
    }

    /**
     * 初始化沉浸式
     * Init immersion bar.
     */
    protected void initImmersionBar() {
        //设置共同沉浸式样式
        ImmersionBar.with(this)
                .navigationBarColor(me.xtree.mvvmhabit.R.color.default_navigation_bar_color)
                .fitsSystemWindows(false)
                .statusBarDarkFont(false)
                .init();
    }

    public static void start(Context context, Long matchID) {
        Intent intent = new Intent(context, LiveMatchDetailActivity.class);
        //SPUtils.getInstance().put(KEY_MATCH, new Gson().toJson(match));
        System.out.println("=============== LiveMatchDetailActivity matchID ================"+matchID);
        mMatchID = matchID;
//        try{
//            int id = Integer.parseInt(matchID);
//            mMatchID = id;
//        } catch(NumberFormatException ex){ // handle your exception
//            ex.printStackTrace();
//        }
        System.out.println("=============== LiveMatchDetailActivity mMatchID ================"+mMatchID);
        //intent.putExtra(KEY_MATCH, match);
        context.startActivity(intent);
    }

    public static void start(Context context, String  matchID) {
        Intent intent = new Intent(context, LiveMatchDetailActivity.class);
        //SPUtils.getInstance().put(KEY_MATCH, new Gson().toJson(match));
        //intent.putExtra(KEY_MATCH, match);
        context.startActivity(intent);
    }

    @Override
    public TemplateBtDetailViewModel initViewModel() {
        if (!TextUtils.equals(mPlatform, PLATFORM_PM) && !TextUtils.equals(mPlatform, PLATFORM_PMXC)) {
            AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
            return new ViewModelProvider(this, factory).get(FbBtDetailViewModel.class);
        } else {
            PMAppViewModelFactory factory = PMAppViewModelFactory.getInstance(getApplication());
            return new ViewModelProvider(this, factory).get(PmBtDetailViewModel.class);
        }
    }


    @Override
    public void initView() {
        binding.appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {// 收缩状态
                binding.rlToolbarTime.setVisibility(View.VISIBLE);
                binding.ctlToolbarLeague.setVisibility(View.GONE);
                binding.llLive.setVisibility(View.GONE);
                binding.llData.setVisibility(View.GONE);
                binding.toolbar.setBackgroundResource(com.xtree.bet.R.color.bt_color_detail_top_toolbar);
            } else if (Math.abs(verticalOffset) == 0) {//展开
                binding.rlToolbarTime.setVisibility(View.GONE);
                if (binding.videoPlayer.getVisibility() != View.VISIBLE && binding.wvAmin.getVisibility() != View.VISIBLE) {
                    binding.ctlToolbarLeague.setVisibility(View.VISIBLE);
                } else {
                    binding.ctlToolbarLeague.setVisibility(View.GONE);
                }
                binding.llLive.setVisibility(View.VISIBLE);
                binding.llData.setVisibility(View.VISIBLE);
                binding.toolbar.setBackgroundResource(android.R.color.transparent);
            } else {
                binding.llLive.setVisibility(View.VISIBLE);
                binding.llData.setVisibility(View.VISIBLE);
            }
        });

        initFragmet();
        initVideoPlayer();
        setWebView();
        initLiveFloatWindows();
    }

    @Override
    public void initData() {
        System.out.println("=============== initData LiveMatchDetail MatchID ================"+mMatchID);
        viewModel.getMatchDetail(mMatchID);
        viewModel.addSubscription();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 设置顶部背景图
     */
    private void setTopBg() {
        try{
            if (mMatch != null && mMatch.getSportId() != null) {
                binding.ctlBg.setBackgroundResource(Constants.getBgMatchDetailTop(mMatch.getSportId()));
            }
        }catch (Exception e){
            e.printStackTrace();
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

    private void setWebView() {
        WebView webView = binding.wvAmin;
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLoadsImagesAutomatically(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                CfLog.d("onPageStarted url:  " + url);
                //Log.d("---", "onPageStarted url:  " + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                CfLog.d("onPageFinished url: " + url);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                ToastUtils.showShort("");
            }

        });
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

    /**
     * 是否启动旋转横屏，true表示启动
     */
    @Override
    public boolean getDetailOrientationRotateAuto() {
        return true;
    }

    /**
     * 初始化播放器相关控件
     */
    private void initFragmet() {
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
    }

    @Override
    public void initViewObservable() {
        viewModel.matchData.observe(this, match -> {
            this.mMatch = match;
            if (match == null) {
                return;
            }
            SPUtils.getInstance().put(KEY_MATCH, new Gson().toJson(match));
            setTopBg();
            if (match.hasAs()) {
                binding.tvAnimi.setVisibility(View.VISIBLE);
            } else {
                binding.tvAnimi.setVisibility(View.GONE);
            }

            if (match.hasVideo()) {
                binding.tvLive.setVisibility(View.VISIBLE);
            } else {
                binding.tvLive.setVisibility(View.GONE);
            }
            binding.tvLeagueName.setText(match.getLeague().getLeagueName());
            binding.tvTeamMain.setText(match.getTeamMain());
            binding.tvTeamVisisor.setText(match.getTeamVistor());
            Glide.with(this)
                    .load(match.getIconMain())
                    //.apply(new RequestOptions().placeholder(placeholderRes))
                    .into(binding.ivLogoMain);

            Glide.with(this)
                    .load(match.getIconVisitor())
                    //.apply(new RequestOptions().placeholder(placeholderRes))
                    .into(binding.ivLogoVisitor);

            Glide.with(this)
                    .load(match.getIconMain())
                    //.apply(new RequestOptions().placeholder(placeholderRes))
                    .into(binding.ivLogoMainTop);

            Glide.with(this)
                    .load(match.getIconVisitor())
                    //.apply(new RequestOptions().placeholder(placeholderRes))
                    .into(binding.ivLogoVisitorTop);

            String score;
            List<Integer> scoreList = mMatch.getScore(Constants.getScoreType());

            if (scoreList != null && scoreList.size() > 1) {
                String scoreMain = String.valueOf(scoreList.get(0));
                String scoreVisitor = String.valueOf(scoreList.get(1));
                score = scoreMain + " - " + scoreVisitor;
                binding.tvScore.setText(score);
                binding.tvScoreMainTop.setText(scoreMain);
                binding.tvScoreVisitorTop.setText(scoreVisitor);
                binding.videoPlayer.getTitleTextView().setText(mMatch.getTeamMain() + score + mMatch.getTeamVistor());
                if (fullVideoPlayer != null) {
                    fullVideoPlayer.getTitleTextView().setText(mMatch.getTeamMain() + score + mMatch.getTeamVistor());
                }
            }

            // 比赛未开始
            if (!match.isGoingon()) {
                binding.tvTimeTop.setText(TimeUtils.longFormatString(match.getMatchTime(), TimeUtils.FORMAT_MM_DD_HH_MM));
                binding.tvTime.setText(TimeUtils.longFormatString(match.getMatchTime(), TimeUtils.FORMAT_MM_DD_1));
                binding.tvScore.setText(TimeUtils.longFormatString(match.getMatchTime(), TimeUtils.FORMAT_HH_MM));
            } else {
                if (TextUtils.equals(Constants.getFbSportId(), match.getSportId()) || TextUtils.equals(Constants.getBsbSportId(), match.getSportId())) { // 足球和篮球
                    binding.tvTime.setText(match.getStage() + " " + match.getTime());
                    binding.tvTimeTop.setText(match.getStage() + " " + match.getTime());
                } else {
                    binding.tvTime.setText(match.getStage());
                    binding.tvTimeTop.setText(match.getStage());
                }
            }

            if (binding.llData.getChildCount() == 0) {
                mScoreDataView = BaseDetailDataView.getInstance(this, match, false);
                if (mScoreDataView != null) {
                    binding.llData.addView(mScoreDataView);
                }
            } else {
                mScoreDataView.setMatch(match, false);
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == com.xtree.bet.R.id.tv_live) {
            binding.videoPlayer.setVisibility(View.VISIBLE);
            binding.ctlToolbarLeague.setVisibility(View.GONE);
            binding.rlToolbarTime.setVisibility(View.GONE);
            if (mMatch.isVideoStart()) {
                initVideoBuilderMode();
            } else {
                ToastUtils.showLong(getText(com.xtree.bet.R.string.bt_bt_match_not_runing));
            }

        } else if (id == com.xtree.bet.R.id.tv_animi) {
            if (mMatch.hasAs() && mMatch.isAnimationStart()) {
                if (mMatch.getAnmiUrls() != null && !TextUtils.isEmpty(mMatch.getAnmiUrls().get(0))) {
                    setWebView();
                    binding.wvAmin.setVisibility(View.VISIBLE);
                    binding.ctlToolbarLeague.setVisibility(View.GONE);
                    binding.rlToolbarTime.setVisibility(View.GONE);
                    binding.wvAmin.loadUrl(mMatch.getAnmiUrls().get(0));
                } else {
                    ToastUtils.showLong(getText(com.xtree.bet.R.string.bt_bt_match_not_runing));
                }
            }

        } else if (id == com.xtree.bet.R.id.iv_back) {
            if (binding.videoPlayer.getVisibility() == View.VISIBLE || binding.videoPlayer.getGSYVideoManager().isPlaying()) {
                binding.videoPlayer.release();
                binding.videoPlayer.setVisibility(View.GONE);
            } else if (binding.wvAmin.getVisibility() == View.VISIBLE) {
                binding.wvAmin.destroy();
                binding.wvAmin.setVisibility(View.GONE);
            } else {
                finish();
            }
        }
    }
    //加载悬浮窗
    private void initLiveFloatWindows() {
        System.out.println("================= initLiveFloatWindows 加载悬浮窗 ====================");
        initNetFloatWindows();
    }


    private void initNetFloatWindows() {
//        mBettingNetFloatingWindows = BettingNetFloatingWindows.getInstance(this, (useAgent, isChangeDomain, checkBox) -> {
//            checkBox.setChecked(useAgent);
//            setDomain(useAgent);
//            //resetViewModel();
//            setChangeDomainVisible();
//            //uploadException(useAgent, isChangeDomain);
//            mBettingNetFloatingWindows.hideSecondaryLayout();
//        });
//        mBettingNetFloatingWindows.show();

        mLiveFloatingWindows = LiveFloatingWindows.getInstance(this, (useAgent, isChangeDomain, checkBox) -> {
            checkBox.setChecked(useAgent);
            setDomain(useAgent);
            //resetViewModel();
            setChangeDomainVisible();
            //uploadException(useAgent, isChangeDomain);
            mLiveFloatingWindows.hideSecondaryLayout();
        });
        mLiveFloatingWindows.show();
    }

    /**
     * 设置当前场馆所用的domain线路
     *
     * @param isChecked
     */
    private void setDomain(boolean isChecked) {
        SPUtils.getInstance().put(SPKeyGlobal.KEY_USE_AGENT + mPlatform, isChecked);
        if (!TextUtils.equals(mPlatform, PLATFORM_PM) && !TextUtils.equals(mPlatform, PLATFORM_PMXC)) {
            // FB体育使用线路位置
            int useLinePotion = SPUtils.getInstance().getInt(SPKeyGlobal.KEY_USE_LINE_POSITION + mPlatform, 0);

            if (isChecked) {
                if (TextUtils.equals(mPlatform, PLATFORM_FBXC)) {
                    SPUtils.getInstance().put(SPKeyGlobal.FBXC_API_SERVICE_URL, DomainUtil.getApiUrl());
                } else {
                    SPUtils.getInstance().put(SPKeyGlobal.FB_API_SERVICE_URL, DomainUtil.getApiUrl());
                }
            } else {
                if (TextUtils.equals(mPlatform, PLATFORM_FBXC)) {
                    SPUtils.getInstance().put(SPKeyGlobal.FBXC_API_SERVICE_URL, BtDomainUtil.getFbxcDomainUrl().get(useLinePotion));
                } else {
                    SPUtils.getInstance().put(SPKeyGlobal.FB_API_SERVICE_URL, BtDomainUtil.getFbDomainUrl().get(useLinePotion));
                }
            }
        } else {
            if (isChecked) {
                if (TextUtils.equals(mPlatform, PLATFORM_PMXC)) {
                    SPUtils.getInstance().put(SPKeyGlobal.PMXC_API_SERVICE_URL, DomainUtil.getApiUrl());
                } else {
                    SPUtils.getInstance().put(SPKeyGlobal.PM_API_SERVICE_URL, DomainUtil.getApiUrl());
                }
            } else {
                if (TextUtils.equals(mPlatform, PLATFORM_PMXC)) {
                    SPUtils.getInstance().put(SPKeyGlobal.PMXC_API_SERVICE_URL, BtDomainUtil.getDefaultPmxcDomainUrl());
                } else {
                    SPUtils.getInstance().put(SPKeyGlobal.PM_API_SERVICE_URL, BtDomainUtil.getDefaultPmDomainUrl());
                }
            }
        }
    }

    /**
     * 是否使用代理或其他非默认线路
     */
    private void setChangeDomainVisible() {
        boolean isAgent = SPUtils.getInstance().getBoolean(SPKeyGlobal.KEY_USE_AGENT + mPlatform);
        mLiveFloatingWindows.setIsSelected(isAgent);
    }


}
