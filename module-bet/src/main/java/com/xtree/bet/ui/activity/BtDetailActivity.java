package com.xtree.bet.ui.activity;

import static com.xtree.base.utils.BtDomainUtil.KEY_PLATFORM;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_PM;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_PMXC;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gyf.immersionbar.ImmersionBar;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.ClickUtil;
import com.xtree.base.utils.TimeUtils;
import com.xtree.bet.BR;
import com.xtree.bet.R;
import com.xtree.bet.bean.ui.Category;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.PlayType;
import com.xtree.bet.constant.Constants;
import com.xtree.bet.contract.BetContract;
import com.xtree.bet.manager.BtCarManager;
import com.xtree.bet.ui.fragment.BtCarDialogFragment;
import com.xtree.bet.ui.fragment.BtDetailOptionFragment;
import com.xtree.bet.ui.viewmodel.TemplateBtDetailViewModel;
import com.xtree.bet.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.bet.ui.viewmodel.factory.PMAppViewModelFactory;
import com.xtree.bet.ui.viewmodel.fb.FbBtDetailViewModel;
import com.xtree.bet.ui.viewmodel.pm.PmBtDetailViewModel;
import com.xtree.bet.util.MatchDeserializer;
import com.xtree.bet.weight.BaseDetailDataView;
import com.xtree.bet.weight.pm.SnkDataView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import com.xtree.base.bus.RxBus;
import com.xtree.base.utils.SPUtils;
import com.xtree.base.utils.ToastUtils;
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager;

public class BtDetailActivity extends GSYBaseActivityDetail<StandardGSYVideoPlayer> implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private final static String KEY_MATCH = "KEY_MATCH_ID";
    private List<Category> mCategories = new ArrayList<>();

    private BaseDetailDataView mScoreDataView;

    private BtDetailOptionFragment fragment;

    private Match mMatch;

    private int tabPos;

    private String mPlatform = SPUtils.getInstance().getString(KEY_PLATFORM);

    public Match getmMatch() {
        return mMatch;
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.bt_layout_detail;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    /**
     * 初始化沉浸式
     * Init immersion bar.
     */
    protected void initImmersionBar() {
        //设置共同沉浸式样式
        ImmersionBar.with(this)
                .navigationBarColor(R.color.default_navigation_bar_color)
                .fitsSystemWindows(false)
                .statusBarDarkFont(false)
                .init();
    }

    public static void start(Context context, Match match) {
        Intent intent = new Intent(context, BtDetailActivity.class);
        SPUtils.getInstance().put(KEY_MATCH, new Gson().toJson(match));
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
                binding.toolbar.setBackgroundResource(R.color.bt_color_detail_top_toolbar);
            } else if (Math.abs(verticalOffset) == 0) {//展开
                binding.rlToolbarTime.setVisibility(View.GONE);
                if (binding.videoPlayer.getVisibility() != View.VISIBLE && binding.wvAmin.getVisibility() != View.VISIBLE) {
                    binding.ctlToolbarLeague.setVisibility(View.VISIBLE);
                    binding.llData.setVisibility(View.VISIBLE);
                } else {
                    binding.ctlToolbarLeague.setVisibility(View.GONE);
                    binding.llData.setVisibility(View.GONE);
                }
                binding.llLive.setVisibility(View.VISIBLE);
                binding.toolbar.setBackgroundResource(android.R.color.transparent);
            } else {
                binding.llLive.setVisibility(View.VISIBLE);
                binding.llData.setVisibility(View.VISIBLE);
            }
        });

        binding.tabCategoryType.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabPos = tab.getPosition();
                for(int i = 0; i < binding.tabCategoryType.getTabCount(); i ++){
                    if(tabPos == i){
                        binding.tabCategoryType.getTabAt(i).getCustomView().setBackgroundResource(R.mipmap.bt_bg_category_tab_selected);
                    }else {
                        binding.tabCategoryType.getTabAt(i).getCustomView().setBackgroundResource(R.drawable.bt_bg_category_tab);
                    }
                }

                updateOptionData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        binding.rlCg.setOnClickListener(this);
        binding.ivExpand.setOnClickListener(this);
        initVideoPlayer();
        setWebView();

    }

    @Override
    public void initData() {
        //mMatch = getIntent().getParcelableExtra(KEY_MATCH);
        Gson gson = new GsonBuilder().serializeNulls().registerTypeAdapter(Match.class, new MatchDeserializer()).create();
        mMatch = gson.fromJson(SPUtils.getInstance().getString(KEY_MATCH), Match.class);
        viewModel.getMatchDetail(mMatch.getId());
        viewModel.getCategoryList(String.valueOf(mMatch.getId()), mMatch.getSportId());
        viewModel.addSubscription();
        setCgBtCar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.addSubscribe(Observable.interval(5, 5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    viewModel.getMatchDetail(mMatch.getId());
                    viewModel.getCategoryList(String.valueOf(mMatch.getId()), mMatch.getSportId());
                    /*if (!mCategories.isEmpty() && mCategories.size() > tabPos) {
                        viewModel.getCategoryList(String.valueOf(mMatch.getId()), mMatch.getSportId());
                    }*/
                })
        );
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
        //EXOPlayer内核，支持格式更多
        PlayerFactory.setPlayManager(Exo2PlayerManager.class);
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
                .setShrinkImageRes(R.mipmap.bt_video_shrink)
                .setEnlargeImageRes(R.mipmap.bt_video_enlarge)
                .setVideoTitle(mMatch.getTeamMain() + score + mMatch.getTeamVistor())
                .setIsTouchWiget(false)
                //.setAutoFullWithSize(true)
                .setRotateViewAuto(false)
                .setLockLand(false)
                .setShowFullAnimation(false)//打开动画
                .setNeedLockFull(false)
                .setLooping(true)  // 直播需要循环播放
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
     * 更新投注选项数据
     */
    private void updateOptionData() {
        if (fragment == null) {
            if (mCategories != null && !mCategories.isEmpty()) {
                fragment = BtDetailOptionFragment.getInstance(mMatch, (ArrayList<PlayType>) mCategories.get(tabPos).getPlayTypeList(),false);
                FragmentTransaction trans = getSupportFragmentManager()
                        .beginTransaction();
                trans.replace(R.id.fl_option, fragment);
                trans.commitAllowingStateLoss();
            }
        } else {
            if (mCategories != null && !mCategories.isEmpty()) {
                if (tabPos < mCategories.size()) {
                    Category category = mCategories.get(tabPos);
                    if (category == null) {
                        return;
                    }
                    RxBus.getDefault().post(new BetContract(BetContract.ACTION_OPTION_CHANGE, category.getPlayTypeList()));
                }
            }
        }
    }

    @Override
    public void initViewObservable() {
        viewModel.matchData.observe(this, match -> {
            this.mMatch = match;
            if (match == null) {
                return;
            }
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
                if(mScoreDataView instanceof SnkDataView || mScoreDataView instanceof com.xtree.bet.weight.fb.SnkDataView){
                    mScoreDataView.setSnkMatch(match, false);
                    mScoreDataView.addMatchListAdditional(match.getFormat() + " 总分");
                }else{
                    mScoreDataView.setMatch(match, false);
                }

            }

        });
        viewModel.updateCagegoryListData.observe(this, categories -> {
            mCategories = categories;
        });
        viewModel.categoryListData.observe(this, categories -> {
            if (categories.isEmpty()) {
                binding.rlPlayMethod.setVisibility(View.GONE);
                binding.flOption.setVisibility(View.GONE);
                binding.llEnd.llEmpty.setVisibility(View.VISIBLE);
            } else {
                binding.rlPlayMethod.setVisibility(View.VISIBLE);
                binding.flOption.setVisibility(View.VISIBLE);
                binding.llEnd.llEmpty.setVisibility(View.GONE);
            }
            //if (mCategories.size() != categories.size()) {
                mCategories = categories;
                if(binding.tabCategoryType.getTabCount() == 0) {
                    for (int i = 0; i < categories.size(); i++) {
                        View view = LayoutInflater.from(this).inflate(R.layout.bt_layout_bet_catory_tab_item, null);
                        TextView tvName = view.findViewById(R.id.tab_item_name);
                        String name = categories.get(i) == null ? "" : categories.get(i).getName();

                        tvName.setText(name);
                        ColorStateList colorStateList = getResources().getColorStateList(R.color.bt_color_category_tab_text);
                        tvName.setTextColor(colorStateList);

                        binding.tabCategoryType.addTab(binding.tabCategoryType.newTab().setCustomView(view));

                    }
                }else{
                    for (int i = 0; i < categories.size(); i++) {
                        try {
                            if (binding.tabCategoryType == null) {
                                CfLog.e("=========binding.tabCategoryType == null=========");
                            }
                            if (categories.get(i) == null && binding.tabCategoryType != null && i < binding.tabCategoryType.getTabCount()) {
                                binding.tabCategoryType.removeTabAt(i);
                                if (binding.tabCategoryType.getTabCount() == 0) {
                                    binding.rlPlayMethod.setVisibility(View.GONE);
                                    binding.flOption.setVisibility(View.GONE);
                                    binding.llEnd.llEmpty.setVisibility(View.VISIBLE);
                                }
                            }
                        }catch (Exception e){
                            CfLog.e("binding.tabCategoryType.getTabCount()-------" + binding.tabCategoryType.getTabCount() + "-----" + i);
                            CfLog.e(e.getMessage());
                        }
                    }
                    viewModel.updateCategoryData();
                }
            updateOptionData();
            /*} else {
                mCategories = categories;
                updateOptionData();
            }*/
        });

        viewModel.betContractListData.observe(this, betContract -> {
            if (betContract.action == BetContract.ACTION_BTCAR_CHANGE) {
                setCgBtCar();
                updateOptionData();
                if (!BtCarManager.isCg()) {
                    binding.rlCg.postDelayed(() -> RxBus.getDefault().postSticky(new BetContract(BetContract.ACTION_OPEN_TODAY)), 500);
                }
            } else if (betContract.action == BetContract.ACTION_OPEN_CG) {
                setCgBtCar();
                updateOptionData();
            }
        });
    }


    /**
     * 设置串关数量与显示与否
     */
    public void setCgBtCar() {
        binding.tvCgCount.setText(String.valueOf(BtCarManager.size()));
        binding.rlCg.setVisibility(!BtCarManager.isCg() ? View.GONE : BtCarManager.isEmpty() ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onRefresh() {
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.rl_cg) {
            if (BtCarManager.size() <= 1) {
                ToastUtils.showLong(getText(R.string.bt_bt_must_have_two_match));
                return;
            }
            if(ClickUtil.isFastClick()){
                return;
            }
            BtCarDialogFragment btCarDialogFragment = new BtCarDialogFragment();
            btCarDialogFragment.show(BtDetailActivity.this.getSupportFragmentManager(), "btCarDialogFragment");
        } else if (id == R.id.tv_live) {
            if (!mMatch.isVideoStart()) {
                ToastUtils.showLong(getText(R.string.bt_bt_match_not_runing));
                return;
            }
            binding.ctlToolbarLeague.setVisibility(View.GONE);
            binding.rlToolbarTime.setVisibility(View.GONE);
            binding.llData.setVisibility(View.GONE);
            if (TextUtils.equals(mMatch.getVideoType(), "p")) {//是PM场馆H5播放页面
                if (!mMatch.getVideoUrls().isEmpty()) {
                    binding.wvAmin.setVisibility(View.VISIBLE);
                    binding.wvAmin.loadUrl(mMatch.getVideoUrls().get(0));
                }
            } else {
                binding.videoPlayer.setVisibility(View.VISIBLE);
                initVideoBuilderMode();
            }

        } else if (id == R.id.tv_animi) {
            if (mMatch.hasAs() && mMatch.isAnimationStart()) {
                if (mMatch.getAnmiUrls() != null && !TextUtils.isEmpty(mMatch.getAnmiUrls().get(0))) {
                    binding.wvAmin.setVisibility(View.VISIBLE);
                    binding.ctlToolbarLeague.setVisibility(View.GONE);
                    binding.rlToolbarTime.setVisibility(View.GONE);
                    binding.wvAmin.loadUrl(mMatch.getAnmiUrls().get(0));
                    binding.llData.setVisibility(View.GONE);
                } else {
                    ToastUtils.showLong(getText(R.string.bt_bt_match_not_runing));
                }
            }

        } else if (id == R.id.iv_back) {
            if (binding.videoPlayer.getVisibility() == View.VISIBLE || binding.videoPlayer.getGSYVideoManager().isPlaying()) {
                binding.videoPlayer.release();
                binding.videoPlayer.setVisibility(View.GONE);
            } else if (binding.wvAmin.getVisibility() == View.VISIBLE) {
                binding.wvAmin.loadUrl("about:blank");
                binding.wvAmin.setVisibility(View.GONE);
            } else {
                finish();
            }
        } else if (id == R.id.iv_expand) {
            if (fragment != null) {
                fragment.expand();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.wvAmin.destroy();
    }
}
