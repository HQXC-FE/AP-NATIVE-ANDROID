package com.xtree.live.player;

import static androidx.media3.common.Player.COMMAND_PLAY_PAUSE;
import static androidx.media3.common.Player.COMMAND_PREPARE;
import static androidx.media3.common.Player.COMMAND_SEEK_TO_DEFAULT_POSITION;
import static androidx.media3.common.Player.STATE_BUFFERING;
import static androidx.media3.common.Player.STATE_ENDED;
import static androidx.media3.common.Player.STATE_IDLE;
import static androidx.media3.common.Player.STATE_READY;
import static androidx.media3.exoplayer.DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS;
import static androidx.media3.exoplayer.DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS;
import static androidx.media3.exoplayer.DefaultLoadControl.DEFAULT_MAX_BUFFER_MS;
import static androidx.media3.exoplayer.DefaultLoadControl.DEFAULT_MIN_BUFFER_MS;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Player;
import androidx.media3.common.Timeline;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DefaultHttpDataSource;
import androidx.media3.datasource.HttpDataSource;
import androidx.media3.datasource.rtmp.RtmpDataSource;
import androidx.media3.exoplayer.DefaultLivePlaybackSpeedControl;
import androidx.media3.exoplayer.DefaultLoadControl;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.LivePlaybackSpeedControl;
import androidx.media3.exoplayer.hls.HlsMediaSource;
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.source.MediaSourceEventListener;
import androidx.media3.exoplayer.source.ProgressiveMediaSource;
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector;
import androidx.media3.exoplayer.trackselection.TrackSelector;
import androidx.media3.exoplayer.util.EventLogger;
import androidx.media3.ui.PlayerControlView;
import androidx.media3.ui.PlayerView;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.xtree.live.LiveConfig;
import com.xtree.live.R;
import com.xtree.live.SPKey;
import com.xtree.live.inter.OnStartAdsFinishListener;
import com.xtree.live.inter.VideoControllerListener;
import com.xtree.live.uitl.DpUtil;
import com.xtree.live.uitl.ViewUtil;

import java.util.List;

import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

public class WidgetExoPlayer extends ConstraintLayout {

    private ViewConfiguration mViewConfiguration;
    private GestureDetectorCompat mDetector;
    private static final String TAG = "WidgetExoPlayer";

    public WidgetExoPlayer(@NonNull Context context) {
        super(context);
        initialize();
    }

    public WidgetExoPlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public WidgetExoPlayer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private ExoPlayer mExoPlayer;
    private ExoPlayerView mPlayerView;
    private ImageView mFullscreen, mPlayPause;
    private TextView mQuality,mAnchor;
    private ImageView mChangeLine;
    private VideoControllerListener mListener;
    private List<String> mQualityList;
    private PopupWindow mPopupWindow;

    //需要刷新直播流的最大时间间隔 停止播放超过这个时间 开始播放时需要重新reach live edge
    private final int LIVE_MAX_OFFSET_MS = 3000;
    //在超过loading delay time 后 loading还没有加载完成loading 才会显示
    private final int LOADING_DELAY_TIME_LIVE = 500;
    private final int LOADING_DELAY_TIME_VIDEO = 200;

    private InnerHandler mHandler;
    // mock 需要用到的视频信息
    private int mMatchId;
    private int mType;
    private int mGameId;

    private int mBufferingTimes = 0;
    private int mBufferingDuration = 0;

    private final static int MAX_ERROR_RETRY_TIME = 3;
    private int mErrorRetryTime;
    private int mPlayErrorCount;

    private int mQualityType;

    private ViewGroup mBrightnessVolumeLayout;
    private ImageView mBrightnessVolumeIcon;
    private ProgressBar mBrightnessVolumeProgress;

    private Animation animateOut,animateIn;

    private float mStartDownX, mStartDownY;

    private float mCurrentBrightnessPercent;
    private float mCurrentVolumePercent;

    private boolean mIsUseAppVolume;

    private boolean ignoreFirstScroll;

    private static final int MAX = 100;
    private static final int APP_MAX_VOLUME = MAX;
    private static final int HALF = MAX / 2;

    private static final int MAX_BRIGHTNESS = 255;

    private AudioManager mAudioManager;

    private boolean mEnableAdjust = true;
    private int MAX_SYSTEM_VOLUME;

    private PlayerControlView mController;

    private View mLiveAdOverlay;
    private TextView mLiveAdOverlayCountDown;
    private ImageView mLiveAdOverlayImage;

    private ImageView mPipButton, mShareButton;
    private ImageView mReportButton;
    private ImageView mSettingButton;

    private WidgetExoPlayerTimeBar mPlayerSeekBar;

    private OnRefreshSourceListener mOnRefreshSourceListener;

    //遇到播放403时候 是否重新请求直播推流地址
    private boolean _403RefreshSource = false;
    @SuppressLint("MissingInflatedId")
    private void initialize() {
        this.animateOut = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out);
        animateOut.setDuration(300);
        this.animateIn = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
        animateIn.setDuration(300);

        mViewConfiguration = ViewConfiguration.get(getContext());

        mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        MAX_SYSTEM_VOLUME = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        //初始化
        mIsUseAppVolume = false;
        if (mIsUseAppVolume) {
            mCurrentVolumePercent = 1f;
        } else {
            mCurrentVolumePercent = 1.0f * mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) / MAX_SYSTEM_VOLUME;
        }
        try {
            mCurrentBrightnessPercent = 1.0f * Settings.System.getInt(getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS) / MAX_BRIGHTNESS;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        mDetector = new GestureDetectorCompat(getContext(), new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onScroll(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
                if (distanceY == 0f) return true;
                if (!ignoreFirstScroll) {
                    LogUtils.d(TAG, "before distanceX:" + distanceX + "  distanceY :" + distanceY);
                    distanceY = distanceY / Math.abs(distanceY) * Math.min(mViewConfiguration.getScaledTouchSlop(), Math.abs(distanceY));
                    LogUtils.d(TAG, "after distanceX:" + distanceX + "  distanceY :" + distanceY);
                    float percent;
                    int height = mViewConfiguration.getScaledTouchSlop() * 30;
                    if (mStartDownX * 2f > getWidth()) {//右半
                        mBrightnessVolumeIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_player_volume, getContext().getTheme()));
                        if (height > 0) {
                            mCurrentVolumePercent += distanceY / height;
                        }
                        mCurrentVolumePercent = Math.min(Math.max(mCurrentVolumePercent, 0), 1);
                        percent = mCurrentVolumePercent;
                        LogUtils.d(TAG, "volume:" + percent);

                        if (mIsUseAppVolume) {
                            int volume = (int) (APP_MAX_VOLUME * percent);
                            if (mExoPlayer != null) mExoPlayer.setVolume(percent);
                            mBrightnessVolumeProgress.setMax(APP_MAX_VOLUME);
                            mBrightnessVolumeProgress.setProgress(volume);
                        } else {
                            int volume = (int) (MAX_SYSTEM_VOLUME * percent);
                            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
                            if (mExoPlayer != null) mExoPlayer.setVolume(1);
                            mBrightnessVolumeProgress.setMax(MAX_SYSTEM_VOLUME);
                            mBrightnessVolumeProgress.setProgress(volume);
                        }

                    } else {
                        mBrightnessVolumeIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_player_brightness, getContext().getTheme()));
                        if (height > 0) {
                            mCurrentBrightnessPercent += distanceY / height;
                        }
                        percent = mCurrentBrightnessPercent = Math.min(Math.max(mCurrentBrightnessPercent, 0), 1);
                        Window window = ((Activity) getContext()).getWindow();
                        WindowManager.LayoutParams attributes = window.getAttributes();
                        LogUtils.d(TAG, "brightness:" + percent);
                        attributes.screenBrightness = percent;
                        window.setAttributes(attributes);
                        int progress = (int) (MAX_BRIGHTNESS * percent);
                        LogUtils.d(TAG, "progress:" + progress);
                        mBrightnessVolumeProgress.setMax(MAX_BRIGHTNESS);
                        mBrightnessVolumeProgress.setProgress(progress);
                    }
                    showVolume();
                    return true;
                } else {
                    ignoreFirstScroll = false;
                }
                return true;
            }
        });
        LayoutInflater.from(getContext()).inflate(
                R.layout.widget_exoplayer, this, true);

        mPipButton = findViewById(R.id.btnPip);
        mShareButton = findViewById(R.id.btnShare);
        mSettingButton = findViewById(R.id.btnSettings);
        mReportButton = findViewById(R.id.btnReport);


        mPlayerView = findViewById(R.id.exoplayer_view);
        mPlayerSeekBar = findViewById(R.id.exo_progress);
        mLiveAdOverlay = findViewById(R.id.live_ad_overlay);
        mLiveAdOverlayCountDown = findViewById(R.id.live_ad_overlay_countdown);
        mLiveAdOverlayImage = findViewById(R.id.live_ad_overlay_image);

        mController = findViewById(R.id.exo_controller);

        mFullscreen = mPlayerView.findViewById(R.id.exo_fullscreen);
//        mFullscreen.setImageResource(R.drawable.exo_icon_fullscreen_enter);
        mPlayPause = findViewById(R.id.exo_play_pause);
        mQuality = mPlayerView.findViewById(R.id.exo_quality);
        mAnchor = mPlayerView.findViewById(R.id.exo_anchor);
        mChangeLine = mPlayerView.findViewById(R.id.exo_change_line);

        Glide.with(this).asGif().load(R.drawable.live_change_line).into(mChangeLine);

        mBrightnessVolumeLayout = findViewById(R.id.brightnessVolumeLayout);
        mBrightnessVolumeLayout.setVisibility(View.GONE);
        mBrightnessVolumeIcon = findViewById(R.id.brightnessVolumeIcon);
        mBrightnessVolumeProgress = findViewById(R.id.brightnessVolumeProgress);
        mBrightnessVolumeProgress.setMax(MAX);
        mPopupWindow = new PopupWindow();
        mHandler = new InnerHandler(Looper.getMainLooper());
    }


    public void setOnRefreshSourceListener(OnRefreshSourceListener listener){
        this.mOnRefreshSourceListener = listener;
    }
    void setUseAppVolume(boolean isUseAppVolume) {
        this.mIsUseAppVolume = isUseAppVolume;
    }

    public void setEnableAdjust(boolean mEnableAdjust) {
        this.mEnableAdjust = mEnableAdjust;
    }

    @OptIn(markerClass = UnstableApi.class)
    public void initialize(LiveVideoModel model) {
        setAdsImageHeightPercentAndRatio(1f, model.getDimensionRatio());
        ConstraintLayout.LayoutParams layoutParams = (LayoutParams) mPlayerView.getLayoutParams();
        layoutParams.dimensionRatio = model.getDimensionRatio();
        mQualityList = model.getQualityList();
        mQualityType = model.getQualityType();
        @StreamSourceType int sourceType = model.getSourceType();
        mMatchId = model.getMatchId();
        mType = model.getType();
        mGameId = model.getGameId();
        if (mQualityList.isEmpty()) return;
        String mediaSourcePath;
        if (mQualityType == MediaQuality.HD && mQualityList.size() >= 2) {
            mQuality.setText(getContext().getString(R.string.live_quality_high));
            mediaSourcePath = mQualityList.get(1);
        } else {
            mQuality.setText(getContext().getString(R.string.live_quality_smooth));
            mediaSourcePath = mQualityList.get(0);
        }
//        mediaSourcePath = "https://stream01aa.shdihgs.cn/live/5_1742788360.m3u8?txSecret=9446dcea6d65b5af2bcb9070b0cb215b&txTime=67e12885";
//        mPlayerSeekBar.setVisibility(mIsLiveStream ? GONE : VISIBLE);
        mQuality.setOnClickListener(v -> showQuality(v, sourceType));

        initPlayer();
        MediaSource mediaSource = getSourceMedia(sourceType, mediaSourcePath);
        if (mediaSource == null) return;
        mediaSource.addEventListener(mHandler, new MediaSourceEventListener() {});
        if (mExoPlayer != null) mExoPlayer.setMediaSource(mediaSource);
    }


    @OptIn(markerClass = UnstableApi.class)
    private void initPlayer() {
        TrackSelector selector = new DefaultTrackSelector(getContext());
        LivePlaybackSpeedControl livePlaybackSpeedControl = new DefaultLivePlaybackSpeedControl.Builder()
                .setFallbackMaxPlaybackSpeed(1.04f)
                .build();
        ExoPlayer.Builder builder = new ExoPlayer.Builder(getContext());
        builder.setTrackSelector(selector)
                .setLoadControl(
                        new DefaultLoadControl.Builder()
                                .setBufferDurationsMs(
                                        DEFAULT_MIN_BUFFER_MS,
                                        DEFAULT_MAX_BUFFER_MS,
                                        DEFAULT_BUFFER_FOR_PLAYBACK_MS,
                                        DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS)
                                .build())
                .setMediaSourceFactory(new DefaultMediaSourceFactory(getContext()).setLiveMaxOffsetMs(LIVE_MAX_OFFSET_MS).setLiveTargetOffsetMs(LIVE_MAX_OFFSET_MS))
                .setLivePlaybackSpeedControl(livePlaybackSpeedControl);

        mExoPlayer = builder.build();
        mExoPlayer.addListener(new Player.Listener() {
            private long bufferingStartTime;

            @Override
            public void onPlayerError(@NonNull PlaybackException error) {
                if (error.errorCode == PlaybackException.ERROR_CODE_BEHIND_LIVE_WINDOW) {
                    // Re-initialize player at the live edge.播放位置可能会落后于直播窗口
                    mExoPlayer.seekToDefaultPosition();
                    mExoPlayer.prepare();
                    mExoPlayer.setPlayWhenReady(true);
                    return;
                } else  if(error.getCause() instanceof HttpDataSource.InvalidResponseCodeException){
                    Throwable cause = error.getCause();
                    //可能由于sourcePath过期  刷新后重试
                    if(((HttpDataSource.InvalidResponseCodeException)cause).responseCode == 403){
                        LogUtils.d(TAG, "InvalidResponseCodeException 403");
                        if(mOnRefreshSourceListener != null && !_403RefreshSource){
                            _403RefreshSource = true;
                            mErrorRetryTime++;
                            mOnRefreshSourceListener.onRefresh();

                            mPlayErrorCount++;
                            return;
                        }
                    }
                }

                LogUtils.d(TAG, "onPlayerError :" + error.getErrorCodeName());
                mPlayErrorCount++;

                if (mExoPlayer == null) return;
                stop();
                if (mErrorRetryTime >= MAX_ERROR_RETRY_TIME) {
                    mErrorRetryTime = 0;
                    OnClickListener clickListener = v -> {
                        mPlayPause.setOnClickListener((view) -> checkAndDispatchPlayPause());
                        start();
                    };
                    mPlayPause.setOnClickListener(clickListener);
                } else {
                    mErrorRetryTime++;
                    start();
                }
            }

            @Override
            public void onPositionDiscontinuity(Player.PositionInfo oldPosition, Player.PositionInfo newPosition, int reason) {
                Player.Listener.super.onPositionDiscontinuity(oldPosition, newPosition, reason);
            }



            @Override
            public void onTimelineChanged(Timeline timeline, int reason) {
                LogUtils.d(TAG, "onTimelineChanged");

            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                LogUtils.d(TAG, "onIsPlayingChanged");
                if (mListener != null) mListener.onIsPlayingChanged(isPlaying);
            }


            @Override
            public void onPlaybackStateChanged(int playbackState) {
                LogUtils.d(TAG, "onPlaybackStateChanged");
                if (playbackState == STATE_BUFFERING) {
                    //卡顿记录
                    mBufferingTimes++;
                    bufferingStartTime = SystemClock.elapsedRealtime();
                } else if (playbackState == STATE_READY) {
                    _403RefreshSource = false;
                    mErrorRetryTime = 0;
                    if (bufferingStartTime > 0) {
                        mBufferingDuration += SystemClock.elapsedRealtime() - bufferingStartTime;
                    }
                }else if (playbackState == STATE_IDLE) {

                }else if (playbackState == STATE_ENDED) {

                }
            }


            @Override
            public void onPlayWhenReadyChanged(boolean playWhenReady, int reason) {
                LogUtils.d(TAG, "onPlayWhenReadyChanged");
                if (reason == Player.PLAY_WHEN_READY_CHANGE_REASON_USER_REQUEST) {
                    bufferingStartTime = 0;
                }
            }
        });

        mPlayPause.setOnClickListener(v -> checkAndDispatchPlayPause());




        mPlayerView.setControllerVisibilityListener((PlayerView.ControllerVisibilityListener) visibility -> {
            if (!mIsAdsComplete) return;
            mPlayPause.setVisibility(visibility);
            if (mListener != null) mListener.isControllerVisible(visibility == View.VISIBLE);
        });
        mPlayerView.setControllerAutoShow(true);
        mPlayerView.setControllerHideOnTouch(true);
        mPlayerView.setControllerShowTimeoutMs(2000);
        mPlayerView.setShowBuffering(PlayerView.SHOW_BUFFERING_NEVER);
        mPlayerView.setErrorMessageProvider(new PlayerErrorMessageProvider());
        mPlayerView.setFullscreenButtonClickListener(isFullScreen -> {
            if (mListener == null) return;

            mListener.onFullscreenButtonClick();
        });

        mPlayerView.requestFocus();
        mPlayerView.setPlayer(mExoPlayer);
    }


    public long getContentPosition() {
        if (mExoPlayer == null) return 0L;
        return mExoPlayer.getContentPosition();
    }

    private void checkAndDispatchPlayPause() {
        if (mExoPlayer == null) return;
        @Player.State int state = mExoPlayer.getPlaybackState();
        if (state == STATE_IDLE || state == Player.STATE_ENDED || !mExoPlayer.getPlayWhenReady()) {
            mLiveAdOverlay.setVisibility(GONE);
            //对于直播，暂停超过一定时间 需要刷新跟上 live edge
            if (isLiveStreaming()) {
                mExoPlayer.seekToDefaultPosition();
                mExoPlayer.prepare();
                mExoPlayer.setPlayWhenReady(true);
            } else {
                dispatchPlay(mExoPlayer);
            }
        } else {
            setAdsImageHeightPercentAndRatio(0.8f, null);
            mLiveAdOverlay.setVisibility(VISIBLE);
            dispatchPause(mExoPlayer);
        }
    }

    private void setAdsImageHeightPercentAndRatio(float percent, String ratio) {
        if(mLiveAdOverlayImage.getLayoutParams() instanceof ConstraintLayout.LayoutParams){
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mLiveAdOverlayImage.getLayoutParams();
            layoutParams.matchConstraintPercentHeight = percent;
            if(!TextUtils.isEmpty(ratio)){
                layoutParams.dimensionRatio = ratio;
            }
            mLiveAdOverlayImage.setLayoutParams(layoutParams);
        }
    }


    private OnStartAdsFinishListener onStartAdsFinishListener;
    public void setOnStartAdsFinishListener(OnStartAdsFinishListener onStartAdsFinishListener) {
        this.onStartAdsFinishListener = onStartAdsFinishListener;
    }

    private void dispatchPause(Player player) {
        if (player.isCommandAvailable(COMMAND_PLAY_PAUSE)) {
            player.pause();
        }
    }

    private void dispatchPlay(Player player) {
        @Player.State int state = player.getPlaybackState();
        if (state == Player.STATE_IDLE && player.isCommandAvailable(COMMAND_PREPARE)) {
            player.prepare();
        } else if (state == Player.STATE_ENDED
                && player.isCommandAvailable(COMMAND_SEEK_TO_DEFAULT_POSITION)) {
            player.seekToDefaultPosition();
        }
        if (player.isCommandAvailable(COMMAND_PLAY_PAUSE)) {
            player.play();
        }
    }

    /**
     * 是否是直播
     * */
    private boolean isLiveStreaming() {
        return (mExoPlayer != null && mExoPlayer.isCurrentMediaItemLive()) || mPlayerSeekBar.getVisibility() != VISIBLE;
    }

    public void setVideoControllerListener(VideoControllerListener listener) {
        mListener = listener;
    }


    public boolean isPlaying() {
        if (mExoPlayer == null) return false;
        return mExoPlayer.isPlaying();
    }

    @OptIn(markerClass = UnstableApi.class)
    public void start() {
        if (mExoPlayer == null) return;
        mExoPlayer.prepare();
        mExoPlayer.seekToDefaultPosition();
        mExoPlayer.setPlayWhenReady(true);
    }


    /**
     * 广告是否播放结束
     */
    private boolean mIsAdsComplete;

    @OptIn(markerClass = UnstableApi.class)
    public void playBufferingAds(int countDown) {
        if(countDown <= 0) {
            mHandler.postDelayed(()->{
                mLiveAdOverlay.setVisibility(GONE);
            }, 500);
            return;
        }
        mIsAdsComplete = false;
        if (mExoPlayer != null) mExoPlayer.setVolume(0);
        mLiveAdOverlay.setVisibility(VISIBLE);
        mLiveAdOverlayCountDown.setVisibility(VISIBLE);
        mLiveAdOverlayCountDown.setText(String.valueOf(countDown));
        delayPlayBufferingAds(countDown - 1);
    }

    @OptIn(markerClass = UnstableApi.class)
    private void delayPlayBufferingAds(int currentCountdown) {
        mHandler.postDelayed(() -> {
            if (currentCountdown > 0) {
                mLiveAdOverlayCountDown.setText(String.valueOf(currentCountdown));
                int newCountdown = currentCountdown - 1;
                delayPlayBufferingAds(newCountdown);
            } else {
                mIsAdsComplete = true;
                if (mExoPlayer != null) mExoPlayer.setVolume(1);
                mPlayerView.setUseController(true);
                mLiveAdOverlayCountDown.setVisibility(GONE);
                mLiveAdOverlay.setVisibility(GONE);
                mPlayerView.setShowBuffering(PlayerView.SHOW_BUFFERING_ALWAYS);
                if(onStartAdsFinishListener != null)onStartAdsFinishListener.onStartAdsFinish();
            }
        }, 1000);
    }

    @OptIn(markerClass = UnstableApi.class)
    public void start(long position) {
        if (mExoPlayer == null) return;
        mExoPlayer.prepare();
        mExoPlayer.seekTo(position);
        mExoPlayer.setPlayWhenReady(true);
    }

    public void stop() {
        if (mExoPlayer == null) return;
        dispatchPause(mExoPlayer);
        mExoPlayer.stop();
    }

    public void resume() {

        if (mExoPlayer != null && !mExoPlayer.isPlaying()) mPlayPause.performClick();
    }

    public void pause() {

        if (mExoPlayer != null && mExoPlayer.isPlaying()) mPlayPause.performClick();
    }


    public void release() {
        if (mExoPlayer == null) return;
        stop();
        mExoPlayer.release();
        mExoPlayer = null;
        mPlayerView.setPlayer(null);
    }

    @OptIn(markerClass = UnstableApi.class)
    private void showQuality(View view, @StreamSourceType int sourceType) {
        View popUpView = LayoutInflater.from(getContext()).inflate(R.layout.view_live_quality, null);
        popUpView.findViewById(R.id.quality_high).setOnClickListener(view1 -> {
            mPopupWindow.dismiss();
//            if (LiveConfig.isVisitor()) {
//                ToastUtils.showShort(getContext().getString(R.string.please_login));
//                return;
//            }
            if (!LiveConfig.isLogin()) {
                ToastUtils.showShort(getContext().getString(R.string.please_login));
                return;
            }
            if (!mQualityList.isEmpty() && mQualityList.size() >= 2) {
                String qualitySource = mQualityList.get(1);
                if (mQualityType == MediaQuality.HD) return;
                mQualityType = MediaQuality.HD;
                mQuality.setText(getContext().getString(R.string.live_quality_high));
                MediaSource mediaSource = getSourceMedia(sourceType, qualitySource);
                //不支持的格式
                if (mediaSource == null) return;
                long position = mExoPlayer.getCurrentPosition();
                mExoPlayer.setMediaSource(mediaSource);
                stop();
                start(position);
            }
        });
        popUpView.findViewById(R.id.quality_smooth).setOnClickListener(view12 -> {
            mPopupWindow.dismiss();
            if (!mQualityList.isEmpty() && mQualityList.size() >= 2) {
                String qualitySource = mQualityList.get(0);
                if (mQualityType == MediaQuality.SD) return;
                mQualityType = MediaQuality.SD;
                mQuality.setText(getContext().getString(R.string.live_quality_smooth));
                MediaSource mediaSource = getSourceMedia(sourceType, qualitySource);
                long position = mExoPlayer.getCurrentPosition();
                //不支持的格式
                if (mediaSource == null) return;
                mExoPlayer.setMediaSource(mediaSource);
                stop();
                start(position);
            }
        });

        mPopupWindow.setContentView(popUpView);
        mPopupWindow.setWidth((int) DpUtil.dp2px(120f));
        mPopupWindow.setHeight((int) DpUtil.dp2px(60f));
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setTouchInterceptor(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    mPopupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });
        int offsetY = -((int) DpUtil.dp2px(60f) + mQuality.getHeight());
        mPopupWindow.showAsDropDown(view, 0, offsetY, Gravity.START);
        mPopupWindow.setFocusable(true);
    }

    @OptIn(markerClass = UnstableApi.class)
    private MediaSource getSourceMedia(@StreamSourceType int sourceType, String mediaSourcePath) {
        switch (sourceType) {
            case StreamSourceType.SOURCE_BASE:
            case StreamSourceType.SOURCE_MP4:
            case StreamSourceType.SOURCE_FLV:
                DataSource.Factory dataSourceFactory = () -> {
                    HttpDataSource dataSource = new DefaultHttpDataSource.Factory().createDataSource();
                    // Set a custom authentication request header.
                    dataSource.setRequestProperty("Referer", LiveConfig.getReferer());
                    return dataSource;
                };

                return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(Uri.parse(mediaSourcePath)));
            case StreamSourceType.SOURCE_M3U8:
                DataSource.Factory factory = () -> {
                    HttpDataSource dataSource = new DefaultHttpDataSource.Factory().createDataSource();
                    // Set a custom authentication request header.
                    dataSource.setRequestProperty("Referer", LiveConfig.getReferer());
                    return dataSource;
                };

                return new HlsMediaSource.Factory(factory).createMediaSource(MediaItem.fromUri(Uri.parse(mediaSourcePath)));
            case StreamSourceType.SOURCE_RTMP:
                RtmpDataSource.Factory rtmpFactory = new RtmpDataSource.Factory();
                return new ProgressiveMediaSource.Factory(rtmpFactory).createMediaSource(MediaItem.fromUri(Uri.parse(mediaSourcePath)));
        }
        return null;
    }

    public void setUseController(boolean useController) {
        mPlayerView.setUseController(useController);
    }

    public void hideQualityDialog() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    @OptIn(markerClass = UnstableApi.class)
    public void showController() {
        mPlayerView.showController();
    }

    @OptIn(markerClass = UnstableApi.class)
    public void hideController() {
        mController.hideImmediately();
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }

    private long mInitTime;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mInitTime = SystemClock.elapsedRealtime();
        registerBrightnessChange();
        registerVolumeChange();
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        unregisterBrightnessChange();
        unregisterVolumeChange();
        mHandler.removeCallbacksAndMessages(null);

    }

    public void forceFullScreen() {
        mFullscreen.performClick();
    }

    public ImageView getReportButton() {
        return mReportButton;
    }

    public ImageView getLiveAdOverlayImage() {
        return mLiveAdOverlayImage;
    }

    public View getPipButton() {
        return mPipButton;
    }

    public View getShareButton() {
        return mShareButton;
    }

    public View getSettingButton() {
        return mSettingButton;
    }

    private class InnerHandler extends Handler {
        public InnerHandler(@NonNull Looper looper) {
            super(looper);
        }

        public InnerHandler(@NonNull Looper looper, @Nullable Callback callback) {
            super(looper, callback);
        }

        static final int LOADING = 1;
        static final int HIDING = 2;

        @Override
        public void handleMessage(@NonNull Message msg) {

        }
    }





    public void addOnLayoutChangeListenerToPlayer(OnLayoutChangeListener listener) {
        mPlayerView.addOnLayoutChangeListener(listener);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private ContentObserver mBrightnessObserver;

    private void registerBrightnessChange() {
        if (mBrightnessObserver == null) {
            mBrightnessObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
                @Override
                public void onChange(boolean selfChange) {
                    try {
                        mCurrentBrightnessPercent = 1.0f * Settings.System.getInt(getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS) / MAX_BRIGHTNESS;
                        mCurrentBrightnessPercent = Math.min(Math.max(mCurrentBrightnessPercent, 0), 1);
                        Window window = ((Activity) getContext()).getWindow();
                        WindowManager.LayoutParams attributes = window.getAttributes();
                        LogUtils.d(TAG, "brightness:" + mCurrentBrightnessPercent);
                        attributes.screenBrightness = mCurrentBrightnessPercent;
                        window.setAttributes(attributes);
                    } catch (Settings.SettingNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            };
        }
        getContext().getContentResolver().registerContentObserver(
                Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS),
                true, mBrightnessObserver
        );
    }

    private void unregisterBrightnessChange() {
        if (mBrightnessObserver != null) {
            getContext().getContentResolver().unregisterContentObserver(mBrightnessObserver);
        }
    }

    private AudioVolumeChangeReceiver audioVolumeChangeReceiver;

    private void registerVolumeChange() {
        if (audioVolumeChangeReceiver == null)
            audioVolumeChangeReceiver = new AudioVolumeChangeReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.media.VOLUME_CHANGED_ACTION");
        getContext().registerReceiver(audioVolumeChangeReceiver, intentFilter);
    }

    private void unregisterVolumeChange() {
        getContext().unregisterReceiver(audioVolumeChangeReceiver);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mStartDownX = event.getX();
            mStartDownY = event.getY();
            ignoreFirstScroll = true;
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (Math.abs(event.getY() - mStartDownY) >= mViewConfiguration.getScaledTouchSlop()) {
                if (mStartDownX * 2f > getWidth() && volumePercentBySystem != -1) {
                    mCurrentVolumePercent = volumePercentBySystem;
                    volumePercentBySystem = -1;
                }
                return true;
            }
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            hideVolume();
        }
        if (mEnableAdjust && this.mDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void showVolume() {
        removeCallbacks(hideVolumeRunnable);
        mBrightnessVolumeLayout.setVisibility(VISIBLE);

    }

    private final Runnable hideVolumeRunnable = () -> {
        ViewUtil.animateOut(mBrightnessVolumeLayout, animateOut, GONE);
    };

    private void hideVolume() {
        if (mBrightnessVolumeLayout.getVisibility() == VISIBLE) {
            post(hideVolumeRunnable);
        }
    }

    private float volumePercentBySystem = -1;

    /**
     * Called when unplugging your headphones for example
     * Register when start playing music
     */
    private class AudioVolumeChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (!mIsUseAppVolume) {
                volumePercentBySystem = 1.0f * mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) / MAX_SYSTEM_VOLUME;
            }
            //Pause the music
            Log.i(TAG, "mCurrentVolumePercent：" + volumePercentBySystem);
        }
    }

    /**
     * 刷新源信息
     */
    public interface OnRefreshSourceListener{
        void onRefresh();
    }
}
