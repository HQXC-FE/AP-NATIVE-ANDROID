package com.xtree.live.player;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.media3.ui.PlayerView;

public class ExoPlayerView extends PlayerView {
    public ExoPlayerView(Context context) {
        super(context);
    }

    public ExoPlayerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ExoPlayerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private long lastClickTime = 0L;
    private static final int FAST_CLICK_DELAY_TIME = 500; // 快速点击间隔
    @Override
    public boolean performClick() {
        if (System.currentTimeMillis() - lastClickTime < FAST_CLICK_DELAY_TIME){
            return true;
        }
        lastClickTime = System.currentTimeMillis();
        if(getPlayer() != null && !getPlayer().isPlaying()){
            return false;
        }
        return super.performClick();
    }
}

