package com.xtree.live.ui.main.activity;

import static android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.app.PictureInPictureParams;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.Rational;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.google.android.material.color.MaterialColors;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.opensource.svgaplayer.SVGACache;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.opensource.svgaplayer.utils.log.SVGALogger;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.DomainUtil;
import com.xtree.bet.BR;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.ui.viewmodel.TemplateBtDetailViewModel;
import com.xtree.bet.ui.viewmodel.factory.PMAppViewModelFactory;
import com.xtree.bet.ui.viewmodel.fb.FbBtDetailViewModel;
import com.xtree.bet.ui.viewmodel.pm.PmBtDetailViewModel;
import com.xtree.live.LiveConfig;
import com.xtree.live.R;
import com.xtree.live.data.factory.AppViewModelFactory;
import com.xtree.live.data.source.response.LiveRoomInfoBean;
import com.xtree.live.data.source.response.LiveUserBean;
import com.xtree.live.data.source.response.QualityBean;
import com.xtree.live.databinding.ActivityLiveDetailBinding;
import com.xtree.live.databinding.IncludeTimerBinding;
import com.xtree.live.databinding.MergeLiveDetailAfterLiveRecommendBinding;
import com.xtree.live.gift.GiftDialog;
import com.xtree.live.gift.ParseCompletion;
import com.xtree.live.inter.GiftViewMarginBottomListener;
import com.xtree.live.inter.VideoControllerListener;
import com.xtree.live.manager.AudioStateManager;
import com.xtree.live.manager.MediaSessionCallback;
import com.xtree.live.message.MessageGift;
import com.xtree.live.message.MessageLiveClose;
import com.xtree.live.message.MessageMsg;
import com.xtree.live.message.MessageOpen;
import com.xtree.live.message.MessageVid;
import com.xtree.live.message.RoomType;
import com.xtree.live.message.SimpleMessageListener;
import com.xtree.live.model.GiftBean;
import com.xtree.live.player.LiveVideoModel;
import com.xtree.live.player.MediaQuality;
import com.xtree.live.player.StreamSourceType;
import com.xtree.live.socket.ChatWebSocketManager;
import com.xtree.live.ui.main.fragment.LiveDetailHomeFragment;
import com.xtree.live.ui.main.viewmodel.LiveViewModel;
import com.xtree.live.uitl.DialogUtil;
import com.xtree.live.uitl.PipModeUtils;
import com.xtree.live.uitl.TimeUtil;
import com.xtree.live.uitl.WindowModeUtils;
import com.xtree.live.uitl.WordUtil;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;

import me.xtree.mvvmhabit.base.BaseActivity;
import me.xtree.mvvmhabit.utils.ToastUtils;

@Route(path = RouterFragmentPath.Live.LIVE_DETAIL)
public class LiveDetailActivity extends BaseActivity<ActivityLiveDetailBinding, LiveViewModel> implements OnRefreshLoadMoreListener, View.OnClickListener, SVGAParser.ParseCompletion , SVGAParser.PlayCallback, GiftViewMarginBottomListener {

    public final static String KEY_MATCH = "KEY_MATCH_ID";
    LiveDetailHomeFragment mLiveDetailPage;
    private SVGAParser mSvgaParser;

    private String mVid;
    private int mType = -1;//比赛类型
    private int mMatchId;//赛事ID
    private String mLiveMatchId;
    private int mUid;
    private boolean isClose = false;

    public LiveRoomInfoBean mLiveRoomInfo;
    public LiveUserBean mLiveUserInfo;


    private AudioStateManager mAudioStateManager;

    private boolean mIsInFreeFormMode = false;
    private Boolean mIsRemoteCasted = false;
    private PriorityQueue<GiftConsumer> mPriorityQueue;
    private GiftDialog mGiftDialog;
    private List<GiftBean> mGiftBeans;
    private IncludeTimerBinding mTimerBinding;
    //这部分 我们暂时没有
//    private MergeLiveRoomActionBinding mLiveActionBinding;
    private MergeLiveDetailAfterLiveRecommendBinding mRecommendBinding;
    private MediaSessionCompat mSession;
    private PictureInPictureParams.Builder mPictureInPictureParamsBuilder;
    private ValueAnimator valueAnimator;
    private Match mMatch;

    private final Rect mPipTransformRect = new Rect();

    public static void forward(Context context, int uid,String vid,String matchId) {
        if(uid <= 0)return;
        for (Activity activity : ActivityUtils.getActivityList()) {
            if (LiveDetailActivity.class.getName().equals(activity.getClass().getName())) {
                activity.finishAndRemoveTask();
            }
        }

        Intent intent = new Intent(context, LiveDetailActivity.class);
        intent.putExtra("uid", uid);
        intent.putExtra("vid", vid);
        intent.putExtra("matchID", matchId);
        //在Android 12 系统的手机（oppo 小米,模拟器谷歌12），home进入画中画，然后在恢复全屏模式，然后点击界面上其他按钮打开新界面，会出现 startActivity新的界面会延迟 7-8 秒 或者 打不开新界面的情况。
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    // type  -1 全部  0足球  1篮球  2 其他  3电竞
    private  void forward(int uid) {
        Intent intent = new Intent(this, LiveDetailActivity.class);
        intent.putExtra("uid", uid);
        //在Android 12 系统的手机（oppo 小米,模拟器谷歌12），home进入画中画，然后在恢复全屏模式，然后点击界面上其他按钮打开新界面，会出现 startActivity新的界面会延迟 7-8 秒 或者 打不开新界面的情况。
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(intent);
    }

    private static class GiftConsumer {
        String swf = "";
        Long swfTime = 0L;
    }

    private static class GiftComparator implements Comparator<GiftConsumer> {
        @Override
        public int compare(GiftConsumer consumer, GiftConsumer consumer2) {
            return consumer.swfTime > consumer2.swfTime ? 1 : 0;
        }
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_live_detail;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    /**
     * 初始化沉浸式
     * Init immersion bar.
     */
    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
//        //设置共同沉浸式样式
//        ImmersionBar.with(this)
////                .navigationBarColor(me.xtree.mvvmhabit.R.color.default_navigation_bar_color)
//                .fitsSystemWindows(false)
//                .statusBarDarkFont(false)
//                .init();
    }

    @Override
    public void initView() {
        mLiveMatchId = getIntent().getStringExtra("matchID");
        CfLog.d("=============== LiveDetailActivity initView mMatchId ==============="+mLiveMatchId);
        mTimerBinding = IncludeTimerBinding.bind(binding.getRoot());
        mRecommendBinding = MergeLiveDetailAfterLiveRecommendBinding.bind(binding.getRoot());
//        mLiveActionBinding = MergeLiveRoomActionBinding.bind(binding.getRoot());

        BarUtils.setStatusBarLightMode(getWindow(), false);
        // 保持屏幕常亮
        getWindow().setFlags(FLAG_KEEP_SCREEN_ON, FLAG_KEEP_SCREEN_ON);

        Configuration configuration = getResources().getConfiguration();
        mIsInFreeFormMode = WindowModeUtils.isFreeFormMode(configuration);
//        mShowReportButton = 1 == AppConfig.getConfig().getReport();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mPriorityQueue = new PriorityQueue<>(new GiftComparator());
        } else {
            mPriorityQueue = new PriorityQueue<>(11, new GiftComparator());
        }

        initGifDialog();
        initListener();
        initAudioManager();


        int[] location = new int[2];
        binding.widgetExoPlayer.addOnLayoutChangeListenerToPlayer((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            v.getLocationInWindow(location);
            mPipTransformRect.left = location[0];
            mPipTransformRect.top = location[1];
            mPipTransformRect.right = location[0] + v.getWidth();
            mPipTransformRect.bottom = location[1] + v.getHeight();
        });

//        ConfigurationBean config = AppConfig.getConfig();
//        if(!TextUtils.isEmpty(config.getVideoAdBarContent())){
//            binding.platformAdsLayout.setVisibility(View.VISIBLE);
//            binding.platformAdsTitle.setText(config.getVideoAdBarTitle());
//            binding.platformAdsContent.setText(config.getVideoAdBarContent());
//
//            binding.platformAdsContent.setRndDuration(100);
//            binding.platformAdsContent.setScrollMode(MarqueeTextView.SCROLL_FOREVER);
//            binding.platformAdsContent.setScrollFirstDelay(200);
//
//            binding.platformAdsContent.startScroll();
//        }else {
//            binding.platformAdsContent.stopScroll();
//            binding.platformAdsLayout.setVisibility(View.GONE);
//        }


//        View.OnClickListener clickListener = v -> {
//            try {
//                if(!TextUtils.isEmpty(config.getTextLinkUrl())){
//                    IntentUtils.jumpToBrowser(this, config.getTextLinkUrl());
//                }
//            }catch (ActivityNotFoundException e){
//                e.printStackTrace();
//            }
//        };
//        binding.platformAdsContent.setOnClickListener(clickListener);

        //直播广告初始化时候
//        binding.widgetExoPlayer.getLiveAdOverlayImage().setOnClickListener(v -> IntentUtils.jumpToBrowser(this, AppConfig.getConfig().getLoadingCoverScreenLink()));
//        GlideLoader.loadVideoPauseCover(GlideApp.with(this), AppConfig.getConfig().getLoadingCoverScreenUrl(), mBinding.widgetExoPlayer.getLiveAdOverlayImage());

        binding.ivSvga.post(()->{
            ViewGroup.LayoutParams layoutParams = binding.ivSvga.getLayoutParams();
            int size = Math.min(ScreenUtils.getAppScreenHeight(), ScreenUtils.getAppScreenWidth());
            layoutParams.height = size;
            layoutParams.width = size;
            binding.ivSvga.setLayoutParams(layoutParams);
        });

        initObservable();
    }

    private void initPresent() {
        //获取当前直播间详情
        viewModel.getRoomInfo(mUid);
//        mvpPresenter.getUserInfo();
        viewModel.getGiftList();
    }

    private void initObservable() {
        //首先获取到直播间的信息，这个直播详情也需要这个数据
        viewModel.liveRoomInfo.observe(this, liveRoomBean -> {

            if (liveRoomBean == null || liveRoomBean.getInfo() == null) {
                viewModel.getRoomInfo(mUid);
                return;
            }
            mLiveRoomInfo = liveRoomBean.getInfo();

            LiveUserBean user = liveRoomBean.getUserData();
            LiveRoomInfoBean info = liveRoomBean.getInfo();
            mLiveUserInfo = user;
            mLiveRoomInfo = info;
            mVid = liveRoomBean.getVid();
            mType = info.getType();
            mMatchId = info.getMatch_id();
            if(liveRoomBean.getInfo()!=null){
                int matchId = liveRoomBean.getInfo().getMatch_id();
                //type -1 全部  直播  热门 足球  篮球 关注 其他
                mType = liveRoomBean.getInfo().getType();
                //privateVid 私有 私密
                mLiveDetailPage = LiveDetailHomeFragment.newInstance(mUid,mVid,"",mType,matchId);
                getSupportFragmentManager().beginTransaction().replace(R.id.main_page, mLiveDetailPage).commitAllowingStateLoss();

            }


            //如果开播时间大于服务器当前时间  则开始倒计时
            int startTime = mLiveRoomInfo.getStarttime();
            int serverTime = mLiveRoomInfo.getServertime();
            if (startTime > serverTime) {
                mTimerBinding.countdownView.setVisibility(View.VISIBLE);
                binding.buttonBack.setVisibility(View.VISIBLE);
                //TODO 展示即将开始的比赛的 主队 - 客队 以及开赛时间

            } else {
                mTimerBinding.countdownView.setVisibility(View.GONE);
                binding.buttonBack.setVisibility(View.VISIBLE);
                QualityBean quality = info.getClarity();
                //直播已经关闭了
                if (quality == null || TextUtils.isEmpty(quality.getSmooth()) && TextUtils.isEmpty(quality.getHd()) && TextUtils.isEmpty(quality.getSd())) {
                    ToastUtils.showShort("直播已结束");
                    return;
                } else {
                    initVideoPlayer(mLiveRoomInfo, true);
                }
            }

        });

        viewModel.liveRoomInfoRefresh.observe(this,liveRoomBean -> {
            if (liveRoomBean.getInfo() == null) return;
            mLiveRoomInfo = liveRoomBean.getInfo();
            initVideoPlayer(mLiveRoomInfo, false);
        });

        //礼物列表
        viewModel.giftList.observe(this, giftBeans -> {
            mGiftBeans = giftBeans;
            mGiftDialog.setNewData(giftBeans);
        });

        viewModel.matchData.observe(this, match -> {
            this.mMatch = match;
            CfLog.d("LiveDetailActivity initViewObservable mMatch:" + mMatch);
            if (match == null) {
                return;
            }
        });

        // 创建成功的 Observer
        Observer<com.xtree.live.data.source.response.fb.Match> successObserver = new Observer<com.xtree.live.data.source.response.fb.Match>() {
            @Override
            public void onChanged(com.xtree.live.data.source.response.fb.Match match) {
                Log.d("getMatchDetail", "获取比赛详情成功：" + match.toString());
                me.xtree.mvvmhabit.utils.SPUtils.getInstance().put(KEY_MATCH, new Gson().toJson(match));
            }
        };

        // 创建失败的 Observer
        Observer<Object> errorObserver = new Observer<Object>() {
            @Override
            public void onChanged(Object error) {
                if (error instanceof Throwable) {
                    Log.e("GameTokenAPI", "发生错误：" + ((Throwable) error).getMessage());
                } else {
                    Log.e("GameTokenAPI", "未知错误：" + error.toString());
                }
            }
        };


        Log.d("LiveDetailActivity", "mLiveMatchId：" + mLiveMatchId);
        viewModel.getMatchDetail(mLiveMatchId,successObserver,errorObserver);
    }

    @Override
    public LiveViewModel initViewModel() {
        com.xtree.live.data.factory.AppViewModelFactory factory = AppViewModelFactory.getInstance(this.getApplication());
        return new ViewModelProvider(this, factory).get(LiveViewModel.class);
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //重启页面
        if (savedInstanceState != null) {
            mUid = savedInstanceState.getInt("uid", 0);
            mVid = savedInstanceState.getString("vid");
            mType = savedInstanceState.getInt("type", 0);
        } else {
            mUid = getIntent().getIntExtra("uid", 0);
        }
        try {
            SVGACache.INSTANCE.onCreate(this, SVGACache.Type.FILE);
            mSvgaParser = new SVGAParser(this);
            SVGALogger.INSTANCE.setLogEnabled(ARouter.debuggable());
        } catch (Exception e) {

        }

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //重启页面
        if (intent != null) {
//            mvpPresenter.recordExitTime(mUid);
            mUid = intent.getIntExtra("uid", 0);
            mVid = "";
            mType = -1;
            mLiveUserInfo = null;
            mLiveRoomInfo = null;

            mIsRemoteCasted = false;
            initData();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("uid", mUid);
        outState.putInt("type", mType);
        outState.putInt("matchId", mMatchId);
        outState.putString("vid", mVid);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        OnApplyWindowInsetsListener onApplyWindowInsetsListener = new OnApplyWindowInsetsListener() {
            @NonNull
            @Override
            public WindowInsetsCompat onApplyWindowInsets(@NonNull View root, @NonNull WindowInsetsCompat windowInsets) {
                Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
                Configuration config = getResources().getConfiguration();
                int topMargin = insets.top;
                int leftMargin = insets.left;
                int rightMargin = insets.right;
                int bottomMargin = insets.bottom;


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    if (!(isInMultiWindowMode() || mIsInFreeFormMode || isInPictureInPictureMode())) {
                        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                            topMargin = 0;
                            bottomMargin = 0;
                        }
                    } else if (isInPictureInPictureMode() && config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        topMargin = 0;
                        leftMargin = 0;
                        rightMargin = 0;
                        bottomMargin = 0;
                    }
                }

                setWidgetExoPlayerLayoutParams();
                setRootLayoutParams(topMargin, leftMargin, rightMargin, bottomMargin);
                windowInsets.inset(leftMargin, topMargin, rightMargin, bottomMargin);
                return windowInsets;
            }
        };
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), onApplyWindowInsetsListener);
        requestApplyWindowInsets(binding.getRoot());
    }

    private void setSvgaMarginBottom(int svgaMarginBottom) {

        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) binding.ivSvga.getLayoutParams();
        if(layoutParams.bottomMargin != svgaMarginBottom){
            layoutParams.bottomMargin = svgaMarginBottom;
            binding.ivSvga.setLayoutParams(layoutParams);
        }
    }

    private void setRootLayoutParams(int topMargin, int leftMargin, int rightMargin, int bottomMargin) {

        ViewGroup.MarginLayoutParams rootLayoutParams = (ViewGroup.MarginLayoutParams) binding.getRoot().getLayoutParams();
        rootLayoutParams.leftMargin = leftMargin;
        rootLayoutParams.topMargin = topMargin;
        rootLayoutParams.rightMargin = rightMargin;
        rootLayoutParams.bottomMargin = bottomMargin;
        binding.getRoot().setLayoutParams(rootLayoutParams);
    }

    private void setWidgetExoPlayerLayoutParams() {
        ConstraintLayout.LayoutParams playerLayoutParams = (ConstraintLayout.LayoutParams) binding.widgetExoPlayer.getLayoutParams();
        playerLayoutParams.width = 0;
        playerLayoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        playerLayoutParams.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
        playerLayoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            playerLayoutParams.height = 0;
            playerLayoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        } else {
            playerLayoutParams.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
            playerLayoutParams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET;
        }
        binding.widgetExoPlayer.setLayoutParams(playerLayoutParams);
    }

    void requestApplyWindowInsets(View view) {
        if (view.isAttachedToWindow()) {
            // We're already attached, just request as normal
            view.requestApplyInsets();
        } else {
            // We're not attached to the hierarchy, add a listener to
            // request when we are
            view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(@NonNull View v) {
                    v.removeOnAttachStateChangeListener(this);
                    v.requestApplyInsets();
                }

                @Override
                public void onViewDetachedFromWindow(@NonNull View v) {

                }
            });
        }
    }

//    private void initNotification() {
//        mLiveActionBinding.btnNotificationLabel.setText(AppConfig.isNotificationBeepOn() ? R.string.notification_status_open : R.string.notification_status_close);
//        mLiveActionBinding.llBtnNotification.setOnClickListener(v->{
//            mLiveActionBinding.llBtnNotification.setSelected(!mLiveActionBinding.llBtnNotification.isSelected());
//            AppConfig.setNotificationBeepOn(mLiveActionBinding.llBtnNotification.isSelected());
//            mLiveActionBinding.btnNotificationLabel.setText(AppConfig.isNotificationBeepOn() ? R.string.notification_status_open : R.string.notification_status_close);
//        });
//    }

    private void initListener() {
        binding.widgetExoPlayer.setOnRefreshSourceListener(() -> viewModel.getRoomInfo(mUid));
//        binding.widgetExoPlayer.getReportButton().setOnClickListener(view -> mvpPresenter.getReportList());

        binding.ivSvga.setCallback(new SVGACallback() {
            @Override
            public void onPause() {

            }

            @Override
            public void onFinished() {
                binding.ivSvga.setVisibility(View.GONE);
            }

            @Override
            public void onRepeat() {

            }

            @Override
            public void onStep(int i, double v) {

            }
        });
        binding.buttonBack.setOnClickListener(v -> {
            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                binding.widgetExoPlayer.forceFullScreen();
            } else {
                finish();
            }
        });

//        binding.widgetExoPlayer.setOnStartAdsFinishListener(() -> {
//            binding.widgetExoPlayer.getLiveAdOverlayImage().setOnClickListener(v -> IntentUtils.jumpToBrowser(this, AppConfig.getConfig().getVideoStopAdvertisingImageLink()));
//            if(this.isDestroyed() || this.isFinishing())return;
//            GlideLoader.loadVideoPauseCover(GlideApp.with(this), AppConfig.getConfig().getVideoStopAdvertisingImageUrl(), binding.widgetExoPlayer.getLiveAdOverlayImage());
//        });

        binding.widgetExoPlayer.getPipButton().setOnClickListener(view -> {
            if (mLiveRoomInfo == null) return;
            //非分屏模式和freeform模式时
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (!isInMultiWindowMode() && !mIsInFreeFormMode) {
                    if (mIsRemoteCasted) {
                        ToastUtils.showShort("当前正在投屏中，无法进入画中画模式。请先提出投屏模式");
                        return;
                    }
                    //支持画中画
                    if (PipModeUtils.isSupportPipMode(this)) {
                        //提示开启画中画权限
                        if (!PipModeUtils.isPipPermissionGranted(this)) {
                            new DialogUtil.Builder(this)
                                    .setTitle(WordUtil.getString(R.string.tips))
                                    .setContent("使用画中画模式需要打开画中画模式系统开关")
                                    .setConfrimString(WordUtil.getString(R.string.confirm))
                                    .setCancelString(WordUtil.getString(R.string.cancel))
                                    .setCancelable(false)
                                    .setClickCallback((dialog, content) -> PipModeUtils.gotoPipSetting(this))
                                    .build().show();
                        } else {
                            enterPipMode();
                        }
                    }
                }
            }
        });
        binding.widgetExoPlayer.setVideoControllerListener(new VideoControllerListener() {
            @Override
            public void isControllerVisible(Boolean isVisible) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    if (isVisible && !isInPictureInPictureMode()) {
                        binding.buttonBack.setVisibility(View.VISIBLE);
                        binding.widgetExoPlayer.getPipButton().setVisibility(PipModeUtils.isSupportPipMode(LiveDetailActivity.this) ? View.VISIBLE : View.GONE);
//                        binding.widgetExoPlayer.getReportButton().setVisibility(mShowReportButton ? View.VISIBLE : View.GONE);
//                        binding.widgetExoPlayer.getMirrorButton().setVisibility(mAcquiredDLNASource ? View.VISIBLE : View.GONE);
                    } else {
                        binding.widgetExoPlayer.getPipButton().setVisibility(View.GONE);
                        binding.widgetExoPlayer.getReportButton().setVisibility(View.GONE);
                        if (!mIsRemoteCasted) {
                            binding.buttonBack.setVisibility(View.GONE);
//                            binding.widgetExoPlayer.getMirrorButton().setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFullscreenButtonClick() {
                if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else {
                    KeyboardUtils.hideSoftInput(LiveDetailActivity.this);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    if (isInPictureInPictureMode() && mSession != null) {
                        long actions = mSession.getController().getPlaybackState().getActions();
                        updateSessionPlaybackState(actions);
                    }
                }
            }
        });

        binding.widgetExoPlayer.getShareButton().setOnClickListener(view -> {
            if (mLiveRoomInfo == null) return;
            showShareLiveDialog();
        });

//        mLiveActionBinding.llBtnSign.setOnClickListener(view -> {
//            if (mLiveRoomInfo == null) return;
//            MyTaskActivity.forward(this);
//        });

       /* mLiveActionBinding.llBtnEnvelope.setOnClickListener(view -> {
            if (mLiveRoomInfo == null) return;
            showTaskPage();
        });

        mLiveActionBinding.llBtnGift.setOnClickListener(view -> {
            mGiftDialog.show();
        });

        mLiveActionBinding.llBtnHeart.setOnClickListener(view -> {
            if (mLiveRoomInfo == null) return;
            AppManager.alreadyLoginInThenGotoPage(this, ()-> DoFollowHelper.doFollow(mvpPresenter, mUid, (result, msg, id) -> {
                this.onDoFollow(result, msg);
            }));
        });*/
    }

    private void initGifDialog() {
        mGiftDialog = new GiftDialog(this, (bean, callback) -> {
            if (mLiveRoomInfo == null) return;
            if (LiveConfig.isVisitor()) {
                ARouter.getInstance().build(RouterActivityPath.Mine.PAGER_LOGIN_REGISTER).navigation();
                return;
            }
            LiveRoomInfoBean roomInfo = mLiveRoomInfo;
            //主播不能给自己送礼物
            if (LiveConfig.getUserId().equals(roomInfo.getUid() + "")) {
                ToastUtils.showShort("不能给自己送礼物哦");
                return;
            }
//            mvpPresenter.sendGift(bean, callback);
        }, this);
    }


    @Override
    public void onMarginBottom(int margin) {
        this.setSvgaMarginBottom(margin);
    }

    @Override
    public int restoreBottomMargin() {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) binding.ivSvga.getLayoutParams();
        return layoutParams.bottomMargin;
    }


    @Override
    public void initData() {
        //默认值
        mRecommendBinding.recommendView.setVisibility(View.GONE);
        mTimerBinding.countdownView.setVisibility(View.GONE);
        isClose = false;
        initPresent();
    }

    public boolean isSameConversationRoom(MessageVid vid) {
        return mVid != null && Objects.equals(mVid, vid.getVid());
    }

    public boolean isSameLiveRoom(MessageLiveClose message) {
        return mUid == message.getUserid() || isSameConversationRoom(message);
    }

    private final SimpleMessageListener messageListener = new SimpleMessageListener() {
        @Override
        public void onReceiveMessageGift(MessageGift message) {

            if (!isSameConversationRoom(message)) return;
            if (mGiftBeans == null) return;
            int index = -1;
            for (int i = 0; i < mGiftBeans.size(); i++) {
                if (Objects.equals(mGiftBeans.get(i).getId(), message.getGiftId())) {
                    index = i;
                    break;
                }
            }
            if (index >= 0) {
                GiftBean bean = mGiftBeans.get(index);
                if (!LiveConfig.isBlockGiftShown() && !TextUtils.isEmpty(bean.getSwf())) {
                    String swf = bean.getSwf();
                    if (swf.startsWith("http://")) {
                        swf = swf.replace("http://", "https://");
                    }
                    playSVGA(swf);
                }
                MessageMsg record = new MessageMsg();
                record.setAction("send");
                record.setSenderNickname(message.getSenderNickname());
                record.setSender("");
                record.setVid(message.getVid());
                record.setSenderExp(1);
                record.setRoomType(RoomType.PAGE_CHAT_GLOBAL);
                record.setMsgType(-1);
                record.setText(bean.getGiftname());
                ChatWebSocketManager.getInstance().pushMessage(record);
            }
        }

        @Override
        public void onReceiveMessageLiveClose(MessageLiveClose message) {
            super.onReceiveMessageLiveClose(message);
            if (isSameLiveRoom(message) && !isClose) {
                isClose = true;
                if (!mIsRemoteCasted) {
                    binding.widgetExoPlayer.release();
                    mAudioStateManager.abandonAudioFocus();

                }
            }
        }

        @Override
        public void onReceiveMessageOpen(MessageOpen message) {
            if ((mLiveRoomInfo == null && mLiveUserInfo == null)){
                viewModel.getRoomInfo(mUid);
            }

        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        ChatWebSocketManager.getInstance().start();
        ChatWebSocketManager.getInstance().registerMessageListener(messageListener);
    }


    @Override
    protected void onPause() {
        super.onPause();
        ChatWebSocketManager.getInstance().unregisterMessageListener(messageListener);
    }

    private void initVideoPlayer(@NonNull LiveRoomInfoBean bean, boolean isShowAds) {
        String mStreamType = bean.getStream_type();
        QualityBean clarity = bean.getClarity();
        if (clarity == null || TextUtils.isEmpty(clarity.getSmooth()) || TextUtils.isEmpty(clarity.getHd())) {
            binding.buttonBack.setVisibility(View.VISIBLE);
            return;
        }
        binding.buttonBack.setVisibility(View.GONE);
        String hd = clarity.getHd();
        String smooth = clarity.getHd();
        String mSource;
        @MediaQuality int qualityType;
        if (LiveConfig.isVisitor()) {
            qualityType = MediaQuality.SD;
            mSource = smooth;
        } else {
            qualityType = MediaQuality.HD;
            mSource = hd;
        }
        ArrayList<String> qualityList = new ArrayList<>();
        qualityList.add(smooth);
        qualityList.add(hd);

        LiveVideoModel model = new LiveVideoModel();
        model.setType(mType);
        model.setGameId(bean.getGame_id());
        model.setMatchId(mMatchId);
        model.setStreamType(mStreamType);
        model.setQualityList(qualityList);
        model.setQualityType(qualityType);

        Uri uri = Uri.parse(mSource);
        String scheme = uri.getScheme();
        String path = uri.getPath();

        int sourceType = StreamSourceType.SOURCE_FLV;
        if (scheme != null) {
            if (scheme.startsWith("artc")) {
                sourceType = StreamSourceType.SOURCE_ARTC;
            } else if (scheme.startsWith("rtmp")) {
                sourceType = StreamSourceType.SOURCE_RTMP;
            } else if (path != null) {
                if (path.endsWith(".m3u8")) {
                    sourceType = StreamSourceType.SOURCE_M3U8;
                } else if (path.endsWith(".mp4")) {
                    sourceType = StreamSourceType.SOURCE_MP4;
                }
            }
        }
        model.setSourceType(sourceType);
        String loadingBar = bean.getLoadingBar();
        if (!TextUtils.isEmpty(loadingBar)) {
            model.setLoadingBar(loadingBar);
        }
        binding.widgetExoPlayer.setVisibility(View.VISIBLE);
        binding.widgetExoPlayer.release();
        binding.widgetExoPlayer.initialize(model);
        binding.widgetExoPlayer.start();
        binding.widgetExoPlayer.playBufferingAds(isShowAds ? 3 : 0);
        mAudioStateManager.requestAudioFocus();
//        mEnterRoomTime = System.currentTimeMillis() /  1000;
//        mvpPresenter.recordEnterTime(mUid);
    }


    private void initAudioManager() {
        // 管理音频
        mAudioStateManager = new AudioStateManager(this);
        mAudioStateManager.registerListener(new AudioStateManager.AudioStateListener() {
            @Override
            public void onAudioFocusRequested() {

                binding.widgetExoPlayer.pause();
            }

            @Override
            public void onAudioFocusGained() {

                binding.widgetExoPlayer.resume();
                mAudioStateManager.requestAudioFocus();
            }

            @Override
            public void onVolumeLoweringRequested() {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            binding.widgetExoPlayer.forceFullScreen();
        } else if (mLiveDetailPage == null || mLiveDetailPage.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (isInPictureInPictureMode()) {
                releaseSession();
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        clearSVGA();
        binding.widgetExoPlayer.setOnStartAdsFinishListener(null);

        binding.widgetExoPlayer.release();
        mAudioStateManager.abandonAudioFocus();

        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            onBackPressed();
            return true;
        }

        // 继续执行父类其他点击事件
        return super.onKeyUp(keyCode, event);
    }

    private Dialog mShareDialog;

    public void showShareLiveDialog() {
        if (mLiveRoomInfo == null) return;
        if (mShareDialog == null) {
            //分享域名地址
            String baseDomain = DomainUtil.getApiUrl();

            if (!baseDomain.startsWith("https://") && !baseDomain.startsWith("http://")) {
                baseDomain = "https://" + baseDomain;
            }
            if (!baseDomain.endsWith("/")) {
                baseDomain = baseDomain + "/";
            }
            String base = baseDomain + "room/" + mLiveRoomInfo.getUid() + "?channel_code=" + LiveConfig.getChannelCode();
            mShareDialog = DialogUtil.showShareLiveDialog(this, base, s -> {
                if (!TextUtils.isEmpty(s)) {
                    ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    manager.setPrimaryClip(ClipData.newPlainText("link", s));
                    ToastUtils.showShort(getString(R.string.copy_success));
                }
            });
        }
        if (!mShareDialog.isShowing()) {
            mShareDialog.show();
        }
    }



    public void clearSVGA() {
        mParseCompletion.release();
        mPriorityQueue.clear();
        binding.ivSvga.stopAnimation();
        binding.ivSvga.setVisibility(View.GONE);
    }

    public void playSVGA(String swf) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (ScreenUtils.isLandscape() || isInPictureInPictureMode()) return;
        }
        GiftConsumer consumer = new GiftConsumer();
        consumer.swf = swf;
        consumer.swfTime = System.currentTimeMillis();

        mPriorityQueue.add(consumer);
        if (mPriorityQueue.size() == 1) {
            playSVGA();
        }
    }

    @Override
    public void onComplete(@NonNull SVGAVideoEntity svgaVideoEntity) {
        binding.ivSvga.setVisibility(View.VISIBLE);
        binding.ivSvga.setVideoItem(svgaVideoEntity);
        binding.ivSvga.startAnimation();
        binding.ivSvga.setCallback(new SVGACallback() {
            @Override
            public void onPause() {

            }

            @Override
            public void onFinished() {
                mPriorityQueue.poll();
                binding.ivSvga.stopAnimation();
                binding.ivSvga.setVisibility(View.GONE);
                if (!mPriorityQueue.isEmpty()) {
                    playSVGA();
                }
            }

            @Override
            public void onRepeat() {

            }

            @Override
            public void onStep(int i, double v) {

            }
        });
    }


    @Override
    public void onError() {
        mPriorityQueue.poll();
        binding.ivSvga.stopAnimation();
        binding.ivSvga.setVisibility(View.GONE);
        if (!mPriorityQueue.isEmpty()) {
            playSVGA();
        }
    }

    @Override
    public void onPlay(@NonNull List<? extends File> list) {

    }

    private final ParseCompletion mParseCompletion = new ParseCompletion(this, this);
    private void playSVGA() {
        if(LiveConfig.isBlockGiftShown()){
            mPriorityQueue.clear();
            return;
        }
        if (mPriorityQueue.isEmpty()) return;
        try {
            GiftConsumer consumer = mPriorityQueue.peek();
            if (consumer == null)return;
            mSvgaParser.decodeFromURL(new URL(consumer.swf), mParseCompletion, mParseCompletion);
        } catch (MalformedURLException e) {

        }
    }


    private WindowInsetsControllerCompat getInsetsController() {
        return WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
    }

    private void hideSystemBar() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat controller = getInsetsController();
            controller.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            controller.hide(WindowInsetsCompat.Type.systemBars());
        } else {
            BarUtils.setStatusBarVisibility(getWindow(), false);
            BarUtils.setNavBarColor(this, Color.parseColor("#000000"));
        }
    }

    private void showSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat controller = getInsetsController();
            controller.show(WindowInsetsCompat.Type.systemBars());
        } else {
            int color = MaterialColors.getColor(binding.getRoot(), android.R.attr.statusBarColor);
            BarUtils.setStatusBarVisibility(getWindow(), true);
            BarUtils.setNavBarColor(this, color);
            BarUtils.setStatusBarColor(this, color);
        }
    }


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        mIsInFreeFormMode = WindowModeUtils.isFreeFormMode(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            hideSystemBar();
            binding.mainPage.setVisibility(View.GONE);
        } else {
            binding.mainPage.setVisibility(View.VISIBLE);
            showSystemBar();
        }
    }


    private void checkAndEnterPipMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (!mIsRemoteCasted && !isInPictureInPictureMode() &&
                    PipModeUtils.isSupportPipMode(this) &&
                    PipModeUtils.isPipPermissionGranted(this) &&
                    (!isInMultiWindowMode() && !mIsInFreeFormMode)
            ) {
                enterPipMode();
            }
        }else {
            ToastUtils.showShort("当前系统版本不支持画中画模式");
        }
    }


    private void initPipSession() {
        if (mSession == null) {
            mSession = new MediaSessionCompat(this, "LiveDetailActivity");
            mSession.setActive(true);
            MediaControllerCompat.setMediaController(this, mSession.getController());

            MediaMetadataCompat metadata = new MediaMetadataCompat.Builder()
                    .build();
            mSession.setMetadata(metadata);

            MediaSessionCallback mMediaSessionCallback = new MediaSessionCallback(binding.widgetExoPlayer);
            mSession.setCallback(mMediaSessionCallback);
        }

        long actions =
                PlaybackStateCompat.ACTION_PLAY
                        | PlaybackStateCompat.ACTION_PAUSE
                        | PlaybackStateCompat.ACTION_PLAY_PAUSE;
        updateSessionPlaybackState(actions);
    }

    private void updateSessionPlaybackState(long actions) {
        if (mSession == null) return;
        @PlaybackStateCompat.State int state = binding.widgetExoPlayer.isPlaying() ? PlaybackStateCompat.STATE_PLAYING : PlaybackStateCompat.STATE_PAUSED;
        PlaybackStateCompat.Builder builder =
                new PlaybackStateCompat.Builder()
                        .setActions(actions)
                        .setActiveQueueItemId(mUid)
                        .setState(state, binding.widgetExoPlayer.getContentPosition(), 1.0f);
        mSession.setPlaybackState(builder.build());
    }

    private void releaseSession() {

        if (mSession == null) return;
        mSession.release();
        mSession = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void enterPipMode() {

        initPictureInPictureParamsBuilder();
        boolean success = enterPictureInPictureMode(mPictureInPictureParamsBuilder.build());
        if(success){
            initPipSession();
            dismissAllDialog();
            binding.widgetExoPlayer.hideQualityDialog();
            binding.widgetExoPlayer.hideController();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initPictureInPictureParamsBuilder() {
        mPictureInPictureParamsBuilder = new PictureInPictureParams.Builder();
        Rational rational = new Rational(16, 9);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            mPictureInPictureParamsBuilder.setSeamlessResizeEnabled(true);
            mPictureInPictureParamsBuilder.setSourceRectHint(mPipTransformRect);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                mPictureInPictureParamsBuilder.setExpandedAspectRatio(rational);
            }
        }
        mPictureInPictureParamsBuilder.setAspectRatio(rational);
    }


    @Override
    public boolean onPictureInPictureRequested() {
        return true;

    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode) {

        if (isInPictureInPictureMode) {
            dismissAllDialog();
            binding.widgetExoPlayer.hideQualityDialog();
            binding.widgetExoPlayer.setUseController(false);
            binding.widgetExoPlayer.hideController();
        } else {
            binding.widgetExoPlayer.setUseController(true);
            binding.widgetExoPlayer.showController();
        }
        super.onPictureInPictureModeChanged(isInPictureInPictureMode);
    }

    private void dismissAllDialog() {

        if (mGiftDialog != null && mGiftDialog.isShowing()) mGiftDialog.dismiss();
        if (mShareDialog != null && mShareDialog.isShowing()) mShareDialog.dismiss();
//        if (mReportDialog != null && mReportDialog.isShowing()) mReportDialog.dismiss();
//        if (mEnvelopDialog != null && mEnvelopDialog.isShowing()) mEnvelopDialog.dismiss();
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        checkAndEnterPipMode();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
    }


    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        super.onMultiWindowModeChanged(isInMultiWindowMode);
    }


}
