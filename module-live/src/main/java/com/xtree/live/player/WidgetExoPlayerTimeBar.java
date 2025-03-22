package com.xtree.live.player;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.media3.common.C;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.common.util.Util;
import androidx.media3.ui.TimeBar;

import com.xtree.live.R;

import org.jetbrains.annotations.NotNull;

import java.util.Formatter;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArraySet;

@UnstableApi
public class WidgetExoPlayerTimeBar extends View implements TimeBar {

    /**
     * Default height for the time bar, in dp.
     */
    public static final int DEFAULT_BAR_HEIGHT_DP = 4;
    /**
     * Default height for the touch target, in dp.
     */
    public static final int DEFAULT_TOUCH_TARGET_HEIGHT_DP = 26;
    /**
     * Default width for ad markers, in dp.
     */
    public static final int DEFAULT_AD_MARKER_WIDTH_DP = 4;
    /**
     * Default diameter for the scrubber when enabled, in dp.
     */
    public static final int DEFAULT_SCRUBBER_ENABLED_SIZE_DP = 12;
    /**
     * Default diameter for the scrubber when disabled, in dp.
     */
    public static final int DEFAULT_SCRUBBER_DISABLED_SIZE_DP = 0;
    /**
     * Default diameter for the scrubber when dragged, in dp.
     */
    public static final int DEFAULT_SCRUBBER_DRAGGED_SIZE_DP = 16;
    /**
     * Default color for the played portion of the time bar.
     */
    public static final int DEFAULT_PLAYED_COLOR = 0xFFFFFFFF;
    /**
     * Default color for the unplayed portion of the time bar.
     */
    public static final int DEFAULT_UNPLAYED_COLOR = 0x33FFFFFF;
    /**
     * Default color for the buffered portion of the time bar.
     */
    public static final int DEFAULT_BUFFERED_COLOR = 0xCCFFFFFF;
    /**
     * Default color for the scrubber handle.
     */
    public static final int DEFAULT_SCRUBBER_COLOR = 0xFFFFFFFF;
    /**
     * Default color for ad markers.
     */
    public static final int DEFAULT_AD_MARKER_COLOR = 0xB2FFFF00;
    /**
     * Default color for played ad markers.
     */
    public static final int DEFAULT_PLAYED_AD_MARKER_COLOR = 0x33FFFF00;

    /**
     * Vertical gravity for progress bar to be located at the center in the view.
     */
    public static final int BAR_GRAVITY_CENTER = 0;
    /**
     * Vertical gravity for progress bar to be located at the bottom in the view.
     */
    public static final int BAR_GRAVITY_BOTTOM = 1;
    /**
     * Vertical gravity for progress bar to be located at the top in the view.
     */
    public static final int BAR_GRAVITY_TOP = 2;

    /**
     * The threshold in dps above the bar at which touch events trigger fine scrub mode.
     */
    private static final int FINE_SCRUB_Y_THRESHOLD_DP = -50;
    /**
     * The ratio by which times are reduced in fine scrub mode.
     */
    private static final int FINE_SCRUB_RATIO = 3;
    /**
     * The time after which the scrubbing listener is notified that scrubbing has stopped after
     * performing an incremental scrub using key input.
     */
    private static final long STOP_SCRUBBING_TIMEOUT_MS = 1000;
    private static final int DEFAULT_INCREMENT_COUNT = 20;
    private static final float SHOWN_SCRUBBER_SCALE = 1.0f;
    private static final float HIDDEN_SCRUBBER_SCALE = 0.0f;

    /**
     * The name of the Android SDK view that most closely resembles this custom view. Used as the
     * class name for accessibility.
     */
    private static final String ACCESSIBILITY_CLASS_NAME = "android.widget.SeekBar";

    private final Rect mSeekBounds;
    private final Rect mProgressBar;
    private final Rect mBufferedBar;
    private final Rect mScrubberBar;
    private final Paint mPlayedPaint;
    private final Paint mBufferedPaint;
    private final Paint mUnPlayedPaint;
    private final Paint mAdMarkerPaint;
    private final Paint mPlayedAdMarkerPaint;
    private final Paint mScrubberPaint;
    private final Drawable mScrubberDrawable;
    private final int mBarHeight;
    private final int mTouchTargetHeight;
    private final int mBarGravity;
    private final int mAdMarkerWidth;
    private final int mScrubberEnabledSize;
    private final int mScrubberDisabledSize;
    private final int mScrubberDraggedSize;
    private final int mScrubberPadding;
    private final int mFineScrubYThreshold;
    private final StringBuilder mFormatBuilder;
    private final Formatter mFormatter;
    private final Runnable mStopScrubbingRunnable;
    private final CopyOnWriteArraySet<OnScrubListener> mListeners;
    private final Point mTouchPosition;
    private final float mDensity;

    private int mKeyCountIncrement;
    private long mKeyTimeIncrement;
    private int mLastCoarseScrubXPosition;
    private Rect mLastExclusionRectangle;
    private ValueAnimator mScrubberScalingAnimator;
    private float mScrubberScale;
    private boolean mScrubbing;
    private long mScrubPosition;
    private long mDuration;
    private long mPosition;
    private long mBufferedPosition;
    private int mAdGroupCount;
    private long[] mAdGroupTimesMs;
    private boolean[] mPlayedAdGroups;

    public WidgetExoPlayerTimeBar(Context context) {
        this(context, null);
    }

    public WidgetExoPlayerTimeBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WidgetExoPlayerTimeBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, attrs);
    }

    public WidgetExoPlayerTimeBar(Context context, @Nullable AttributeSet attrs,
                                  int defStyleAttr, @Nullable AttributeSet barAttrs) {
        this(context, attrs, defStyleAttr, barAttrs, 0);
    }

    // Suppress warnings due to usage of View methods in the constructor.
    @SuppressWarnings("nullness:method.invocation.invalid")
    public WidgetExoPlayerTimeBar(Context context, @Nullable AttributeSet attrs,
                                  int defStyleAttr, @Nullable AttributeSet barAttrs, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        mSeekBounds = new Rect();
        mProgressBar = new Rect();
        mBufferedBar = new Rect();
        mScrubberBar = new Rect();
        mPlayedPaint = new Paint();
        mBufferedPaint = new Paint();
        mUnPlayedPaint = new Paint();
        mAdMarkerPaint = new Paint();
        mPlayedAdMarkerPaint = new Paint();
        mScrubberPaint = new Paint();
        mScrubberPaint.setAntiAlias(true);
        mListeners = new CopyOnWriteArraySet<>();
        mTouchPosition = new Point();

        // Calculate the dimensions and paints for drawn elements.
        Resources res = context.getResources();
        DisplayMetrics displayMetrics = res.getDisplayMetrics();
        mDensity = displayMetrics.density;
        mFineScrubYThreshold = dpToPx(mDensity, FINE_SCRUB_Y_THRESHOLD_DP);
        int defaultBarHeight = dpToPx(mDensity, DEFAULT_BAR_HEIGHT_DP);
        int defaultTouchTargetHeight = dpToPx(mDensity, DEFAULT_TOUCH_TARGET_HEIGHT_DP);
        int defaultAdMarkerWidth = dpToPx(mDensity, DEFAULT_AD_MARKER_WIDTH_DP);
        int defaultScrubberEnabledSize = dpToPx(mDensity, DEFAULT_SCRUBBER_ENABLED_SIZE_DP);
        int defaultScrubberDisabledSize = dpToPx(mDensity, DEFAULT_SCRUBBER_DISABLED_SIZE_DP);
        int defaultScrubberDraggedSize = dpToPx(mDensity, DEFAULT_SCRUBBER_DRAGGED_SIZE_DP);
        if (barAttrs != null) {
            TypedArray array = context.getTheme().obtainStyledAttributes(
                    barAttrs, R.styleable.DefaultTimeBar, defStyleAttr, defStyleRes);
            try {
                mScrubberDrawable = array.getDrawable(R.styleable.DefaultTimeBar_scrubber_drawable);
                if (mScrubberDrawable != null) {
                    setDrawableLayoutDirection(mScrubberDrawable);
                    defaultTouchTargetHeight = Math.max(mScrubberDrawable.getMinimumHeight(), defaultTouchTargetHeight);
                }
                mBarHeight = array.getDimensionPixelSize(R.styleable.DefaultTimeBar_bar_height, defaultBarHeight);
                mTouchTargetHeight = array.getDimensionPixelSize(R.styleable.DefaultTimeBar_touch_target_height, defaultTouchTargetHeight);
                mBarGravity = array.getInt(R.styleable.DefaultTimeBar_bar_gravity, BAR_GRAVITY_CENTER);
                mAdMarkerWidth = array.getDimensionPixelSize(R.styleable.DefaultTimeBar_ad_marker_width, defaultAdMarkerWidth);
                mScrubberEnabledSize = array.getDimensionPixelSize(R.styleable.DefaultTimeBar_scrubber_enabled_size, defaultScrubberEnabledSize);
                mScrubberDisabledSize = array.getDimensionPixelSize(R.styleable.DefaultTimeBar_scrubber_disabled_size, defaultScrubberDisabledSize);
                mScrubberDraggedSize = array.getDimensionPixelSize(R.styleable.DefaultTimeBar_scrubber_dragged_size, defaultScrubberDraggedSize);
                int playedColor = array.getInt(R.styleable.DefaultTimeBar_played_color, DEFAULT_PLAYED_COLOR);
                int scrubberColor = array.getInt(R.styleable.DefaultTimeBar_scrubber_color, DEFAULT_SCRUBBER_COLOR);
                int bufferedColor = array.getInt(R.styleable.DefaultTimeBar_buffered_color, DEFAULT_BUFFERED_COLOR);
                int unplayedColor = array.getInt(R.styleable.DefaultTimeBar_unplayed_color, DEFAULT_UNPLAYED_COLOR);
                int adMarkerColor = array.getInt(R.styleable.DefaultTimeBar_ad_marker_color, DEFAULT_AD_MARKER_COLOR);
                int playedAdMarkerColor = array.getInt(R.styleable.DefaultTimeBar_played_ad_marker_color, DEFAULT_PLAYED_AD_MARKER_COLOR);
                mPlayedPaint.setColor(playedColor);
                mScrubberPaint.setColor(scrubberColor);
                mBufferedPaint.setColor(bufferedColor);
                mUnPlayedPaint.setColor(unplayedColor);
                mAdMarkerPaint.setColor(adMarkerColor);
                mPlayedAdMarkerPaint.setColor(playedAdMarkerColor);
            } finally {
                array.recycle();
            }
        } else {
            mBarHeight = defaultBarHeight;
            mTouchTargetHeight = defaultTouchTargetHeight;
            mBarGravity = BAR_GRAVITY_CENTER;
            mAdMarkerWidth = defaultAdMarkerWidth;
            mScrubberEnabledSize = defaultScrubberEnabledSize;
            mScrubberDisabledSize = defaultScrubberDisabledSize;
            mScrubberDraggedSize = defaultScrubberDraggedSize;
            mPlayedPaint.setColor(DEFAULT_PLAYED_COLOR);
            mScrubberPaint.setColor(DEFAULT_SCRUBBER_COLOR);
            mBufferedPaint.setColor(DEFAULT_BUFFERED_COLOR);
            mUnPlayedPaint.setColor(DEFAULT_UNPLAYED_COLOR);
            mAdMarkerPaint.setColor(DEFAULT_AD_MARKER_COLOR);
            mPlayedAdMarkerPaint.setColor(DEFAULT_PLAYED_AD_MARKER_COLOR);
            mScrubberDrawable = null;
        }
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        mStopScrubbingRunnable = () -> stopScrubbing(/* canceled= */ false);
        if (mScrubberDrawable != null) {
            mScrubberPadding = (mScrubberDrawable.getMinimumWidth() + 1) / 2;
        } else {
            mScrubberPadding = (Math.max(mScrubberDisabledSize,
                    Math.max(mScrubberEnabledSize, mScrubberDraggedSize)) + 1) / 2;
        }
        mScrubberScale = 1.0f;
        mScrubberScalingAnimator = new ValueAnimator();
        mScrubberScalingAnimator.addUpdateListener(animation -> {
            mScrubberScale = (float) animation.getAnimatedValue();
            invalidate(mSeekBounds);
        });
        mDuration = C.TIME_UNSET;
        mKeyTimeIncrement = C.TIME_UNSET;
        mKeyCountIncrement = DEFAULT_INCREMENT_COUNT;
        setFocusable(true);
        if (getImportantForAccessibility() == View.IMPORTANT_FOR_ACCESSIBILITY_AUTO) {
            setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_YES);
        }
    }

    /**
     * Shows the scrubber handle.
     */
    public void showScrubber() {
        showScrubber(/* showAnimationDurationMs= */ 0);
    }

    /**
     * Shows the scrubber handle with animation.
     *
     * @param showAnimationDurationMs The duration for scrubber showing animation.
     */
    public void showScrubber(long showAnimationDurationMs) {
        if (mScrubberScalingAnimator.isStarted()) {
            mScrubberScalingAnimator.cancel();
        }
        mScrubberScalingAnimator.setFloatValues(mScrubberScale, SHOWN_SCRUBBER_SCALE);
        mScrubberScalingAnimator.setDuration(showAnimationDurationMs);
        mScrubberScalingAnimator.start();
    }

    /**
     * Hides the scrubber handle.
     */
    public void hideScrubber() {
        hideScrubber(/* hideAnimationDurationMs= */ 0);
    }

    /**
     * Hides the scrubber handle with animation.
     *
     * @param hideAnimationDurationMs The duration for scrubber hiding animation.
     */
    public void hideScrubber(long hideAnimationDurationMs) {
        if (mScrubberScalingAnimator.isStarted()) {
            mScrubberScalingAnimator.cancel();
        }
        mScrubberScalingAnimator.setFloatValues(mScrubberScale, HIDDEN_SCRUBBER_SCALE);
        mScrubberScalingAnimator.setDuration(hideAnimationDurationMs);
        mScrubberScalingAnimator.start();
    }

    /**
     * Sets the color for the portion of the time bar representing media before the playback position.
     *
     * @param playedColor The color for the portion of the time bar representing media before the
     *                    playback position.
     */
    public void setPlayedColor(@ColorInt int playedColor) {
        mPlayedPaint.setColor(playedColor);
        invalidate(mSeekBounds);
    }

    /**
     * Sets the color for the scrubber handle.
     *
     * @param scrubberColor The color for the scrubber handle.
     */
    public void setScrubberColor(@ColorInt int scrubberColor) {
        mScrubberPaint.setColor(scrubberColor);
        invalidate(mSeekBounds);
    }

    /**
     * Sets the color for the portion of the time bar after the current played position up to the
     * current buffered position.
     *
     * @param bufferedColor The color for the portion of the time bar after the current played
     *                      position up to the current buffered position.
     */
    public void setBufferedColor(@ColorInt int bufferedColor) {
        mBufferedPaint.setColor(bufferedColor);
        invalidate(mSeekBounds);
    }

    /**
     * Sets the color for the portion of the time bar after the current played position.
     *
     * @param unplayedColor The color for the portion of the time bar after the current played
     *                      position.
     */
    public void setUnplayedColor(@ColorInt int unplayedColor) {
        mUnPlayedPaint.setColor(unplayedColor);
        invalidate(mSeekBounds);
    }

    /**
     * Sets the color for unplayed ad markers.
     *
     * @param adMarkerColor The color for unplayed ad markers.
     */
    public void setAdMarkerColor(@ColorInt int adMarkerColor) {
        mAdMarkerPaint.setColor(adMarkerColor);
        invalidate(mSeekBounds);
    }

    /**
     * Sets the color for played ad markers.
     *
     * @param playedAdMarkerColor The color for played ad markers.
     */
    public void setPlayedAdMarkerColor(@ColorInt int playedAdMarkerColor) {
        mPlayedAdMarkerPaint.setColor(playedAdMarkerColor);
        invalidate(mSeekBounds);
    }

    @Override
    public void addListener(@NotNull OnScrubListener listener) {
        Assertions.checkNotNull(listener);
        mListeners.add(listener);
    }

    @Override
    public void removeListener(@NotNull OnScrubListener listener) {
        mListeners.remove(listener);
    }

    @Override
    public void setKeyTimeIncrement(long time) {
        Assertions.checkArgument(time > 0);
        mKeyCountIncrement = C.INDEX_UNSET;
        mKeyTimeIncrement = time;
    }

    @Override
    public void setKeyCountIncrement(int count) {
        Assertions.checkArgument(count > 0);
        mKeyCountIncrement = count;
        mKeyTimeIncrement = C.TIME_UNSET;
    }

    @Override
    public void setPosition(long position) {
        mPosition = position;
        setContentDescription(getProgressText());
        update();
    }

    @Override
    public void setBufferedPosition(long bufferedPosition) {
        mBufferedPosition = bufferedPosition;
        update();
    }

    @Override
    public void setDuration(long duration) {
        setVisibility(duration <= 0 ? GONE : VISIBLE);
        mDuration = duration;
        if (mScrubbing && duration == C.TIME_UNSET) {
            stopScrubbing(/* canceled= */ true);
        }
        update();
    }

    @Override
    public long getPreferredUpdateDelay() {
        int timeBarWidthDp = pxToDp(mDensity, mProgressBar.width());
        return timeBarWidthDp == 0 || mDuration == 0 ||
                mDuration == C.TIME_UNSET ? Long.MAX_VALUE : mDuration / timeBarWidthDp;
    }

    @Override
    public void setAdGroupTimesMs(@Nullable long[] adGroupTimesMs, @Nullable boolean[] playedAdGroups, int adGroupCount) {
        Assertions.checkArgument(adGroupCount == 0 || (adGroupTimesMs != null && playedAdGroups != null));
        mAdGroupCount = adGroupCount;
        mAdGroupTimesMs = adGroupTimesMs;
        mPlayedAdGroups = playedAdGroups;
        update();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (mScrubbing && !enabled) {
            stopScrubbing(/* canceled= */ true);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.save();
        drawTimeBar(canvas);
        drawPlayhead(canvas);
        canvas.restore();
    }

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled() || mDuration <= 0) {
            return false;
        }
        Point touchPosition = resolveRelativeTouchPosition(event);
        int x = touchPosition.x;
        int y = touchPosition.y;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isInSeekBar(x, y)) {
                    positionScrubber(x);
                    startScrubbing(getScrubberPosition());
                    update();
                    invalidate();
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mScrubbing) {
                    if (y < mFineScrubYThreshold) {
                        int relativeX = x - mLastCoarseScrubXPosition;
                        positionScrubber(mLastCoarseScrubXPosition + relativeX / FINE_SCRUB_RATIO);
                    } else {
                        mLastCoarseScrubXPosition = x;
                        positionScrubber(x);
                    }
                    updateScrubbing(getScrubberPosition());
                    update();
                    invalidate();
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mScrubbing) {
                    stopScrubbing(/* canceled= */ event.getAction() == MotionEvent.ACTION_CANCEL);
                    return true;
                }
                break;
            default:
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isEnabled()) {
            long positionIncrement = getPositionIncrement();
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    positionIncrement = -positionIncrement;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    if (scrubIncrementally(positionIncrement)) {
                        removeCallbacks(mStopScrubbingRunnable);
                        postDelayed(mStopScrubbingRunnable, STOP_SCRUBBING_TIMEOUT_MS);
                        return true;
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_CENTER:
                case KeyEvent.KEYCODE_ENTER:
                    if (mScrubbing) {
                        stopScrubbing(/* canceled= */ false);
                        return true;
                    }
                    break;
                default:
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (mScrubbing && !gainFocus) {
            stopScrubbing(/* canceled= */ false);
        }
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        updateDrawableState();
    }

    @Override
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (mScrubberDrawable != null) {
            mScrubberDrawable.jumpToCurrentState();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int height = heightMode == MeasureSpec.UNSPECIFIED ? mTouchTargetHeight
                : heightMode == MeasureSpec.EXACTLY ? heightSize : Math.min(mTouchTargetHeight, heightSize);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height);
        updateDrawableState();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int width = right - left;
        int height = bottom - top;
        int barY = (height - mTouchTargetHeight) / 2;
        int seekLeft = getPaddingLeft();
        int seekRight = width - getPaddingRight();
        int progressY;
        if (mBarGravity == BAR_GRAVITY_BOTTOM) {
            progressY = barY + mTouchTargetHeight - (getPaddingBottom() + mScrubberPadding + mBarHeight / 2);
        } else if (mBarGravity == BAR_GRAVITY_TOP) {
            progressY = barY + getPaddingTop() + mScrubberPadding - mBarHeight / 2;
        } else {
            progressY = barY + (mTouchTargetHeight - mBarHeight) / 2;
        }
        mSeekBounds.set(seekLeft, barY, seekRight, barY + mTouchTargetHeight);
        mProgressBar.set(mSeekBounds.left + mScrubberPadding, progressY,
                mSeekBounds.right - mScrubberPadding, progressY + mBarHeight);
        if (Util.SDK_INT >= 29) {
            setSystemGestureExclusionRectsV29(width, height);
        }
        update();
    }

    @Override
    public void onRtlPropertiesChanged(int layoutDirection) {
        if (mScrubberDrawable != null && setDrawableLayoutDirection(mScrubberDrawable, layoutDirection)) {
            invalidate();
        }
    }

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_SELECTED) {
            event.getText().add(getProgressText());
        }
        event.setClassName(ACCESSIBILITY_CLASS_NAME);
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(ACCESSIBILITY_CLASS_NAME);
        info.setContentDescription(getProgressText());
        if (mDuration <= 0) {
            return;
        }
        if (Util.SDK_INT >= 21) {
            info.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_FORWARD);
            info.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_BACKWARD);
        } else {
            info.addAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            info.addAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
        }
    }

    @Override
    public boolean performAccessibilityAction(int action, @Nullable Bundle args) {
        if (super.performAccessibilityAction(action, args)) {
            return true;
        }
        if (mDuration <= 0) {
            return false;
        }
        if (action == AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD) {
            if (scrubIncrementally(-getPositionIncrement())) {
                stopScrubbing(/* canceled= */ false);
            }
        } else if (action == AccessibilityNodeInfo.ACTION_SCROLL_FORWARD) {
            if (scrubIncrementally(getPositionIncrement())) {
                stopScrubbing(/* canceled= */ false);
            }
        } else {
            return false;
        }
        sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_SELECTED);
        return true;
    }

    private void startScrubbing(long scrubPosition) {
        mScrubPosition = scrubPosition;
        mScrubbing = true;
        setPressed(true);
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
        for (OnScrubListener listener : mListeners) {
            listener.onScrubStart(this, scrubPosition);
        }
    }

    private void updateScrubbing(long scrubPosition) {
        if (mScrubPosition == scrubPosition) {
            return;
        }
        mScrubPosition = scrubPosition;
        for (OnScrubListener listener : mListeners) {
            listener.onScrubMove(this, scrubPosition);
        }
    }

    private void stopScrubbing(boolean canceled) {
        removeCallbacks(mStopScrubbingRunnable);
        mScrubbing = false;
        setPressed(false);
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(false);
        }
        invalidate();
        for (OnScrubListener listener : mListeners) {
            listener.onScrubStop(this, mScrubPosition, canceled);
        }
    }

    /**
     * Incrementally scrubs the position by {@code positionChange}.
     *
     * @param positionChange The change in the scrubber position, in milliseconds. May be negative.
     * @return Returns whether the scrubber position changed.
     */
    private boolean scrubIncrementally(long positionChange) {
        if (mDuration <= 0) {
            return false;
        }
        long previousPosition = mScrubbing ? mScrubPosition : mPosition;
        long scrubPosition = Util.constrainValue(previousPosition + positionChange, 0, mDuration);
        if (scrubPosition == previousPosition) {
            return false;
        }
        if (!mScrubbing) {
            startScrubbing(scrubPosition);
        } else {
            updateScrubbing(scrubPosition);
        }
        update();
        return true;
    }

    private void update() {
        mBufferedBar.set(mProgressBar);
        mScrubberBar.set(mProgressBar);
        long newScrubberTime = mScrubbing ? mScrubPosition : mPosition;
        if (mDuration > 0) {
            int bufferedPixelWidth = (int) ((mProgressBar.width() * mBufferedPosition) / mDuration);
            mBufferedBar.right = Math.min(mProgressBar.left + bufferedPixelWidth, mProgressBar.right);
            int scrubberPixelPosition = (int) ((mProgressBar.width() * newScrubberTime) / mDuration);
            mScrubberBar.right = Math.min(mProgressBar.left + scrubberPixelPosition, mProgressBar.right);
        } else {
            mBufferedBar.right = mProgressBar.left;
            mScrubberBar.right = mProgressBar.left;
        }
        invalidate(mSeekBounds);
    }

    private void positionScrubber(float xPosition) {
        mScrubberBar.right = Util.constrainValue((int) xPosition, mProgressBar.left, mProgressBar.right);
    }

    private Point resolveRelativeTouchPosition(MotionEvent motionEvent) {
        mTouchPosition.set((int) motionEvent.getX(), (int) motionEvent.getY());
        return mTouchPosition;
    }

    private long getScrubberPosition() {
        if (mProgressBar.width() <= 0 || mDuration == C.TIME_UNSET) {
            return 0;
        }
        return (mScrubberBar.width() * mDuration) / mProgressBar.width();
    }

    private boolean isInSeekBar(float x, float y) {
        return mSeekBounds.contains((int) x, (int) y);
    }

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    private void drawTimeBar(Canvas canvas) {
        int progressBarHeight = mProgressBar.height();
        int barTop = mProgressBar.centerY() - progressBarHeight / 2;
        int barBottom = barTop + progressBarHeight;
        if (mDuration <= 0) {
            canvas.drawRoundRect(mProgressBar.left, barTop, mProgressBar.right,
                    barBottom, mBarHeight / 2, mBarHeight / 2, mUnPlayedPaint);
            return;
        }
        int bufferedLeft = mBufferedBar.left;
        int bufferedRight = mBufferedBar.right;
        int progressLeft = Math.max(Math.max(mProgressBar.left, bufferedRight), mScrubberBar.right);
        if (progressLeft < mProgressBar.right) {
            canvas.drawRoundRect(mScrubberBar.left, barTop, mProgressBar.right,
                    barBottom, mBarHeight / 2, mBarHeight / 2, mUnPlayedPaint);
        }
        bufferedLeft = Math.max(bufferedLeft, mScrubberBar.right);
        if (bufferedRight > bufferedLeft) {
            canvas.drawRoundRect(bufferedLeft, barTop, bufferedRight,
                    barBottom, mBarHeight / 2, mBarHeight / 2, mBufferedPaint);
        }
        if (mScrubberBar.width() > 0) {
            canvas.drawRoundRect(mScrubberBar.left, barTop, mScrubberBar.right,
                    barBottom, mBarHeight / 2, mBarHeight / 2, mPlayedPaint);
        }
        if (mAdGroupCount == 0) {
            return;
        }
        long[] adGroupTimesMs = Assertions.checkNotNull(mAdGroupTimesMs);
        boolean[] playedAdGroups = Assertions.checkNotNull(mPlayedAdGroups);
        int adMarkerOffset = mAdMarkerWidth / 2;
        for (int i = 0; i < mAdGroupCount; i++) {
            long adGroupTimeMs = Util.constrainValue(adGroupTimesMs[i], 0, mDuration);
            int markerPositionOffset = (int) (mProgressBar.width() * adGroupTimeMs / mDuration) - adMarkerOffset;
            int markerLeft = mProgressBar.left + Math.min(mProgressBar.width() - mAdMarkerWidth, Math.max(0, markerPositionOffset));
            Paint paint = playedAdGroups[i] ? mPlayedAdMarkerPaint : mAdMarkerPaint;
            canvas.drawRect(markerLeft, barTop, markerLeft + mAdMarkerWidth, barBottom, paint);
        }
    }

    private void drawPlayhead(Canvas canvas) {
        if (mDuration <= 0) {
            return;
        }
        int playheadX = Util.constrainValue(mScrubberBar.right, mScrubberBar.left, mProgressBar.right);
        int playheadY = mScrubberBar.centerY();
        if (mScrubberDrawable == null) {
            int scrubberSize = (mScrubbing || isFocused())
                    ? mScrubberDraggedSize : (isEnabled() ? mScrubberEnabledSize : mScrubberDisabledSize);
            int playheadRadius = (int) ((scrubberSize * mScrubberScale) / 2);
            canvas.drawCircle(playheadX, playheadY, playheadRadius, mScrubberPaint);
        } else {
            int scrubberDrawableWidth = (int) (mScrubberDrawable.getIntrinsicWidth() * mScrubberScale);
            int scrubberDrawableHeight = (int) (mScrubberDrawable.getIntrinsicHeight() * mScrubberScale);
            mScrubberDrawable.setBounds(
                    playheadX - scrubberDrawableWidth / 2, playheadY - scrubberDrawableHeight / 2,
                    playheadX + scrubberDrawableWidth / 2, playheadY + scrubberDrawableHeight / 2);
            mScrubberDrawable.draw(canvas);
        }
    }

    private void updateDrawableState() {
        if (mScrubberDrawable != null && mScrubberDrawable.isStateful()
                && mScrubberDrawable.setState(getDrawableState())) {
            invalidate();
        }
    }

    @RequiresApi(29)
    private void setSystemGestureExclusionRectsV29(int width, int height) {
        if (mLastExclusionRectangle != null
                && mLastExclusionRectangle.width() == width
                && mLastExclusionRectangle.height() == height) {
            // Allocating inside onLayout is considered a DrawAllocation lint error, so avoid if possible.
            return;
        }
        mLastExclusionRectangle = new Rect(/* left= */ 0, /* top= */ 0, width, height);
//        setSystemGestureExclusionRects(Collections.singletonList(lastExclusionRectangle));
    }

    private String getProgressText() {
        return Util.getStringForTime(mFormatBuilder, mFormatter, mPosition);
    }

    private long getPositionIncrement() {
        return mKeyTimeIncrement == C.TIME_UNSET ?
                (mDuration == C.TIME_UNSET ? 0 : (mDuration / mKeyCountIncrement)) : mKeyTimeIncrement;
    }

    @SuppressWarnings("UnusedReturnValue")
    private boolean setDrawableLayoutDirection(Drawable drawable) {
        return Util.SDK_INT >= 23 && setDrawableLayoutDirection(drawable, getLayoutDirection());
    }

    private static boolean setDrawableLayoutDirection(Drawable drawable, int layoutDirection) {
        return Util.SDK_INT >= 23 && drawable.setLayoutDirection(layoutDirection);
    }

    private static int dpToPx(float density, int dps) {
        return (int) (dps * density + 0.5f);
    }

    private static int pxToDp(float density, int px) {
        return (int) (px / density);
    }

}
