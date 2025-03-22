package com.xtree.live.manager;

import android.support.v4.media.session.MediaSessionCompat;

import com.blankj.utilcode.util.LogUtils;
import com.xtree.live.player.WidgetExoPlayer;

public class MediaSessionCallback extends MediaSessionCompat.Callback{

    private final WidgetExoPlayer exoPlayer;

    public MediaSessionCallback(WidgetExoPlayer exoPlayer) {
        this.exoPlayer = exoPlayer;
    }

    @Override
    public void onPlay() {
        LogUtils.d("MediaSessionCallback", "onPlay");
        super.onPlay();
        if(exoPlayer != null)exoPlayer.resume();
    }

    @Override
    public void onPause() {
        LogUtils.d("MediaSessionCallback", "onPause");
        super.onPause();
        if(exoPlayer != null)exoPlayer.pause();
    }

}
